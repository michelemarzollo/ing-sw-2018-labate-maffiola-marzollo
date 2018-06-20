package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.networking.server.RmiServerInterface;
import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.utils.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The network handler: it simulates the sever on the client.
 * <p>
 * It behaves similarly to an adapter, to connect the {@link ClientNetInterface}
 * to the {@link ServerNetInterface}, when the connection is with RMI.</p>
 *
 * @author michelemarzollo
 */
public class RmiNetworkHandler implements ServerNetInterface {

    /**
     * The server interface to exchange message with.
     */
    private RmiServerInterface server;

    /**
     * The remote reference to the {@link RmiClientImplementation}.
     */
    private RmiClientInterface remoteRef;

    /**
     * The exported interface.
     */
    private RmiClientInterface rmiClientInterface;

    /**
     * The string to print in case of {@link RemoteException}.
     */
    private static final String ERROR_STRING = "Connection error: ";

    /**
     * The constructor of the class: it asks the Stub of the server and creates
     * the RMI client.
     *
     * @param address     the address.
     * @param serviceName the name of the service.
     */
    public RmiNetworkHandler(String address, String serviceName) {

        try {
            server = (RmiServerInterface) Naming.lookup("//" + address + "/" + serviceName);

        } catch (MalformedURLException e) {
            Logger.getDefaultLogger().log("URL not found!");
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log(ERROR_STRING + e.getMessage() + "!");
        } catch (NotBoundException e) {
            Logger.getDefaultLogger().log("The reference passed isn't connected to anything!");
        }
    }

    /**
     * The method to send a message to the server.
     *
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        try {
            server.send(message);
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log(ERROR_STRING + e.getMessage() + "!");
        }
    }

    /**
     * The method to add a client to the server. It creates the Skeleton
     * of the client, to interact with the Stub of the server, and passes the
     * remote reference of the client to the server.
     *
     * @param client the client to connect.
     */
    @Override
    public boolean addClient(ClientNetInterface client) {
        try {
            rmiClientInterface = new RmiClientImplementation(client);
            remoteRef = (RmiClientInterface) UnicastRemoteObject.exportObject(
                    rmiClientInterface, 0);
            return server.addClient(remoteRef);
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log(ERROR_STRING + e.getMessage() + "!");
        }
        return false;
    }

    /**
     * The method to remove a client form the server.
     *
     * @param client the client to remove.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        try {
            server.removeClient(remoteRef);
            UnicastRemoteObject.unexportObject(rmiClientInterface, true);
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log(ERROR_STRING + e.getMessage() + "!");
        }
    }
}
