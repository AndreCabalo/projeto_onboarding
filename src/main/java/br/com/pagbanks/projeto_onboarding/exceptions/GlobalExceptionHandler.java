package br.com.pagbanks.projeto_onboarding.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    private ResponseEntity<List<DataErrorValid>> handleValidException(MethodArgumentNotValidException e){
        var errors = e.getFieldErrors();
        List<DataErrorValid> erroList = errors.stream().map(DataErrorValid::new).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroList);
    }

    private record DataErrorValid(String field, String message){
        public DataErrorValid(FieldError errors){
            this(errors.getField(),errors.getDefaultMessage());
        }
    }
}
