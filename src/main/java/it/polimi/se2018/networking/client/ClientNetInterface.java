package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Message;


/**
 * The interface for all clients or virtual-clients of the package
 * 'networking', independently from the kind of implementation.
 *
 * @author michelemarzollo
 */
public interface ClientNetInterface {

    /**
     * The method that returns the username of the client (or virtual client).
     *
     * @return the username.
     */
    String getUsername();

    /**
     * The method to notify the client.
     *
     * @param message the message that the client receives.
     */
    void notify(Message message);
}
