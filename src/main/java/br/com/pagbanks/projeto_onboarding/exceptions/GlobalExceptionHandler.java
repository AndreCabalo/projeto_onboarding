package br.com.pagbanks.projeto_onboarding.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException(ResourceNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ItemAlreadyAddedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleItemAlreadyAddedException(ItemAlreadyAddedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidException(MethodArgumentNotValidException e){
        var erros = e.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(DataErroValid:: new).toList());
    }

    private record DataErroValid(String field, String message){
        public DataErroValid(FieldError erro){
            this(erro.getField(),erro.getDefaultMessage());
        }
    }
}
