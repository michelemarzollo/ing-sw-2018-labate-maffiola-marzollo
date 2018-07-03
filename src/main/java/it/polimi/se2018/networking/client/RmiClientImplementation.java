package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Command;
import it.polimi.se2018.networking.messages.Message;

/**
 * The implementation of the remote interface RmiClientInterface.
 *
 * @author michelemarzollo
 */
public class RmiClientImplementation implements RmiClientInterface {

    /**
     * The interface that contains the methods for the clients,
     * independently from the type of their implementation.
     */
    private final ClientNetInterface client;

    /**
     * The constructor of the class.
     *
     * @param client the interface for the client.
     */
    public RmiClientImplementation(ClientNetInterface client) {
        this.client = client;
    }

    /**
     * The getter for the username.
     *
     * @return {@code client}.
     */
    public String getUsername() {
        return client.getUsername();
    }

    /**
     * The method to notify the client with a message.
     *
     * @param message the message that the client should receive.
     */
    @Override
    public void notify(Message message) {
        if (message.getCommand() != Command.ACK)
            client.notify(message);
    }

    /**
     * Getter for the underlying client net interface.
     *
     * @return The underlying client net interface.
     */
    public ClientNetInterface getClient() {
        return client;
    }

}
