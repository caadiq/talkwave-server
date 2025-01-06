package mcnc.talkwave.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mcnc.talkwave.entity.Emoji;
import mcnc.talkwave.repository.EmojiRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmojiService {
    private final EmojiRepository emojiRepository;

    public List<Emoji> getAllEmojis() {
        return emojiRepository.findAll();
    }
}
