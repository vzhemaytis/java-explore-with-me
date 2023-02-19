package ru.ewm.stats.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.stats.dto.EndpointHitDto;
import ru.ewm.stats.dto.ViewStatsDto;
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
    private final String pattern = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

    @PostMapping("/hit")
    public ResponseEntity<Object> saveNewHit(@RequestBody @Valid EndpointHitDto hitDto) {
        log.info("save new endpoint hit = {}", hitDto);
        EndpointHitDto savedHit = hitService.saveNewHit(hitDto);
        return new ResponseEntity<>(savedHit, HttpStatus.CREATED);
    }

    @GetMapping("stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "start") String start,
                                           @RequestParam(name = "end") String end,
                                           @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                           @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, dateTimeFormatter);
        if (startDateTime.isAfter(LocalDateTime.now())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("get stats from = {} till = {} about uris = {}", startDateTime, endDateTime, uris);
        List<ViewStatsDto> stats = hitService.getStats(startDateTime, endDateTime, uris, unique);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
