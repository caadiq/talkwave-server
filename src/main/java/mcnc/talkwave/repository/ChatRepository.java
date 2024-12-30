package mcnc.talkwave.repository;

import mcnc.talkwave.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByRoomIdOrderBySendDateDesc(Long roomId);
}