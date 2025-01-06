package mcnc.talkwave.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcnc.talkwave.dto.*;
import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.entity.Emoji;
import mcnc.talkwave.entity.User;
import mcnc.talkwave.service.ChatRoomService;
import mcnc.talkwave.service.ChatService;
import mcnc.talkwave.service.EmojiService;
import mcnc.talkwave.service.UserCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final EmojiService emojiService;
    private final UserCacheService userCacheService;
    private final SimpMessagingTemplate messagingTemplate; // STOMP 메시지 전송 도구

    // 채팅방 생성
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody CreateRoomDTO createRoomDTO) {
        ChatRoom chatRoom = chatService.createChatRoom(createRoomDTO);
        return ResponseEntity.ok(chatRoom);
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getChatRooms(@RequestParam String userId) {
        List<ChatRoomDTO> chatRooms = chatService.findAllChatRooms(userId);
        return ResponseEntity.ok(chatRooms);
    }

    // 특정 채팅방의 채팅 내역 조회
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<ChatListDTO> getChatMessages(@PathVariable Long roomId) {
        ChatListDTO chats = chatService.getChatMessages(roomId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/emoji")
    public ResponseEntity<List<Emoji>> getAllEmojis() {
        return ResponseEntity.ok(emojiService.getAllEmojis());
    }

    @PostMapping("/rooms/leave")
    public void handleLeave(@RequestBody ChatEventDTO chatEventDTO) {
        chatRoomService.leaveRoom(chatEventDTO.getRoomId(), chatEventDTO.getUserId());
        User enteredUser = userCacheService.getUserDetails(chatEventDTO.getUserId());
        ChatDTO chatDTO = new ChatDTO(enteredUser, enteredUser.getName() + "님이 퇴장하셨습니다.");
        messagingTemplate.convertAndSend(
                "/room/" + chatEventDTO.getRoomId(),
                chatDTO
        );
    }

    // 채팅 메시지 전송 (STOMP 사용)
    @MessageMapping("/message") // 클라이언트에서 보낼 경로
    public void sendMessage(@Payload ChatRequestDTO chatRequestDTO) {
        // 메시지 저장
        log.info("chat received: {}", chatRequestDTO.toString());
        ChatDTO chatDTO = chatService.saveChatMessage(chatRequestDTO.getRoomId(), chatRequestDTO.getUserId(), chatRequestDTO.getMessage());

        // 구독한 클라이언트에게 메시지 전송
        messagingTemplate.convertAndSend(
                "/room/" + chatRequestDTO.getRoomId(),
                chatDTO
        );
    }
//
//    @MessageMapping("/join")
//    public void handleJoin(@Payload ChatEventDTO chatEventDTO) {
//        boolean joinResult = chatRoomService.joinRoom(chatEventDTO.getRoomId(), chatEventDTO.getUserId());
//        if (joinResult) {
//            User enteredUser = userCacheService.getUserDetails(chatEventDTO.getUserId());
//            ChatDTO chatDTO = new ChatDTO(enteredUser, enteredUser.getName() + "님이 입장하셨습니다.");
//
//            messagingTemplate.convertAndSend(
//                    "/room/" + chatEventDTO.getRoomId(),
//                    chatDTO
//            );
//        }
//    }
//
//    @MessageMapping("/leave")
//    public void handleLeave(@Payload ChatEventDTO chatEventDTO) {
//        chatRoomService.leaveRoom(chatEventDTO.getRoomId(), chatEventDTO.getUserId());
//        User enteredUser = userCacheService.getUserDetails(chatEventDTO.getUserId());
//        ChatDTO chatDTO = new ChatDTO(enteredUser, enteredUser.getName() + "님이 퇴장하셨습니다.");
//        messagingTemplate.convertAndSend(
//                "/room/" + chatEventDTO.getRoomId(),
//                chatDTO
//        );
//    }
}
