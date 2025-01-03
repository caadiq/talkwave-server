package mcnc.talkwave.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class CreateRoomDTO {
    private String roomName;
    private String userId;
    private List<String> userList;
}
