package mcnc.talkwave.dto;

import lombok.*;
import mcnc.talkwave.entity.ChatRoom;
import mcnc.talkwave.entity.ChatRoomUser;
import mcnc.talkwave.entity.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatListDTO {
    private Long roomId;
    private String roomName;
    private List<UserDeptDTO.UserInfoDTO> userList;
    private List<ChatDTO> chatList;

    public ChatListDTO(ChatRoom chatRoom, List<ChatRoomUser> userList, List<ChatDTO> chatList) {
        this.roomId = chatRoom.getId();
        this.roomName = chatRoom.getName();
        this.userList = userList.stream().map(UserDeptDTO.UserInfoDTO::new)
                .toList();
        this.chatList = chatList;
    }
}
