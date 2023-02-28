package ru.ewm.service.event.repository;

import ru.ewm.service.constants.EventState;
import ru.ewm.service.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {

    List<Event> findEvents(List<Long> users,
                           List<EventState> states,
                           List<Long> categories,
                           LocalDateTime rangeStart,
                           LocalDateTime rangeEnd,
                           long from,
                           int size);
}
