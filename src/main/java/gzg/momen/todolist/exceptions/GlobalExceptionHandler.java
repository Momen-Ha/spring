package gzg.momen.todolist.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorObject> handleTaskNotFoundException(TaskNotFoundException e, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorObject> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNameNotFoundException(UsernameNotFoundException e, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<>(errorObject, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setTimestamp(new Date());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        errorObject.setMessage(String.join(", ", errors));

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorObject> handleSecurityException(SecurityException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.FORBIDDEN);
    }


}
