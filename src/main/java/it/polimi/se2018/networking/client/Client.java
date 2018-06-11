package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.view.View;

/**
 * Represents the client.
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
     * @param view The view to associate the client with.
     * @param server The server used for the communications.
     */
    public Client(View view, ServerNetInterface server){
        netInterface = new ClientImplementation(this, view);
        this.server = server;
        server.addClient(getNetInterface());
    }

    /**
     * Getter for the server the client uses.
     * @return The server the client uses.
     */
    public ServerNetInterface getServer(){
        return server;
    }

    /**
     * Getter for the network interface of the client.
     * @return The network interface of the client.
     */
    public ClientNetInterface getNetInterface(){
        return netInterface;
    }
}
