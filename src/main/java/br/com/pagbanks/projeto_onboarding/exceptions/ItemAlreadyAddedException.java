package br.com.pagbanks.projeto_onboarding.exceptions;

public class ItemAlreadyAddedException extends RuntimeException{

    private String message;

    public ItemAlreadyAddedException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
