package ru.ewm.service.compilation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.compilation.dto.NewCompilationDto;
import ru.ewm.service.compilation.dto.UpdateCompilationRequest;
import ru.ewm.service.compilation.service.AdminCompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    public ResponseEntity<Object> addCompilation(@RequestBody @NotNull @Valid NewCompilationDto newCompilationDto) {
        log.info("add compilation = {}", newCompilationDto);
        return new ResponseEntity<>(adminCompilationService.addCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable(name = "compId") Long compId) {
        log.info("delete compilation with id = {}", compId);
        adminCompilationService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable(name = "compId") Long compId,
                                                    @RequestBody @NotNull UpdateCompilationRequest updateRequest) {
        log.info("update compilation with id = {}", compId);
        return new ResponseEntity<>(adminCompilationService
                .updateCompilation(compId, updateRequest), HttpStatus.OK);
    }
}
