package ru.ewm.service.compilation.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ewm.service.compilation.dto.NewCompilationDto;
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
}
