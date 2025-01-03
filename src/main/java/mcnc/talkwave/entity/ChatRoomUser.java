package mcnc.talkwave.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;   // 채팅방

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 사용자

    @CreationTimestamp
    private LocalDateTime joinTime;

    public ChatRoomUser(ChatRoom room, User user) {
        this.chatRoom = room;
        this.user = user;
    }

}
