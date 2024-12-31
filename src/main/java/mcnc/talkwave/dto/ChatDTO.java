package mcnc.talkwave.dto;

import lombok.*;
import mcnc.talkwave.entity.Chat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatDTO {
    private Long chatId;
    private String message;
    private LocalDateTime sendDate;
    private String userId;
    private String userName;

    public ChatDTO(Chat chat) {
        this.chatId = chat.getId();
        this.message = chat.getMessage();
        this.sendDate = chat.getSendDate();
        this.userId = chat.getSender().getUserId();
        this.userName = chat.getSender().getName();
    }
}
