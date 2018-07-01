package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Message;

/**
 * Dummy ClientNetInterface only used in tests to isolate the behaviour under test.
 */
public class DummyClient implements ClientNetInterface {

    /**
     * The username.
     */
    private final String username;

    /**
     * Creates a new instance with the provided username.
     * @param username The username.
     */
    public DummyClient(String username) {
        this.username = username;
    }

    /**
     * Getter for the username.
     * @return The username.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Disabled.
     * @param message the message that the client receives.
     */
    @Override
    public void notify(Message message) {
        //Do nothing
    }

    @Override
    public void close() {

    }
}
