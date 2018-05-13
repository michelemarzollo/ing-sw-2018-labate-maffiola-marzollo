package it.polimi.se2018.model;

/**
 *@author giorgiolabate
 * Unchecked exception thrown by the {@link Die} when building a Die with value out of the 1-6 range.
 */

//NON HA SENSO CHE SIA RUNTIME!!!
public class DieValueException extends RuntimeException{ //eccezione unchecked
    public DieValueException(String msg){
        super(msg);
    }
}
