package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Command;
import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.networking.server.RmiServerInterface;
import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.utils.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

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
    private RmiClientImplementation rmiClient;

    private Timer pingTimer;

    private static final int PING_TIMEOUT = 15000;

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
        pingTimer = new Timer("Ping-Timer");
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
            removeClient(rmiClient.getClient());
        }
    }

    /**
     * The method to add a client to the server.
     * <p>It creates the Skeleton
     * of the client, to interact with the Stub of the server, and passes the
     * remote reference of the client to the server.</p>
     * <p>It also starts a timer that pings the server to detect if the connection is down.</p>
     *
     * @param client        the client to connect.
     * @param isMultiPlayer {@code true} if the client is playing in multi player mode;
     *                      {@code false} if it's playing in single player mode.
     * @return {@code true} if the client had been added; {@code false} otherwise.
     */
    @Override
    public boolean addClient(ClientNetInterface client, boolean isMultiPlayer) {
        try {
            rmiClient = new RmiClientImplementation(client);
            remoteRef = (RmiClientInterface) UnicastRemoteObject.exportObject(
                    rmiClient, 0);
            boolean added = server.addClient(remoteRef, isMultiPlayer);
            if (added)
                pingTimer.schedule(new TimerTask() {
                    public void run() {
                        send(new Message(Command.PING, ""));
                    }
                }, PING_TIMEOUT, PING_TIMEOUT);
            return added;
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
            UnicastRemoteObject.unexportObject(rmiClient, true);
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log(ERROR_STRING + e.getMessage() + "!");
        }
        pingTimer.cancel();
        client.close();
    }

}
