package mcnc.talkwave.repository;

import mcnc.talkwave.dto.ChatRoomDTO;
import mcnc.talkwave.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select new mcnc.talkwave.dto.ChatRoomDTO(r, c) " +
           "from ChatRoom r " +
           "join Chat c on r = c.room " +
           "where c.sendDate = (select max(c2.sendDate) from Chat c2 where c2.room = r) " +
           "order by c.sendDate desc")
    List<ChatRoomDTO> findLatestChatForRooms();


}