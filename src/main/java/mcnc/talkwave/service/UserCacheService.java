package mcnc.talkwave.service;

import mcnc.talkwave.entity.User;
import mcnc.talkwave.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Service
public class UserCacheService {

    private final Cache<String, User> userCache;
    private final UserRepository userRepository;

    public UserCacheService(UserRepository userRepository) {
        this.userRepository = userRepository;

        // Caffeine 캐시 설정 (10분 유지, 최대 1000개)
        this.userCache = Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    /**
     * 캐시 또는 DB에서 User 정보를 가져옵니다.
     * @param userId 사용자 ID
     * @return ChatUser 객체
     */
    public User getUserDetails(String userId) {
        // 캐시에서 먼저 조회
        User user = userCache.getIfPresent(userId);
        if (user != null) {
            return user;
        }

        // DB에서 조회 후 캐시에 저장
        return userRepository.findById(userId)
                .map(foundUser -> {
                    userCache.put(userId, foundUser); // 캐시에 저장
                    return foundUser;
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    /**
     * 캐시에 User 정보를 수동으로 업데이트
     * @param userId 사용자 ID
     * @param user 사용자 정보 객체
     */
    public void updateUserDetails(String userId, User user) {
        userCache.put(userId, user);
    }

    /**
     * 캐시에서 User 정보 제거
     */
    public void evictUser(String userId) {
        userCache.invalidate(userId);
    }
}
