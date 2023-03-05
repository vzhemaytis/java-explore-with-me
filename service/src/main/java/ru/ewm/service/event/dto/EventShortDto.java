package ru.ewm.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ewm.service.category.dto.CategoryDto;
import ru.ewm.service.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.ewm.service.constants.Constants.DATE_TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private long views;

    @Override
    public String toString() {
        return "EventShortDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
