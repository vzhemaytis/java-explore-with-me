package ru.ewm.service.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ewm.service.category.model.Category;
import ru.ewm.service.constants.EventState;
import ru.ewm.service.error.ForbiddenException;
import ru.ewm.service.event.dto.UpdateEventRequest;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.validation.EntityFoundValidation;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommonEventServiceImpl implements ru.ewm.service.event.service.CommonEventService {

    private final EntityFoundValidation entityFoundValidation;

    @Override
    public Event updateEvent(Event eventToUpdate, UpdateEventRequest updateEventRequest) {
        if (eventToUpdate.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        if (updateEventRequest.getAnnotation() != null) {
            eventToUpdate.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = entityFoundValidation.checkIfCategoryExist(updateEventRequest.getCategory());
            eventToUpdate.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            eventToUpdate.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ForbiddenException("Event date should not earlier then in 2 hours from now");
            }
            eventToUpdate.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getPaid() != null) {
            eventToUpdate.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            eventToUpdate.setTitle(updateEventRequest.getTitle());
        }
        return eventToUpdate;
    }
}
