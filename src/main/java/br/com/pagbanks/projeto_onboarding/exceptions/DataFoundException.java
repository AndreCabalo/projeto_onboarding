package br.com.pagbanks.projeto_onboarding.exceptions;

public class DataFoundException extends RuntimeException {

    private String mensagem;

    public DataFoundException(String message) {
        this.mensagem = message;
    }
}
