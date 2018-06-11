package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Message;

import java.rmi.RemoteException;

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
    private ClientNetInterface client;

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
    public String getUsername() throws RemoteException{
        return client.getUsername();
    }

    /**
     * The method to notify the client with a message.
     *
     * @param message the message that the client should receive.
     * @throws RemoteException if there were problems of communication during
     *                         the remote method call.
     */
    @Override
    public void notify(Message message) throws RemoteException {
        client.notify(message);
    }

}
