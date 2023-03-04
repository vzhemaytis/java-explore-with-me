package ru.ewm.service.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.State;
import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.error.EntityNotFoundException;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.mapper.EventMapper;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.CommonEventService;
import ru.ewm.service.event.service.PublicEventService;
import ru.ewm.service.participation.model.ParticipationRequest;
import ru.ewm.service.participation.service.CommonRequestService;
import ru.ewm.service.validation.EntityFoundValidator;
import ru.ewm.stats.client.web.HitsClient;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.ewm.service.constants.Constants.APP_NAME;
import static ru.ewm.service.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final HitsClient hitsClient;
    private final EntityFoundValidator entityFoundValidator;
    private final CommonEventService commonEventService;
    private final CommonRequestService commonRequestService;

    @Transactional
    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event eventToReturn = entityFoundValidator.checkIfEventExist(id);
        if (!eventToReturn.getState().equals(State.PUBLISHED)) {
            throw new EntityNotFoundException(id, Event.class.getSimpleName());
        }

        EventFullDto eventFullDto = toEventFullDto(eventToReturn);

        Map<Long, Long> views = commonEventService.getStats(List.of(eventToReturn), false);
        eventFullDto.setViews(views.get(eventToReturn.getId()));

        List<ParticipationRequest> confirmedRequests = commonRequestService
                .findConfirmedRequests(List.of(eventToReturn));
        eventFullDto.setConfirmedRequests(confirmedRequests.size());

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        hitsClient.saveNewHit(APP_NAME, uri, ip, LocalDateTime.now());
        return eventFullDto;
    }

    @Transactional
    @Override
    public List<EventFullDto> publicEventSearch(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Boolean onlyAvailable,
                                                SortTypes sort,
                                                long from,
                                                int size,
                                                String ip) {

        List<Event> foundEvents = eventRepository.publicEventSearch(text, categories, paid, rangeStart, rangeEnd, from, size);
        if (foundEvents.isEmpty()) {
            return List.of();
        }

        List<EventFullDto> eventFullDtos = foundEvents.stream().
                map(EventMapper::toEventFullDto).collect(Collectors.toList());

        Map<Long, Long> views = commonEventService.getStats(foundEvents, false);
        eventFullDtos.forEach(e -> e.setViews(views.get(e.getId())));

        List<ParticipationRequest> confirmedRequests = commonRequestService.findConfirmedRequests(foundEvents);
        for (EventFullDto fullDto : eventFullDtos) {
            fullDto.setConfirmedRequests((int) confirmedRequests.stream()
                    .filter(request -> request.getEvent().getId().equals(fullDto.getId()))
                    .count());
        }

        LocalDateTime timestamp = LocalDateTime.now();
        foundEvents.forEach(event -> hitsClient.saveNewHit(APP_NAME, "/events/" + event.getId(), ip, timestamp));

        if (sort != null && sort.equals(SortTypes.VIEWS)) {
            return eventFullDtos.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews)).collect(Collectors.toList());
        }
        return eventFullDtos.stream()
                .sorted(Comparator.comparing(EventFullDto::getEventDate)).collect(Collectors.toList());
    }
}
