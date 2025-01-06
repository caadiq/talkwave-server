package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.dto.*;
import mcnc.talkwave.entity.*;
import mcnc.talkwave.repository.ChatRepository;
import mcnc.talkwave.repository.ChatRoomRepository;
import mcnc.talkwave.repository.ChatRoomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final RoomCacheService roomCacheService;
    private final UserCacheService userCacheService;
    private final EmojiCacheService emojiCacheService;

    // 채팅방 생성
    @Transactional
    public ChatRoom createChatRoom(CreateRoomDTO createRoomDTO) {
        ChatRoom chatRoom = ChatRoom.createRoom(createRoomDTO.getRoomName());
        chatRoomRepository.save(chatRoom);
        User creator = userCacheService.getUserDetails(createRoomDTO.getUserId());
        List<ChatRoomUser> chatRoomUserList = Optional.ofNullable(createRoomDTO.getUserList())
                .orElse(Collections.emptyList())
                .stream()
                .map(userId -> new ChatRoomUser(chatRoom, userCacheService.getUserDetails(userId)))
                .collect(Collectors.toList());
        chatRoomUserList.add(new ChatRoomUser(chatRoom, creator));
        chatRoomUserRepository.saveAll(chatRoomUserList);
        saveChatMessage(chatRoom.getId(), createRoomDTO.getUserId(), creator.getName() + "님이 방을 생성하셨습니다.");
        return chatRoom;
    }

    // 채팅방 조회
    public List<ChatRoomDTO> findAllChatRooms(String userId) {
        return chatRoomRepository.findLatestChatForRooms(userId);
    }

    // 채팅 메시지 저장
    @Transactional
    public Chat saveChatMessage(Long roomId, String userId, String message) {
        ChatRoom room = roomCacheService.getRoomDetails(roomId);
        User sender = userCacheService.getUserDetails(userId);
        Chat chat = Chat.createChat(room, sender, message);
        return chatRepository.save(chat);
    }

    @Transactional
    public ChatDTO saveUserSentMessage(ChatRequestDTO chatRequestDTO) {
        Chat chat = saveChatMessage(chatRequestDTO.getRoomId(), chatRequestDTO.getUserId(), chatRequestDTO.getMessage());
        if (chatRequestDTO.getEmojiId() != null) {
            chat.setEmoji(emojiCacheService.getEmojiDetails(chatRequestDTO.getEmojiId()));
        }
        chatRepository.save(chat);
        return ChatDTO.of(chat);
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
