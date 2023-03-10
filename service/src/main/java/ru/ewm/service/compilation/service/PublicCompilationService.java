package ru.ewm.service.compilation.service;

import ru.ewm.service.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    CompilationDto getCompilation(Long compId);

    List<CompilationDto> findCompilations(boolean pinned, long from, int size);
}
