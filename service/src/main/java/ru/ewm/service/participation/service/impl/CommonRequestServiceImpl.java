package ru.ewm.service.participation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ewm.service.constants.State;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.participation.model.ParticipationRequest;
import ru.ewm.service.participation.repository.RequestRepository;
import ru.ewm.service.participation.service.CommonRequestService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonRequestServiceImpl implements CommonRequestService {

    private final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequest> findConfirmedRequests(List<Event> events) {
        return requestRepository
                .findAllByEventInAndStatusIs(events, State.PUBLISHED);
    }
}
