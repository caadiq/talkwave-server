package mcnc.talkwave.repository;

import mcnc.talkwave.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

    void deleteByChatRoom_IdAndUser_UserId(Long roomId, String userId);
    List<ChatRoomUser> findByChatRoom_Id(Long roomId);
    boolean existsByChatRoom_IdAndUser_UserId(Long roomId, String userId);
}
