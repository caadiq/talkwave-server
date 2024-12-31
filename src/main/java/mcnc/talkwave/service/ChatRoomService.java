package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.entity.ChatRoomUser;
import mcnc.talkwave.entity.User;
import mcnc.talkwave.repository.ChatRoomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final RoomCacheService roomCacheService;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserCacheService userCacheService;

    @Transactional
    public void joinRoom(Long roomId, String userId) {
        ChatRoom room = roomCacheService.getRoomDetails(roomId);
        User user = userCacheService.getUserDetails(userId);
        chatRoomUserRepository.save(new ChatRoomUser(room, user));
    }

    @Transactional
    public void leaveRoom(Long roomId, String userId) {
        chatRoomUserRepository.deleteByChatRoom_IdAndUser_UserId(roomId, userId);
    }
}
