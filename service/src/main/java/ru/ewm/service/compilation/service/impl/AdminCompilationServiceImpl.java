package ru.ewm.service.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.compilation.dto.CompilationDto;
import ru.ewm.service.compilation.dto.NewCompilationDto;
import ru.ewm.service.compilation.model.Compilation;
import ru.ewm.service.compilation.repository.CompilationRepository;
import ru.ewm.service.compilation.service.AdminCompilationService;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;

import java.util.List;

import static ru.ewm.service.compilation.mapper.CompilationMapper.toCompilation;
import static ru.ewm.service.compilation.mapper.CompilationMapper.toCompilationDto;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilationToSave = toCompilation(newCompilationDto);
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        compilationToSave.setEvents(events);
        return toCompilationDto(compilationRepository.save(compilationToSave));
    }
}
