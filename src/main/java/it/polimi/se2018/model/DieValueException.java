package it.polimi.se2018.model;

/**
 * Unchecked exception thrown by the {@link Die} when building a Die with value
 * out of the 1-6 range.
 *
 * @author giorgiolbt
 */

public class DieValueException extends RuntimeException {
    public DieValueException(String msg) {
        super(msg);
    }
}
