package ru.ewm.service.event.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.constants.SortTypes;
import ru.ewm.service.event.dto.EventFullDto;
import ru.ewm.service.event.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.ewm.service.constants.Constants.DATE_TIME_PATTERN;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping("{id}")
    public ResponseEntity<Object> getEvent(@PathVariable(name = "id") @Positive Long id,
                                           HttpServletRequest request) {
        log.info("get event with id = {}", id);
        return new ResponseEntity<>(publicEventService.getEvent(id, request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> search(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false)
            @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false)
            @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) SortTypes sort,
            @RequestParam(name = "from", defaultValue = "0")@PositiveOrZero long from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        log.info("search with parameters:" +
                " text={} categories={} paid={} rangeStart={} rangeEnd{} onlyAvailable={} sort={} from={} pageSize={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        List<EventFullDto> found = publicEventService
                .publicEventSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, ip);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }
}
