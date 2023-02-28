package ru.ewm.service.event.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ewm.service.constants.EventState;
import ru.ewm.service.event.service.AdminEventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<Object> findEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero long from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size
            ) {
        log.info("find event with: users = {}, states = {}, categories = {}, rangeStart = {}, rangeEnd = {}",
                users, states, categories, rangeStart, rangeEnd);
        return new ResponseEntity<>(adminEventService
                .findEvents(users, states, categories, rangeStart, rangeEnd, from, size), HttpStatus.OK);
    }
}
