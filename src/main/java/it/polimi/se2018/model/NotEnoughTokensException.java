package it.polimi.se2018.model;

/**
 * Exception thrown by {@code consumeTokens}
 * in {@link Player} when the number of tokens
 * to be used are more than the actual number
 * of tokens owned by the {@link Player}.
 */
public class NotEnoughTokensException extends Exception {
    public NotEnoughTokensException(String msg) {
        super(msg);
    }
}
