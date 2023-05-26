package be.dpa.bootiful.activities.padp.rest;


import be.dpa.bootiful.activities.dm.api.exception.ActivityNotFoundException;
import be.dpa.bootiful.activities.dm.api.exception.InvalidParticipantException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

/**
 * Custom global exception handler for all controllers.
 *
 * @author denis
 */
@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<String> handleExceptionInternal(String message, HttpStatus status) {
        if (StringUtils.isNotEmpty(message)) {
            log.info(message);
        }
        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return handleExceptionInternal(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParticipantException.class)
    public ResponseEntity<String> handleInvalidParticipantException(InvalidParticipantException e) {
        return handleExceptionInternal(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ActivityNotFoundException.class)
    public ResponseEntity<String> handleActivityNotFoundException(ActivityNotFoundException e) {
        return handleExceptionInternal(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
