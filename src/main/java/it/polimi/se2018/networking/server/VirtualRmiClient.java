package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.client.RmiClientImplementation;
import it.polimi.se2018.networking.client.RmiClientInterface;
import it.polimi.se2018.networking.messages.Message;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

/**
 * The class that simulates the client on the server.
 *
 * @author michelemarzollo
 */
public class VirtualRmiClient implements ClientNetInterface {

    /**
     * The interface of the server.
     */
    private ServerNetInterface server;

    /**
     * The username of the client that the VirtualRmiClient represents.
     */
    private String username;

    /**
     * The interface of the real client.
     */
    private RmiClientInterface client;

    /**
     * The constructor of the class.
     *
     * @param server the interface of the server.
     * @param client the interface of the real client.
     */
    public VirtualRmiClient(ServerNetInterface server, RmiClientInterface client) {
        this.server = server;
        this.client = client;
        RmiClientImplementation clientImplementation = (RmiClientImplementation) client;
        this.username = clientImplementation.getClient().getUsername();
    }

    /**
     * The getter for {@code username}.
     *
     * @return {@code username}.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * The method to notify the client.
     *
     * @param message the message that the client receives.
     */
    @Override
    public void notify(Message message) {
        try {
            client.notify(message);
        } catch (ConnectException e) {
            server.removeClient(this);
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        }
    }
}
