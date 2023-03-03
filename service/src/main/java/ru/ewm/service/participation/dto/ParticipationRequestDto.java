package ru.ewm.service.participation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ewm.service.constants.State;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private State status;

    @Override
    public String toString() {
        return "ParticipationRequestDto{" +
                "created=" + created +
                ", event=" + event +
                ", id=" + id +
                ", requester=" + requester +
                ", state=" + status +
                '}';
    }
}
