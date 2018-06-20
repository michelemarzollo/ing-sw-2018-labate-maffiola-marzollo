package it.polimi.se2018.utils;

/**
 * The exception thrown when some method tries to get the reference to the
 * instance of a {@link ClientConfiguration} or {@link ServerConfiguration}, but the
 * class wasn't instantiated.
 *
 * @author michelemarzollo
 */
public class MissingConfigurationException extends Exception {
    /**
     * The constructor.
     *
     * @param msg the message of the exception.
     */
    MissingConfigurationException(String msg) {
        super(msg);
    }
}
