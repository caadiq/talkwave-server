package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.ChatResponseDTO;
import mcnc.talkwave.entity.Chat;
import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.entity.User;
import mcnc.talkwave.repository.ChatRepository;
import mcnc.talkwave.repository.ChatRoomRepository;
import mcnc.talkwave.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    // 채팅방 생성
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.createRoom(name);
        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방 조회
    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    // 채팅 메시지 저장
    @Transactional
    public ChatResponseDTO saveChatMessage(ChatResponseDTO chatMessageDTO) {
        ChatRoom room = chatRoomRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));
        User sender = userRepository.findById(chatMessageDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Chat chat = Chat.createChat(room, sender, chatMessageDTO.getMessage());
        chatRepository.save(chat);
        ChatResponseDTO.of(chat, room.getId(), sender);
    }

    // 특정 채팅방의 채팅 내역 조회
    public Page<Chat> getChatMessages(Long roomId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return chatRepository.findByRoomIdOrderBySendDateDesc(roomId, pageable);
    }
}
