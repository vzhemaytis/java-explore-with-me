package ru.ewm.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ewm.service.category.dto.CategoryDto;
import ru.ewm.service.constants.State;
import ru.ewm.service.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.ewm.service.constants.Constants.DATE_TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;

    //TODO add location

    private Boolean paid;
    private int participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private State state;
    private String title;
    private Long views;

    @Override
    public String toString() {
        return "EventFullDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
