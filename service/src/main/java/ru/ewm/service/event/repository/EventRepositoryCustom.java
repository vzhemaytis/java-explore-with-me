package ru.ewm.service.event.repository;

import ru.ewm.service.constants.EventState;
import ru.ewm.service.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {

    List<Event> adminEventSearch(List<Long> users,
                                 List<EventState> states,
                                 List<Long> categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 long from,
                                 int size);

    List<Event> publicEventSearch(String text,
                                  List<Long> categories,
                                  Boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  long from,
                                  int size);
}
