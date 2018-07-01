package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.view.View;

/**
 * Represents the client.
 *
 * @author dvdmff
 */
public class Client {

    /**
     * The net interface of the client.
     */
    private final ClientNetInterface netInterface;

    /**
     * The server the client communicates with.
     */
    private final ServerNetInterface server;

    /**
     * Creates a new client that handles the specified view and uses the given
     * sever for communication.
     *
     * @param view   The view to associate the client with.
     * @param server The server used for the communications.
     */
    public Client(View view, ServerNetInterface server) {
        netInterface = new ClientImplementation(this, view);
        this.server = server;
    }

    /**
     * Getter for the server the client uses.
     *
     * @return The server the client uses.
     */
    public ServerNetInterface getServer() {
        return server;
    }

    /**
     * Getter for the network interface of the client.
     *
     * @return The network interface of the client.
     */
    public ClientNetInterface getNetInterface() {
        return netInterface;
    }

    /**
     * Connects the client to the server in the specified game mode.
     *
     * @param isMultiPlayer {@code true} if in multi player mode; {@code false}
     *                      if in single player mode.
     */
    public void connect(boolean isMultiPlayer) {
        boolean success = getServer().addClient(getNetInterface(), isMultiPlayer);
        if(!success)
            throw new ConnectionRefusedException();
    }

    /**
     * Terminates the connection to the server.
     */
    public void disconnect() {
        getServer().removeClient(getNetInterface());
    }

    /**
     * Exception to indicate the connection was refused.
     */
    public static class ConnectionRefusedException extends RuntimeException{
    }
}
