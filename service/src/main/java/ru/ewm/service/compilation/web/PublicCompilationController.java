package ru.ewm.service.compilation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.compilation.service.PublicCompilationService;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PublicCompilationController {

    private final PublicCompilationService publicCompilationService;

    @GetMapping("/{compId}")
    public ResponseEntity<Object> getCompilation(@PathVariable(name = "compId") Long compId) {
        log.info("get compilation with id = {}", compId);
        return new ResponseEntity<>(publicCompilationService.getCompilation(compId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findCompilations(@RequestParam(name = "pinned", required = false) boolean pinned,
                                                   @RequestParam(name = "from", defaultValue = "0") long from,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("find all pinned = {} compilation from = {} page size = {}", pinned, from, size);
        return new ResponseEntity<>(publicCompilationService.findCompilations(pinned, from, size), HttpStatus.OK);
    }
}
