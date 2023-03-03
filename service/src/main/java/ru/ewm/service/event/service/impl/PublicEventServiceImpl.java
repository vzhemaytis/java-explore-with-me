package ru.ewm.service.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.EventState;
import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.error.EntityNotFoundException;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.mapper.EventMapper;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.CommonEventService;
import ru.ewm.service.event.service.PublicEventService;
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

    @Transactional
    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event eventToReturn = entityFoundValidator.checkIfEventExist(id);
        if(!eventToReturn.getState().equals(EventState.PUBLISHED)) {
            throw new EntityNotFoundException(id, Event.class.getSimpleName());
        }
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        Map<Long, Long> views = commonEventService.getStats(List.of(eventToReturn), false);

        EventFullDto eventFullDto = toEventFullDto(eventToReturn);
        eventFullDto.setViews(views.get(eventToReturn.getId()));
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

        List<Event> found = eventRepository.publicEventSearch(text, categories, paid, rangeStart, rangeEnd, from, size);
        if (found.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> views = commonEventService.getStats(found, false);

        List<EventFullDto> eventFullDtos = found.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
        eventFullDtos.forEach(e -> e.setViews(views.get(e.getId())));

        LocalDateTime timestamp = LocalDateTime.now();
        found.forEach(event -> hitsClient.saveNewHit(APP_NAME, "/events/" + event.getId(), ip, timestamp));

        return eventFullDtos;
    }
}
