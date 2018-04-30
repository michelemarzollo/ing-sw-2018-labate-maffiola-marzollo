package it.polimi.se2018.model;

public class DieValueException extends RuntimeException{ //eccezione unchecked
    public DieValueException(String msg){
        super(msg);
    }
}
