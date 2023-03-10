package ru.ewm.service.event.service;

import ru.ewm.service.event.dto.UpdateEventRequest;
import ru.ewm.service.event.model.Event;

import java.util.List;
import java.util.Map;

public interface CommonEventService {
    Event updateEvent(Event eventToUpdate, UpdateEventRequest updateEventRequest);

    Map<Long, Long> getStats(List<Event> events,
                             Boolean unique);
}
