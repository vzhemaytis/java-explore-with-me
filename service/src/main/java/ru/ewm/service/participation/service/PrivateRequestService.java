package ru.ewm.service.participation.service;

import ru.ewm.service.participation.dto.EventRequestStatusUpdateRequest;
import ru.ewm.service.participation.dto.EventRequestStatusUpdateResult;
import ru.ewm.service.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> findAllUsersRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequests(Long userId,
                                                  Long eventId,
                                                  EventRequestStatusUpdateRequest request);
}
