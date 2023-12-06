package pl.pseudoorganization.hotelerp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.pseudoorganization.hotelerp.error.ApplicationException;
import pl.pseudoorganization.hotelerp.error.ErrorCode;
import pl.pseudoorganization.hotelerp.model.chore.ChoreBusinessException;

@ControllerAdvice
public class AdviceController
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ApplicationException.class })
    protected ResponseEntity<Object> handleConflict(final ApplicationException ex) {
        return switch (ex.getErrorCode()) {
            case USER_ALREADY_EXISTS ->
                new ResponseEntity<>(ErrorCode.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
            case NOT_FOUND ->
                new ResponseEntity<>(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
            case INVALID_STATUS ->
                new ResponseEntity<>(ErrorCode.INVALID_STATUS, HttpStatus.BAD_REQUEST);
            case OTHER_ERROR ->
                new ResponseEntity<>(ErrorCode.OTHER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @ExceptionHandler(value = { ChoreBusinessException.class })
    protected ResponseEntity<Object> handleConflict(final ChoreBusinessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleConflict(final IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleConflict(final Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}