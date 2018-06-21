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
     * @param client        the client to add.
     * @param isMultiPlayer {@code true} if the client wants to play in multi player mode;
     *                      {@code false} if it wants ti play in single player mode.
     * @return {@code true} if the client has been added; {@code false} otherwise.
     */
    @Override
    public boolean addClient(RmiClientInterface client, boolean isMultiPlayer) {
        VirtualRmiClient virtualClient = new VirtualRmiClient(server, client);
        return server.addClient(virtualClient, isMultiPlayer);
    }

    /**
     * The method to remove a client form the server.
     *
     * @param client the client to remove.
     */
    @Override
    public void removeClient(RmiClientInterface client) {
        VirtualRmiClient virtualClient = new VirtualRmiClient(server, client);
        server.removeClient(virtualClient);
    }

    /**
     * The method to send a message to the server.
     *
     * @param message the message to be received by the server.
     */
    @Override
    public void send(Message message) {
        server.send(message);
    }

    /**
     * The method that overrides the method of {@link Object}, to see
     * if two objects are equals.
     *
     * @param o the object to compare.
     * @return {@code true}, if they are the same object, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    /**
     * The override of hashCode in Object.
     *
     * @return the hashCode.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
