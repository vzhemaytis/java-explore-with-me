package ru.ewm.service.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ewm.service.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    List<Event> findAllByIdIsGreaterThanEqualAndInitiatorIdIs(Long id, Long initiatorId, Pageable pageable);
}
