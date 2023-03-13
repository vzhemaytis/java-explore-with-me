package ru.ewm.service.event.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.event.dto.NewEventDto;
import ru.ewm.service.event.dto.UpdateEventUserRequest;
import ru.ewm.service.event.service.PrivateEventService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @PostMapping
    public ResponseEntity<Object> addEvent(@PathVariable Long userId,
                                           @RequestBody @NotNull @Valid NewEventDto newEventDto) {
        log.info("add new event = {} by user with id = {}", newEventDto, userId);
        return new ResponseEntity<>(privateEventService.addEvent(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getUserEvents(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero long from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        log.info("get all event of user with id - {} from = {} page size = {}", userId, from, size);
        return new ResponseEntity<>(privateEventService.getUserEvents(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getUserEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("get event with id = {} by user with id = {}", eventId, userId);
        return new ResponseEntity<>(privateEventService.getUserEvent(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @PathVariable Long userId, @PathVariable Long eventId,
            @RequestBody @NotNull @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.info("update event with id = {} by user with id = {}", eventId, userId);
        return new ResponseEntity<>(privateEventService
                .updateEvent(userId, eventId, updateEventUserRequest), HttpStatus.OK);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<Object> getAllFollowedUsersEvents(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero long from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size,
            @RequestParam(name = "sort", required = false) SortTypes sort) {
        log.info("get all followed users events by user with id = {}", userId);
        return new ResponseEntity<>(privateEventService
                .getAllFollowedUsersEvents(userId, from, size, sort), HttpStatus.OK);
    }
}
