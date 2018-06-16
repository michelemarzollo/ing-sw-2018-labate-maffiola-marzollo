package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.client.RmiClientInterface;
import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.utils.Logger;

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
    private final ServerNetInterface server;

    /**
     * The interface of the real client.
     */
    private final RmiClientInterface client;

    /**
     * The username of the player.
     */
    private final String username;

    /**
     * The constructor of the class.
     *
     * @param server the interface of the server.
     * @param client the interface of the real client.
     */
    public VirtualRmiClient(ServerNetInterface server, RmiClientInterface client) {
        String retrievedUsername;
        try {
            retrievedUsername = client.getUsername();
        } catch (RemoteException e) {
            retrievedUsername = "";
        }
        this.username = retrievedUsername;
        this.server = server;
        this.client = client;
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
            Logger.getDefaultLogger().log("Connection error: " + e.getMessage() + "!");
        }
    }
}
