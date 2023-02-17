package ru.ewm.stats.service;

import ru.ewm.stats.dto.EndpointHitDto;
import ru.ewm.stats.dto.ViewStatsDto;

import java.time.Instant;
import java.util.List;

public interface HitService {
    EndpointHitDto saveNewHit(EndpointHitDto hitDto);
    List<ViewStatsDto> getStats(Instant start,
                                Instant end,
                                List<String> uris,
                                Boolean unique);
}
