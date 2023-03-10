package ru.ewm.service.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.State;
import ru.ewm.service.error.ForbiddenException;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.dto.UpdateEventRequest;
import ru.ewm.service.event.mapper.EventMapper;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.AdminEventService;
import ru.ewm.service.event.service.CommonEventService;
import ru.ewm.service.validation.EntityFoundValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.ewm.service.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final EntityFoundValidator entityFoundValidator;
    private final CommonEventService commonEventService;

    @Override
    public List<EventFullDto> adminEventSearch(List<Long> users,
                                               List<State> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               long from,
                                               int size) {
        List<Event> foundEvents = eventRepository.adminEventSearch(
                users, states, categories, rangeStart, rangeEnd, from, size);

        List<EventFullDto> eventFullDtos = foundEvents.stream()
                .map(EventMapper::toEventFullDto).collect(Collectors.toList());

        Map<Long, Long> views = commonEventService.getStats(foundEvents, false);
        if (!views.isEmpty()) {
            eventFullDtos.forEach(e -> e.setViews(views.get(e.getId())));
        }
        return eventFullDtos;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEventAdminRequest) {
        Event eventToUpdate = entityFoundValidator.checkIfEventExist(eventId);
        if (eventToUpdate.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ForbiddenException("Event date should not earlier then in 1 hours from now");
        }
        if (!eventToUpdate.getState().equals(State.PENDING)) {
            throw new ForbiddenException(
                    String.format("Cannot publish the event because it's not in the right state: %s",
                            eventToUpdate.getState().name()));
        }
        Event updatedEvent = commonEventService.updateEvent(eventToUpdate, updateEventAdminRequest);
        switch (updateEventAdminRequest.getStateAction()) {
            case PUBLISH_EVENT:
                updatedEvent.setPublishedOn(LocalDateTime.now());
                updatedEvent.setState(State.PUBLISHED);
                break;
            case REJECT_EVENT:
                updatedEvent.setState(State.CANCELED);
                break;
        }
        return toEventFullDto(eventRepository.save(updatedEvent));
    }
}
