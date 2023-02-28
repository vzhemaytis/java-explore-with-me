package ru.ewm.service.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.category.model.Category;
import ru.ewm.service.constants.EventState;
import ru.ewm.service.error.EntityNotFoundException;
import ru.ewm.service.error.ForbiddenException;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.dto.NewEventDto;
import ru.ewm.service.event.dto.UpdateEventRequest;
import ru.ewm.service.event.mapper.EventMapper;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.CommonEventService;
import ru.ewm.service.event.service.PrivateEventService;
import ru.ewm.service.user.model.User;
import ru.ewm.service.validation.EntityFoundValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.ewm.service.event.mapper.EventMapper.toEvent;
import static ru.ewm.service.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final EntityFoundValidation entityFoundValidation;
    private final CommonEventService commonEventService;

    @Transactional
    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        Event eventToSave = toEvent(newEventDto);
        Category category = entityFoundValidation.checkIfCategoryExist(newEventDto.getCategory());
        eventToSave.setCategory(category);
        User initiator = entityFoundValidation.checkIfUserExist(userId);
        eventToSave.setInitiator(initiator);
        eventToSave.setState(EventState.PENDING);
        eventToSave.setCreatedOn(LocalDateTime.now());
        return toEventFullDto(eventRepository.save(eventToSave));
    }

    @Transactional
    @Override
    public List<EventFullDto> getUserEvents(Long userId, long from, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        List<Event> foundEvents = eventRepository
                .findAllByIdIsGreaterThanEqualAndInitiatorIdIs(from, userId, pageRequest);
        return foundEvents.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event event = entityFoundValidation.checkIfEventExist(eventId);
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new EntityNotFoundException(eventId, Event.class.getSimpleName());
        }
        return toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventRequest updateEventUserRequest) {
        Event eventToUpdate = entityFoundValidation.checkIfEventExist(eventId);
        if (!Objects.equals(eventToUpdate.getInitiator().getId(), userId)) {
            throw new ForbiddenException("Event could be updated only by initiator");
        }
        Event updatedEvent = commonEventService.updateEvent(eventToUpdate, updateEventUserRequest);
        return toEventFullDto(eventRepository.save(updatedEvent));
    }
}
