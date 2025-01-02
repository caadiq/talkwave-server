package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.ChatDTO;
import mcnc.talkwave.dto.ChatListDTO;
import mcnc.talkwave.dto.ChatRequestDTO;
import mcnc.talkwave.dto.ChatRoomDTO;
import mcnc.talkwave.entity.Chat;
import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.entity.ChatRoomUser;
import mcnc.talkwave.entity.User;
import mcnc.talkwave.repository.ChatRepository;
import mcnc.talkwave.repository.ChatRoomRepository;
import mcnc.talkwave.repository.ChatRoomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final RoomCacheService roomCacheService;
    private final UserCacheService userCacheService;

    // 채팅방 생성
    @Transactional
    public ChatRoom createChatRoom(String name, String userId) {
        ChatRoom chatRoom = ChatRoom.createRoom(name);
        chatRoomRepository.save(chatRoom);
        User creator = userCacheService.getUserDetails(userId);
        saveChatMessage(chatRoom.getId(), userId, creator.getName() + "님이 방을 생성하셨습니다.");
        return chatRoom;
    }

    // 채팅방 조회
    public List<ChatRoomDTO> findAllChatRooms() {
        return chatRoomRepository.findLatestChatForRooms();
    }

    // 채팅 메시지 저장
    @Transactional
    public ChatDTO saveChatMessage(Long roomId, String userId, String message) {
        ChatRoom room = roomCacheService.getRoomDetails(roomId);
        User sender = userCacheService.getUserDetails(userId);
        Chat chat = Chat.createChat(room, sender, message);
        chatRepository.save(chat);
        return ChatDTO.of(chat, sender);
    }

    // 특정 채팅방의 채팅 내역 조회
    public ChatListDTO getChatMessages(Long roomId) {
        List<ChatDTO> chatDTOList = chatRepository.findByRoomIdOrderBySendDate(roomId)
                .stream().map(ChatDTO::new)
                .toList();
        ChatRoom room = roomCacheService.getRoomDetails(roomId);
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findByChatRoom_Id(roomId);
        return new ChatListDTO(room, chatRoomUserList, chatDTOList);
    }
}
