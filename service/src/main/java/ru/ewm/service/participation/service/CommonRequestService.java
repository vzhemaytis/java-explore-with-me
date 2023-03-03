package ru.ewm.service.participation.service;

import ru.ewm.service.participation.model.ParticipationRequest;

import java.util.List;

public interface CommonRequestService {
    List<ParticipationRequest> findConfirmedRequests(List<Long> eventIds);
}
