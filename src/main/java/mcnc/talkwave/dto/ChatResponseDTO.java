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
public class ChatResponseDTO {
    private Long roomId;      // 채팅방 ID
    private String userId;    // 보낸 유저 ID
    private String userName;
    private String message;   // 채팅 메시지 내용
    private LocalDateTime sendDate;

    public static ChatResponseDTO of(Chat chat, Long roomId, User user) {
        return ChatResponseDTO.builder()
                .roomId(roomId)
                .userId(user.getUserId())
                .userName(user.getName())
                .message(chat.getMessage())
                .sendDate(chat.getSendDate())
                .build();
    }
}