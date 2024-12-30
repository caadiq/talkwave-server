package mcnc.talkwave.repository;

import mcnc.talkwave.entity.Departure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartureRepository extends JpaRepository<Departure, Long> {
}
