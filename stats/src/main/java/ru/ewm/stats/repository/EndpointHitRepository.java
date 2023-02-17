package ru.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ewm.stats.model.EndpointHit;

import java.time.Instant;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query(" select h from EndpointHit h " +
            "where h.timestamp > ?1 " +
            "and h.timestamp < ?2 " +
            "and h.uri in ?3" +
            "order by h.id desc")
    List<EndpointHit> getHits(Instant start, Instant end, List<String> uris);
}
