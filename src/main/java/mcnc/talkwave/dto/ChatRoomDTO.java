package mcnc.talkwave.dto;

import lombok.*;
import mcnc.talkwave.entity.Chat;
import mcnc.talkwave.entity.ChatRoom;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatRoomDTO {
    private Long roomId;
    private String roomName;
    private ChatDTO chatDTO;

    public ChatRoomDTO(ChatRoom chatRoom, Chat chat) {
        this.roomId = chatRoom.getId();
        this.roomName = chatRoom.getName();
        this.chatDTO = new ChatDTO(chat);
    }
}