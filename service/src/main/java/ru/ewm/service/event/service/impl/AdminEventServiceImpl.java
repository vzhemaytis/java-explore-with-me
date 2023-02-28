package ru.ewm.service.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.EventState;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.mapper.EventMapper;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public List<EventFullDto> findEvents(List<Long> users,
                                         List<EventState> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         long from,
                                         int size) {
        List<Event> foundEvents = eventRepository.findEvents(
                users, states, categories, rangeStart, rangeEnd, from, size);
        return foundEvents.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }
}
