package ru.ewm.service.event.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.EventState;
import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.error.EntityNotFoundException;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.mapper.EventMapper;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.PublicEventService;
import ru.ewm.service.validation.EntityFoundValidator;
import ru.ewm.stats.client.web.HitsClient;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.ewm.service.constants.Constants.APP_NAME;
import static ru.ewm.service.constants.Constants.DATE_TIME_FORMATTER;
import static ru.ewm.service.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final HitsClient hitsClient;
    private final EntityFoundValidator entityFoundValidator;

    @Transactional
    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        Event eventToReturn = entityFoundValidator.checkIfEventExist(id);
        if(!eventToReturn.getState().equals(EventState.PUBLISHED)) {
            throw new EntityNotFoundException(id, Event.class.getSimpleName());
        }
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String start = eventToReturn.getPublishedOn().format(DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        long views;
        try {
            views = getViews(start, end, uri, false);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        EventFullDto eventFullDto = toEventFullDto(eventToReturn);
        eventFullDto.setViews(views);
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
                                                int size) {
        List<Event> found = eventRepository.publicEventSearch(text, categories, paid, rangeStart, rangeEnd, from, size);
        return found.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }



    private Long getViews(String start, String end, String uri, boolean unique) throws JsonProcessingException {
        Long views = 0L;
        ResponseEntity<Object> response = hitsClient.getViewStats(start, end, List.of(uri), unique);

        if (!response.hasBody()) {
            return views;
        }

        List<ViewStatsDto> stats;
        ObjectMapper mapper = new ObjectMapper();
        stats = Arrays.asList(mapper.readValue(mapper.writeValueAsString(response.getBody()), ViewStatsDto[].class));
        Optional<Long> viewsOptional = stats.stream()
                .filter(l -> l.getUri().equals(uri)).map(ViewStatsDto::getHits).findFirst();
        if (viewsOptional.isEmpty()) {
            return views;
        }
        return viewsOptional.get();
    }
}
