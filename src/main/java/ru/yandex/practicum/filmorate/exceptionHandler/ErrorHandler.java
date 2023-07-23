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
    public ResponseBody handlerValidationException(final ValidationException e) {
        log.warn("400 {}", e.getMessage());
        return new ResponseBody("Validation error 400", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseBody handlerNotFoundException(final ObjectNotFoundException e) {
        log.warn("404 {}", e.getMessage());
        return new ResponseBody("Object not found 404", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBody handlerInternalException(final InternalException e) {
        log.warn("500 {}", e.getMessage());
        return new ResponseBody("Internal error 500", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseBody handlerDataAccessException(final DataException e) {
        log.warn("404 {}", e.getMessage());
        return new ResponseBody("Data not found 404", e.getMessage());
    }
}
