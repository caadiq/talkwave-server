package mcnc.talkwave.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import mcnc.talkwave.entity.Emoji;
import mcnc.talkwave.repository.EmojiRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmojiCacheService {

    private final Cache<Long, Emoji> emojiCache;
    private final EmojiRepository emojiRepository;

    public EmojiCacheService(EmojiRepository emojiRepository) {
        this.emojiRepository = emojiRepository;

        // Caffeine 캐시 설정 (10분 유지, 최대 1000개)
        this.emojiCache = Caffeine.newBuilder()
                .expireAfterWrite(100, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    /**
     * 캐시 또는 DB에서 Emoji 정보를 가져옵니다.
     * @param emojiId 이모지 ID
     * @return ChatEmoji 객체
     */
    public Emoji getEmojiDetails(Long emojiId) {
        // 캐시에서 먼저 조회
        Emoji emoji = emojiCache.getIfPresent(emojiId);
        if (emoji != null) {
            return emoji;
        }

        // DB에서 조회 후 캐시에 저장
        return emojiRepository.findById(emojiId)
                .map(foundEmoji -> {
                    emojiCache.put(emojiId, foundEmoji); // 캐시에 저장
                    return foundEmoji;
                })
                .orElseThrow(() -> new IllegalArgumentException("Emoji not found with ID: " + emojiId));
    }

    /**
     * 캐시에 Emoji 정보를 수동으로 업데이트
     * @param emojiId emoji ID
     * @param emoji emoji 정보 객체
     */
    public void updateEmojiDetails(Long emojiId, Emoji emoji) {
        emojiCache.put(emojiId, emoji);
    }

    /**
     * 캐시에서 Emoji 정보 제거
     */
    public void evictEmoji(Long emojiId) {
        emojiCache.invalidate(emojiId);
    }
}
