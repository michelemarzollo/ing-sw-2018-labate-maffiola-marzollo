package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.RmiClientInterface;
import it.polimi.se2018.networking.messages.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The RMI server implementation, which extends {@link UnicastRemoteObject}, for
 * the RMI schema. It's instantiation is the remote object that offers the service
 * to the client.
 * <p>The class is an adapter for {@link ServerNetInterface}.</p>
 *
 * @author michelemarzollo
 */
public class RmiServerImplementation extends UnicastRemoteObject implements RmiServerInterface {

    /**
     * The sever interface where the RmiServerImplementation invokes its methods,
     * after adapting the parameters to call the methods.
     */
    private transient ServerNetInterface server;

    /**
     * The constructor of the class.
     *
     * @param server the sever interface where the RmiServerImplementation invokes its methods
     * @throws RemoteException if there were problems of communication during
     *                         a remote method call.
     */
    public RmiServerImplementation(ServerNetInterface server) throws RemoteException {
        super(0);
        this.server = server;
    }

    /**
     * The method to ad a client to the server.
     *
     * @param client the client to add.
     * @throws RemoteException if there were problems of communication during
     *                         the remote method call.
     */
    @Override
    public void addClient(RmiClientInterface client) throws RemoteException {
        VirtualRmiClient virtualClient = new VirtualRmiClient(server, client);
        server.addClient(virtualClient);
    }

    /**
     * The method to remove a client form the server.
     *
     * @param client the client to remove.
     * @throws RemoteException if there were problems of communication during
     *                         the remote method call.
     */
    @Override
    public void removeClient(RmiClientInterface client) throws RemoteException {
        VirtualRmiClient virtualClient = new VirtualRmiClient(server, client);
        server.removeClient(virtualClient);
    }

    /**
     * The method to send a message to the server.
     *
     * @param message the message to be received by the server.
     * @throws RemoteException if there were problems of communication during
     *                         the remote method call.
     */
    @Override
    public void send(Message message) throws RemoteException {
        server.send(message);
    }

}
