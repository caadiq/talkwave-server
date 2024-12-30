package mcnc.talkwave.repository;

import mcnc.talkwave.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findByRoomIdOrderBySendDateDesc(Long roomId, Pageable pageable);
}