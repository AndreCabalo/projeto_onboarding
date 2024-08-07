package br.com.pagbanks.projeto_onboarding.exceptions;

public class QuantidadeIndisponivelException extends RuntimeException {

    private final String message;

    public QuantidadeIndisponivelException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
