package br.com.pagbanks.projeto_onboarding.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ItemAlreadyAddedException.class)
    public ResponseEntity<String> handleItemAlreadyAddedException(ItemAlreadyAddedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DataErroValid>> handleValidException(MethodArgumentNotValidException e){
        var errors = e.getFieldErrors();
        List<DataErroValid> erroList = errors.stream().map(DataErroValid::new).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroList);
    }

    private record DataErroValid(String field, String message){
        public DataErroValid(FieldError errors){
            this(errors.getField(),errors.getDefaultMessage());
        }
    }
}
