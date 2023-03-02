package ru.ewm.service.event.service;

import ru.ewm.service.constants.EventState;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.dto.UpdateEventRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> adminEventSearch(List<Long> users,
                                        List<EventState> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        long from,
                                        int size);

    EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEventAdminRequest);
}
