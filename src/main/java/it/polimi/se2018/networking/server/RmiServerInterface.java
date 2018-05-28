package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.RmiClientInterface;
import it.polimi.se2018.networking.messages.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface for the RMI communication.
 *
 * @author michelemarzollo
 */
public interface RmiServerInterface extends Remote {

    /**
     * The method to add a client to the server.
     *
     * @param client the client to add.
     * @throws RemoteException if there were problems of communication during
     *                         a remote method call.
     */
    void addClient(RmiClientInterface client) throws RemoteException;

    /**
     * The method to remove a client from the server.
     *
     * @param client the client to remove.
     * @throws RemoteException if there were problems of communication during
     *                         a remote method call.
     */
    void removeClient(RmiClientInterface client) throws RemoteException;

    /**
     * The method to send a message to the server.
     *
     * @param message the message to be received by the server.
     * @throws RemoteException if there were problems of communication during
     *                         the remote method call.
     */
    void send(Message message) throws RemoteException;

}