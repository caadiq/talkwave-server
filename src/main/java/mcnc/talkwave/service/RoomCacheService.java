package mcnc.talkwave.service;

import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Service
public class RoomCacheService {

    private final Cache<Long, ChatRoom> roomCache;
    private final ChatRoomRepository chatRoomRepository;

    public RoomCacheService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;

        // Caffeine 캐시 설정 (10분 유지, 최대 1000개)
        this.roomCache = Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    /**
     * 캐시 또는 DB에서 Room 정보를 가져옵니다.
     * @param roomId 방 ID
     * @return ChatRoom 객체
     */
    public ChatRoom getRoomDetails(Long roomId) {
        // 캐시에서 먼저 조회
        ChatRoom room = roomCache.getIfPresent(roomId);
        if (room != null) {
            return room;
        }

        // DB에서 조회 후 캐시에 저장
        return chatRoomRepository.findById(roomId)
                .map(chatRoom -> {
                    roomCache.put(roomId, chatRoom); // 캐시에 저장
                    return chatRoom;
                })
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));
    }

    /**
     * 캐시에 Room 정보를 수동으로 업데이트
     * @param roomId 방 ID
     * @param chatRoom 방 정보 객체
     */
    public void updateRoomDetails(Long roomId, ChatRoom chatRoom) {
        roomCache.put(roomId, chatRoom);
    }

    /**
     * 캐시에서 Room 정보 제거
     * @param roomId 방 ID
     */
    public void evictRoom(Long roomId) {
        roomCache.invalidate(roomId);
    }
}
