package ru.ewm.service.event.service;

import ru.ewm.service.event.dto.UpdateEventRequest;
import ru.ewm.service.event.model.Event;

public interface CommonEventService {
    Event updateEvent(Event eventToUpdate, UpdateEventRequest updateEventRequest);
}
