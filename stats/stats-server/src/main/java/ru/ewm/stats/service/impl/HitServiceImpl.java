package ru.ewm.stats.service.impl;

import dto.EndpointHitDto;
import dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.stats.model.EndpointHit;
import ru.ewm.stats.repository.EndpointHitRepository;
import ru.ewm.stats.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.ewm.stats.mapper.EndpointHitMapper.toEndpointHit;
import static ru.ewm.stats.mapper.EndpointHitMapper.toEndpointHitDto;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final EndpointHitRepository repository;

    @Transactional
    @Override
    public EndpointHitDto saveNewHit(EndpointHitDto hitDto) {
        EndpointHit savedHit = repository.save(toEndpointHit(hitDto));

        return toEndpointHitDto(savedHit);
    }

    @Transactional
    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return repository.getStatsUniqueIps(start, end, uris);
        }
        return repository.getStats(start, end, uris);
    }
}
