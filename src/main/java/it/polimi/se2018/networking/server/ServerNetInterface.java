package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Message;

/**
 * The interface that contains the common features of the different
 * servers. It is used to separate the use of the server from it's implementation.
 */
public interface ServerNetInterface {

    /**
     * The method to send a message to the server.
     *
     * @param message the message to send.
     */
    void send(Message message);

    /**
     * The method to add a client to the server.
     *
     * @param client        the client to add.
     * @param isMultiPlayer {@code true} if the client wants to play in multi player mode;
     *                      {@code false} if it wants ti play in single player mode.
     * @return {@code true} if the client has been added; {@code false} otherwise.
     */
    boolean addClient(ClientNetInterface client, boolean isMultiPlayer);

    /**
     * The method to remove a client from the server.
     *
     * @param client the client to remove.
     */
    void removeClient(ClientNetInterface client);

}
