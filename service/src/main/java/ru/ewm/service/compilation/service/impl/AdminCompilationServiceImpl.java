package ru.ewm.service.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.compilation.dto.CompilationDto;
import ru.ewm.service.compilation.dto.NewCompilationDto;
import ru.ewm.service.compilation.dto.UpdateCompilationRequest;
import ru.ewm.service.compilation.model.Compilation;
import ru.ewm.service.compilation.repository.CompilationRepository;
import ru.ewm.service.compilation.service.AdminCompilationService;
import ru.ewm.service.event.dto.EventShortDto;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.CommonEventService;
import ru.ewm.service.participation.model.ParticipationRequest;
import ru.ewm.service.participation.service.CommonRequestService;
import ru.ewm.service.validation.EntityFoundValidator;

import java.util.List;
import java.util.Map;

import static ru.ewm.service.compilation.mapper.CompilationMapper.toCompilation;
import static ru.ewm.service.compilation.mapper.CompilationMapper.toCompilationDto;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CommonEventService commonEventService;
    private final CommonRequestService commonRequestService;
    private final EntityFoundValidator entityFoundValidator;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilationToSave = toCompilation(newCompilationDto);
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        compilationToSave.setEvents(events);
        CompilationDto compilationDto = toCompilationDto(compilationRepository.save(compilationToSave));

        Map<Long, Long> views = commonEventService.getStats(events, false);
        if (!views.isEmpty()) {
            compilationDto.getEvents().forEach(e -> e.setViews(views.get(e.getId())));
        }

        List<ParticipationRequest> confirmedRequests = commonRequestService.findConfirmedRequests(events);
        for (EventShortDto shortDto : compilationDto.getEvents()) {
            shortDto.setConfirmedRequests((int) confirmedRequests.stream()
                    .filter(request -> request.getEvent().getId().equals(shortDto.getId()))
                    .count());
        }

        return compilationDto;
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        entityFoundValidator.checkIfCompilationExist(compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateRequest) {
        Compilation compilationToUpdate = entityFoundValidator.checkIfCompilationExist(compId);

        if (updateRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateRequest.getEvents());
            compilationToUpdate.setEvents(events);
        }

        if (updateRequest.getPinned() != null) {
            compilationToUpdate.setPinned(updateRequest.getPinned());
        }

        if (updateRequest.getTitle() != null) {
            compilationToUpdate.setTitle(updateRequest.getTitle());
        }

        CompilationDto updatedCompilationDto = toCompilationDto(compilationRepository.save(compilationToUpdate));

        Map<Long, Long> views = commonEventService.getStats(compilationToUpdate.getEvents(), false);
        if (!views.isEmpty()) {
            updatedCompilationDto.getEvents().forEach(e -> e.setViews(views.get(e.getId())));
        }

        List<ParticipationRequest> confirmedRequests = commonRequestService
                .findConfirmedRequests(compilationToUpdate.getEvents());
        for (EventShortDto shortDto : updatedCompilationDto.getEvents()) {
            shortDto.setConfirmedRequests((int) confirmedRequests.stream()
                    .filter(request -> request.getEvent().getId().equals(shortDto.getId()))
                    .count());
        }

        return updatedCompilationDto;
    }
}
