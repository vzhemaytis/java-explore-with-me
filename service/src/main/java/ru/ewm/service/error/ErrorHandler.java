package ru.ewm.service.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        Optional<FieldError> optionalFieldError = e.getBindingResult().getFieldErrors().stream().findFirst();
        String message = "";
        if (optionalFieldError.isPresent()) {
            FieldError fieldError = optionalFieldError.get();
            String fieldName = fieldError.getField();
            String value = Objects.requireNonNull(fieldError.getRejectedValue()).toString();
            String error = fieldError.getDefaultMessage();
            message = String.format("Field: %s. Error: %s. Value: %s", fieldName, error, value);
        }
        log.warn(message);

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Incorrectly made request.");
        apiError.setMessage(message);
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(final DataIntegrityViolationException e) {
        String message = e.getMessage();
        log.warn(message);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.CONFLICT.name());
        apiError.setReason("Integrity constraint has been violated.");
        apiError.setMessage(message);
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEmptyResultDataAccessException(final EntityNotFoundException e) {
        String message = String.format("%s with id=%s was not found", e.getClassName(), e.getId());
        log.warn(message);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND.name());
        apiError.setReason("The required object was not found.");
        apiError.setMessage(message);
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        String message = e.getMessage();
        log.warn(message);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Incorrectly made request.");
        apiError.setMessage(message);
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        String message = e.getMessage();
        log.warn(message);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.FORBIDDEN.name());
        apiError.setReason("For the requested operation the conditions are not met.");
        apiError.setMessage(message);
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        String message = e.getMessage();
        log.warn(message);
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.name());
        apiError.setReason("Incorrectly made request.");
        apiError.setMessage(message);
        apiError.setTimestamp(LocalDateTime.now());
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.warn(e.getMessage());
        return null;
    }
}
