package mcnc.talkwave.repository;

import mcnc.talkwave.entity.Departure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartureRepository extends JpaRepository<Departure, Long> {

    boolean existsByName(String name);
}
