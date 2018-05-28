package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiClientInterface extends Remote {

    /**
     * The method to notify the client.
     *
     * @param message the message that the client should receive.
     * @throws RemoteException if there were problems of communication during
     *                         the remote method call.
     */
    void notify(Message message) throws RemoteException;

}
