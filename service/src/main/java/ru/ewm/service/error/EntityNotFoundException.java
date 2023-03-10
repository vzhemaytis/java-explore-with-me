package ru.ewm.service.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityNotFoundException extends RuntimeException {
    private final Long id;
    private final String className;
}
