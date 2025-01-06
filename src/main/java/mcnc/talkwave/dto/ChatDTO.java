package mcnc.talkwave.dto;

import lombok.*;
import mcnc.talkwave.entity.Chat;
import mcnc.talkwave.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatDTO {
    private String message;
    private LocalDateTime sendDate;
    private String userId;
    private String userName;
    private String emojiUrl;

    public ChatDTO(Chat chat) {
        this.message = chat.getMessage();
        this.sendDate = chat.getSendDate();
        this.userId = chat.getSender().getUserId();
        this.userName = chat.getSender().getName();
        this.emojiUrl = chat.getEmoji().getUrl();
    }

    public ChatDTO(User user, String message) {
        this.message = message;
        this.sendDate = LocalDateTime.now();
        this.userId = user.getUserId();
        this.userName = user.getName();
    }


    public static ChatDTO of(Chat chat) {
        return ChatDTO.builder()
                .userId(chat.getSender().getUserId())
                .userName(chat.getSender().getName())
                .message(chat.getMessage())
                .sendDate(chat.getSendDate())
                .build();
    }


}
