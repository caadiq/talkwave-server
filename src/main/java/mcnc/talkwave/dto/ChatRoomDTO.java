package mcnc.talkwave.dto;

import lombok.*;
import mcnc.talkwave.entity.Chat;
import mcnc.talkwave.entity.ChatRoom;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatRoomDTO {
    private Long roomId;
    private String roomName;
    private String latestMessage;
    private LocalDateTime sendDate;

    public ChatRoomDTO(ChatRoom chatRoom, Chat chat) {
        this.roomId = chatRoom.getId();
        this.roomName = chatRoom.getName();
        this.latestMessage = chat.getEmoji() != null ? "(이모티콘) " + chat.getMessage() : chat.getMessage();
        this.sendDate = chat.getSendDate();
    }
}
