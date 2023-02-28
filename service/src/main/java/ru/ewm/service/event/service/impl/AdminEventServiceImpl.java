package ru.ewm.service.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.constants.EventState;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.dto.UpdateEventRequest;
import ru.ewm.service.event.mapper.EventMapper;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.AdminEventService;
import ru.ewm.service.event.service.CommonEventService;
import ru.ewm.service.validation.EntityFoundValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ewm.service.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final EntityFoundValidation entityFoundValidation;
    private final CommonEventService commonEventService;

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

    @Transactional
    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventRequest updateEventAdminRequest) {
        Event eventToUpdate = entityFoundValidation.checkIfEventExist(eventId);
        Event updatedEvent = commonEventService.updateEvent(eventToUpdate, updateEventAdminRequest);
        switch (updateEventAdminRequest.getStateAction()) {
            case PUBLISH_EVENT:
                updatedEvent.setPublishedOn(LocalDateTime.now());
                updatedEvent.setState(EventState.PUBLISHED);
                break;
            case REJECT_EVENT:
                updatedEvent.setState(EventState.CANCELED);
                break;
        }
        return toEventFullDto(eventRepository.save(updatedEvent));
    }
}
