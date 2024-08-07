package br.com.pagbanks.projeto_onboarding.exceptions;

public class CarrinhoNotFoundException extends RuntimeException {

    private String message;

    public CarrinhoNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

