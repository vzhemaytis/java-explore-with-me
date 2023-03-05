package ru.ewm.service.participation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.State;
import ru.ewm.service.error.ForbiddenException;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.participation.dto.EventRequestStatusUpdateRequest;
import ru.ewm.service.participation.dto.EventRequestStatusUpdateResult;
import ru.ewm.service.participation.dto.ParticipationRequestDto;
import ru.ewm.service.participation.mapper.RequestMapper;
import ru.ewm.service.participation.model.ParticipationRequest;
import ru.ewm.service.participation.repository.RequestRepository;
import ru.ewm.service.participation.service.CommonRequestService;
import ru.ewm.service.participation.service.PrivateRequestService;
import ru.ewm.service.user.model.User;
import ru.ewm.service.validation.EntityFoundValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.ewm.service.participation.mapper.RequestMapper.toParticipationRequestDto;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final RequestRepository requestRepository;
    private final EntityFoundValidator entityFoundValidator;
    private final CommonRequestService commonRequestService;

    @Transactional
    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = entityFoundValidator.checkIfUserExist(userId);
        Event event = entityFoundValidator.checkIfEventExist(eventId);

        Optional<ParticipationRequest> optionalRequest = requestRepository
                .findFirstByEventIdIsAndRequesterIdIs(event.getId(), requester.getId());

        if (optionalRequest.isPresent()) {
            throw new ForbiddenException("participation request for this event is already exist");
        }

        if (event.getInitiator().equals(requester)) {
            throw new ForbiddenException("participation request could not be created by event initiator");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("participation request could not be created for not published event");
        }

        List<ParticipationRequest> confirmedRequests = commonRequestService
                .findConfirmedRequests(List.of(event));
        if (confirmedRequests.size() == event.getParticipantLimit()) {
            throw new ForbiddenException("participation limit is reached");
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(State.PUBLISHED);
        } else {
            participationRequest.setStatus(State.PENDING);
        }

        return toParticipationRequestDto(requestRepository.save(participationRequest));
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> findAllUsersRequests(Long userId) {
        List<ParticipationRequest> foundRequests = requestRepository.findAllByRequesterIdIs(userId);
        return foundRequests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User requester = entityFoundValidator.checkIfUserExist(userId);
        ParticipationRequest request = entityFoundValidator.checkIfRequestExist(requestId);
        if (!request.getRequester().equals(requester)) {
            throw new ForbiddenException("participation request could be cancelled only by requester");
        }
        request.setStatus(State.CANCELED);
        return toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        User initiator = entityFoundValidator.checkIfUserExist(userId);
        Event event = entityFoundValidator.checkIfEventExist(eventId);
        if (!event.getInitiator().equals(initiator)) {
            throw new ForbiddenException("user is not event initiator");
        }
        List<ParticipationRequest> foundRequests = requestRepository.findAllByEventIdIs(eventId);
        return foundRequests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId,
                                                        Long eventId,
                                                        EventRequestStatusUpdateRequest request) {
        User initiator = entityFoundValidator.checkIfUserExist(userId);
        Event event = entityFoundValidator.checkIfEventExist(eventId);
        if (!event.getInitiator().equals(initiator)) {
            throw new ForbiddenException("user is not event initiator");
        }

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ForbiddenException("requests for this event don't need confirmation");
        }

        int confirmedRequests = commonRequestService.findConfirmedRequests(List.of(event)).size();

        List<ParticipationRequest> requestsToUpdate = requestRepository.findAllByIdIn(request.getRequestIds());
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> requestDtos;
        switch (request.getStatus()) {
            case REJECTED:
                for (ParticipationRequest r : requestsToUpdate) {

                    if (!r.getStatus().equals(State.PENDING)) {
                        throw new ForbiddenException("Request must have status PENDING");
                    }

                    r.setStatus(State.CANCELED);
                }
                requestDtos = requestRepository.saveAll(requestsToUpdate)
                        .stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
                eventRequestStatusUpdateResult.setRejectedRequests(requestDtos);
                break;
            case CONFIRMED:
                for (ParticipationRequest r : requestsToUpdate) {

                    if (!r.getStatus().equals(State.PENDING)) {
                        throw new ForbiddenException("Request must have status PENDING");
                    }

                    if (confirmedRequests == event.getParticipantLimit()) {
                        throw new ForbiddenException("The participant limit has been reached");
                    }

                    r.setStatus(State.PUBLISHED);
                    confirmedRequests++;
                }
                requestDtos = requestRepository.saveAll(requestsToUpdate)
                        .stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
                eventRequestStatusUpdateResult.setConfirmedRequests(requestDtos);
                break;
        }
        return eventRequestStatusUpdateResult;
    }
}
