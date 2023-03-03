package ru.ewm.stats.web;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.stats.service.HitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;
    private final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    @PostMapping("/hit")
    public ResponseEntity<Object> saveNewHit(@RequestBody @Valid EndpointHitDto hitDto) {
        log.info("save new endpoint hit = {}", hitDto);
        EndpointHitDto savedHit = hitService.saveNewHit(hitDto);
        return new ResponseEntity<>(savedHit, HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "start")
                                           @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                           @RequestParam(name = "end")
                                           @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                           @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                           @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        if (start.isAfter(LocalDateTime.now())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("get stats from = {} till = {} about uris = {} with unique ids = {}",
                start, end, uris, unique);
        List<ViewStatsDto> stats = hitService.getStats(start, end, uris, unique);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
