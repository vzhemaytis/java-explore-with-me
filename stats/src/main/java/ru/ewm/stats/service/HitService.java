package ru.ewm.stats.service;

import ru.ewm.stats.dto.EndpointHitDto;
import ru.ewm.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    EndpointHitDto saveNewHit(EndpointHitDto hitDto);

    List<ViewStatsDto> getStats(LocalDateTime start,
                                LocalDateTime end,
                                List<String> uris,
                                Boolean unique);
}
