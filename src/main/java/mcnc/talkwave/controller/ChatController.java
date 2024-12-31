package mcnc.talkwave.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcnc.talkwave.dto.ChatDTO;
import mcnc.talkwave.dto.ChatMessageDTO;
import mcnc.talkwave.dto.ChatResponseDTO;
import mcnc.talkwave.dto.ChatRoomDTO;
import mcnc.talkwave.entity.Chat;
import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate; // STOMP 메시지 전송 도구

    // 채팅방 생성
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam String name) {
        ChatRoom chatRoom = chatService.createChatRoom(name);
        return ResponseEntity.ok(chatRoom);
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getChatRooms() {
        List<ChatRoomDTO> chatRooms = chatService.findAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    // 특정 채팅방의 채팅 내역 조회
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatDTO>> getChatMessages(@PathVariable Long roomId) {
        List<ChatDTO> chats = chatService.getChatMessages(roomId);
        return ResponseEntity.ok(chats);
    }

    // 채팅 메시지 전송 (STOMP 사용)
    @MessageMapping("/message") // 클라이언트에서 보낼 경로
    public void sendMessage(@Payload ChatMessageDTO chatMessageDTO) {
        // 메시지 저장
        log.info("chat received: {}", chatMessageDTO.toString());
        ChatResponseDTO chatResponseDTO = chatService.saveChatMessage(chatMessageDTO);

        // 구독한 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend(
                "/room/" + chatResponseDTO.getRoomId(),
                chatResponseDTO
        );
    }
}
