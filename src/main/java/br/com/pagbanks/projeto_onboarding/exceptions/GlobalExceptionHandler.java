package br.com.pagbanks.projeto_onboarding.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handlerErro404(){

        return ResponseEntity.notFound().build();

    }
}
