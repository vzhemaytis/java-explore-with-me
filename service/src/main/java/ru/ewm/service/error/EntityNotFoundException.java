package ru.ewm.service.error;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final Long id;
    private final String className;

    public EntityNotFoundException(Long id, String className) {
        this.id = id;
        this.className = className;
    }
}
