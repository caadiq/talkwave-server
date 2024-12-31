package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.ChatDTO;
import mcnc.talkwave.dto.ChatMessageDTO;
import mcnc.talkwave.dto.ChatResponseDTO;
import mcnc.talkwave.dto.ChatRoomDTO;
import mcnc.talkwave.entity.Chat;
import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.entity.User;
import mcnc.talkwave.repository.ChatRepository;
import mcnc.talkwave.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final RoomCacheService roomCacheService;
    private final UserCacheService userCacheService;

    // 채팅방 생성
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.createRoom(name);
        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방 조회
    public List<ChatRoomDTO> findAllChatRooms() {
        return chatRoomRepository.findLatestChatForRooms();
    }

    // 채팅 메시지 저장
    @Transactional
    public ChatResponseDTO saveChatMessage(ChatMessageDTO chatMessageDTO) {
        ChatRoom room = roomCacheService.getRoomDetails(chatMessageDTO.getRoomId());
        User sender = userCacheService.getUserDetails(chatMessageDTO.getUserId());
        Chat chat = Chat.createChat(room, sender, chatMessageDTO.getMessage());
        chatRepository.save(chat);
        return ChatResponseDTO.of(chat, room.getId(), sender);
    }

    // 특정 채팅방의 채팅 내역 조회
    public List<ChatDTO> getChatMessages(Long roomId) {
        return chatRepository.findByRoomIdOrderBySendDate(roomId)
                .stream().map(ChatDTO::new)
                .toList();
    }
}
