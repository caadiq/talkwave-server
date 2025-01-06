package mcnc.talkwave.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDate;

    @ManyToOne
    @Setter
    @JoinColumn(name = "emoji_id")
    private Emoji emoji;

    @Builder
    public Chat(ChatRoom room, User sender, String message) {
        this.room = room;
        this.sender = sender;
        this.message = message;
        this.sendDate = LocalDateTime.now();
    }
    /**
     * 채팅 생성
     *
     * @param room    채팅 방
     * @param sender  보낸이
     * @param message 내용
     * @return Chat Entity
     */
    public static Chat createChat(ChatRoom room, User sender, String message) {
        return Chat.builder()
                .room(room)
                .sender(sender)
                .message(message)
                .build();
    }
}
