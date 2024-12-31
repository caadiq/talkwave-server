package mcnc.talkwave.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChatEventDTO {
    private Long roomId;
    private String userId;
}
