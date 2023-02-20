package ru.ewm.stats.model;

import dto.ViewStatsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hits")
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(name = "ViewStatsDtos",
        query = "select h.app, h.uri, count(h.ip) as ehits from hits as h " +
        "where h.timestamp > ?1 " +
        "and h.timestamp < ?2 " +
        "and h.uri in ?3 " +
        "group by h.app, h.uri " +
        "order by ehits desc", resultSetMapping = "ViewStatsDtoMapping")
@NamedNativeQuery(name = "ViewStatsDtosUniqueIps",
        query = "select h.app, h.uri, count(distinct (h.ip)) as ehits from hits as h " +
        "where h.timestamp > ?1 " +
        "and h.timestamp < ?2 " +
        "and h.uri in ?3 " +
        "group by h.app, h.uri " +
        "order by ehits desc", resultSetMapping = "ViewStatsDtoMapping")
@SqlResultSetMapping(name = "ViewStatsDtoMapping",
        classes = {
                @ConstructorResult(
                        columns = {
                                @ColumnResult(name = "app", type = String.class),
                                @ColumnResult(name = "uri", type = String.class),
                                @ColumnResult(name = "ehits", type = Long.class)
                        },
                        targetClass = ViewStatsDto.class
                )}
)
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
