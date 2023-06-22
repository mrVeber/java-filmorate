package ru.yandex.practicum.filmorate.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBody handleValidationException(final ValidationException e) {
        log.warn("Invalid input: " + e.getMessage());
        return new ResponseBody(ValidationException.class.getName(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseBody handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseBody(ObjectNotFoundException.class.getName(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBody handleObjectNotFoundException(final Exception e) {
        log.warn(e.getMessage());
        return new ResponseBody(e.getClass().getName(), e.getMessage());
    }
}
