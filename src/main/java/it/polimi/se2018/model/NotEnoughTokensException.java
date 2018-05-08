package it.polimi.se2018.model;

public class NotEnoughTokensException extends Exception {
    public NotEnoughTokensException(String msg) {
        super(msg);
    }
}
