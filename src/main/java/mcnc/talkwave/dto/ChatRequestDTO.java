package mcnc.talkwave.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChatRequestDTO {
    private Long roomId;      // 채팅방 ID
    private String userId;    // 보낸 유저 ID
    private String message;   // 채팅 메시지 내용
    private Long emojiId;
}
