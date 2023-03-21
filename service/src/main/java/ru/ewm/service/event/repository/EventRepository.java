package ru.ewm.service.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ewm.service.constants.State;
import ru.ewm.service.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    List<Event> findAllByIdIsGreaterThanEqualAndInitiatorIdIs(Long id, Long initiatorId, Pageable pageable);

    @Query(" select e from Event e " +
            "where e.id >= ?1 " +
            "and e.initiator.id in ?2 " +
            "and e.state = ?3 " +
            "and e.eventDate > ?4")
    List<Event> findSubscriptionsEvents(Long id,
                                        List<Long> initiatorIds,
                                        State state,
                                        LocalDateTime now,
                                        Pageable pageable);
}
