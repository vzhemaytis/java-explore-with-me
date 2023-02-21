package ru.ewm.stats.repository;

import dto.ViewStatsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query(nativeQuery = true, name = "ViewStatsDtos")
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, name = "ViewStatsDtosUniqueIps")
    List<ViewStatsDto> getStatsUniqueIps(LocalDateTime start, LocalDateTime end, List<String> uris);
}
