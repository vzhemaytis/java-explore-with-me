package ru.ewm.service.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.compilation.dto.CompilationDto;
import ru.ewm.service.compilation.mapper.CompilationMapper;
import ru.ewm.service.compilation.model.Compilation;
import ru.ewm.service.compilation.repository.CompilationRepository;
import ru.ewm.service.compilation.service.PublicCompilationService;
import ru.ewm.service.event.dto.EventShortDto;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.event.repository.EventRepository;
import ru.ewm.service.event.service.CommonEventService;
import ru.ewm.service.participation.model.ParticipationRequest;
import ru.ewm.service.participation.service.CommonRequestService;
import ru.ewm.service.validation.EntityFoundValidator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.ewm.service.compilation.mapper.CompilationMapper.toCompilationDto;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;
    private final EntityFoundValidator entityFoundValidator;
    private final CommonEventService commonEventService;
    private final CommonRequestService commonRequestService;

    @Transactional
    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation foundCompilation = entityFoundValidator.checkIfCompilationExist(compId);
        CompilationDto compilationDto = toCompilationDto(foundCompilation);

        Map<Long, Long> views = commonEventService.getStats(foundCompilation.getEvents(), false);
        if (!views.isEmpty()) {
            setViewsToDto(views, compilationDto);
        }

        List<ParticipationRequest> confirmedRequests = commonRequestService
                .findConfirmedRequests(foundCompilation.getEvents());
        for (EventShortDto shortDto : compilationDto.getEvents()) {
            shortDto.setConfirmedRequests((int) confirmedRequests.stream()
                    .filter(request -> request.getEvent().getId().equals(shortDto.getId()))
                    .count());
        }
        return compilationDto;
    }

    @Transactional
    @Override
    public List<CompilationDto> findCompilations(boolean pinned, long from, int size) {
        PageRequest pageRequest = PageRequest.of(0, size);

        List<Compilation> foundCompilations = compilationRepository
                .findAllByIdIsGreaterThanEqualAndPinnedIs(from, pinned, pageRequest);

        List<CompilationDto> compilationDtos = foundCompilations.stream()
                .map(CompilationMapper::toCompilationDto).collect(Collectors.toList());

        Set<Event> events = foundCompilations.stream()
                .map(Compilation::getEvents)
                .flatMap(List<Event>::stream)
                .collect(Collectors.toSet());

        Map<Long, Long> views = commonEventService.getStats(List.copyOf(events), false);
        if (!views.isEmpty()) {
            compilationDtos.forEach(compilationDto -> setViewsToDto(views, compilationDto));
        }

        List<ParticipationRequest> confirmedRequests = commonRequestService
                .findConfirmedRequests(List.copyOf(events));

        compilationDtos
                .forEach(compilationDto -> setConfirmedRequestsToDto(compilationDto.getEvents(), confirmedRequests));

        return compilationDtos;
    }

    private void setViewsToDto(Map<Long, Long> views, CompilationDto compilationDto) {
        compilationDto.getEvents().forEach(e -> e.setViews(views.get(e.getId())));
    }

    private void setConfirmedRequestsToDto(List<EventShortDto> events, List<ParticipationRequest> confirmedRequests) {
        for (EventShortDto shortDto : events) {
            shortDto.setConfirmedRequests((int) confirmedRequests.stream()
                    .filter(request -> request.getEvent().getId().equals(shortDto.getId()))
                    .count());
        }
    }
}
