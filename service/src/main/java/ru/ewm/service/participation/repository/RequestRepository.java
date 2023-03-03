package ru.ewm.service.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ewm.service.constants.State;
import ru.ewm.service.participation.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Optional<ParticipationRequest> findFirstByEventIdIsAndRequesterIdIs(Long eventId, Long requesterId);

    List<ParticipationRequest> findAllByRequesterIdIs(Long requesterId);

    List<ParticipationRequest> findAllByEventIdIs(Long eventId);

    List<ParticipationRequest> findAllByEventIdInAndStatusIs(List<Long> eventIds, State status);
}
