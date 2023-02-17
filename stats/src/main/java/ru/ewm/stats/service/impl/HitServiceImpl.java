package ru.ewm.stats.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.stats.dto.EndpointHitDto;
import ru.ewm.stats.dto.ViewStatsDto;
import ru.ewm.stats.model.EndpointHit;
import ru.ewm.stats.repository.EndpointHitRepository;
import ru.ewm.stats.service.HitService;

import java.time.Instant;
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
        EndpointHit hit = toEndpointHit(hitDto);
        return toEndpointHitDto(repository.save(hit));
    }

    @Override
    public List<ViewStatsDto> getStats(Instant start, Instant end, List<String> uris, Boolean unique) {
        List<EndpointHit> hits = repository.getHits(start, end, uris);

        return null;
    }
}
