package ru.ewm.service.event.service;

import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.event.dto.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    EventFullDto getEvent(Long id, HttpServletRequest request);

    List<EventFullDto> publicEventSearch(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Boolean onlyAvailable,
                                         SortTypes sort,
                                         long from,
                                         int size,
                                         String ip);
}
