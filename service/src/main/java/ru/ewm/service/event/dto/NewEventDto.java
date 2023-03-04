package ru.ewm.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import ru.ewm.service.event.model.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.ewm.service.constants.Constants.DATE_TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "must not be blank")
    @Size(min = 20, max = 2000, message = "must be more then 20 less then 2000")
    private String annotation;
    @NotNull(message = "must not be null")
    @Positive(message = "must be positive")
    private Long category;
    @NotBlank(message = "must not be blank")
    @Size(min = 20, max = 7000, message = "must be more then 20 less then 7000")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    @Future(message = "должно содержать дату, которая еще не наступила")
    private LocalDateTime eventDate;
    @NotNull(message = "must not be null")
    private Location location;
    private boolean paid;
    private int participantLimit;
    @Value("true")
    private boolean requestModeration;
    @NotBlank(message = "must not be blank")
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
