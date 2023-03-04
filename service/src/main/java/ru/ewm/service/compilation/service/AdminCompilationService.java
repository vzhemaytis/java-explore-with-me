package ru.ewm.service.compilation.service;

import ru.ewm.service.compilation.dto.CompilationDto;
import ru.ewm.service.compilation.dto.NewCompilationDto;

public interface AdminCompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);
}
