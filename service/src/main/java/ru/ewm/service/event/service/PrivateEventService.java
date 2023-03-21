package ru.ewm.service.event.service;

import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.dto.NewEventDto;
import ru.ewm.service.event.dto.UpdateEventRequest;

import java.util.List;

public interface PrivateEventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    List<EventFullDto> getUserEvents(Long userId, long from, int size);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventUserRequest);

    List<EventFullDto> getAllFollowedUsersEvents(Long userId, long from, int size, SortTypes sort);
}
