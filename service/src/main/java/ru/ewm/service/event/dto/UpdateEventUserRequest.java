package ru.ewm.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.ewm.service.constants.Constants.DATE_TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "must be more then 20 less then 2000")
    private String annotation;
    @Positive(message = "must be positive")
    private Long category;
    @Size(min = 20, max = 7000, message = "must be more then 20 less then 7000")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    //TODO create Location

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120, message = "must be more then 3 less then 120")
    private String title;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "annotation='" + annotation + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
