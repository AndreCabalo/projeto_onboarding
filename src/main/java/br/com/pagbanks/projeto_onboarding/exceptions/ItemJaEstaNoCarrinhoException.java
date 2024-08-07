package br.com.pagbanks.projeto_onboarding.exceptions;

public class ItemJaEstaNoCarrinhoException extends RuntimeException {

    private final String message;

    public ItemJaEstaNoCarrinhoException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
