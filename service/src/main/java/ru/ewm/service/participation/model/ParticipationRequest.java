package ru.ewm.service.participation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ewm.service.constants.State;
import ru.ewm.service.event.model.Event;
import ru.ewm.service.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "participation_requests")
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private State status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationRequest that = (ParticipationRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                "id=" + id +
                ", created=" + created +
                ", event=" + event +
                ", requester=" + requester +
                ", state=" + status +
                '}';
    }
}
