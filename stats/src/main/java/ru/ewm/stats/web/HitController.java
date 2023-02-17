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
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveNewHit(@RequestBody @Valid EndpointHitDto hitDto) {
        log.info("save new endpoint hit = {}", hitDto);
        EndpointHitDto savedHit = hitService.saveNewHit(hitDto);
        return new ResponseEntity<>(savedHit, HttpStatus.CREATED);
    }

    @GetMapping("stats")
    public ResponseEntity<Object> getStats(@PastOrPresent @RequestParam(name = "start") Instant start,
                                           @PastOrPresent @RequestParam(name = "end") Instant end,
                                           @RequestParam(name = "uris", defaultValue = "[]") List<String> uris,
                                           @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        List<ViewStatsDto> stats = hitService.getStats(start, end, uris, unique);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
