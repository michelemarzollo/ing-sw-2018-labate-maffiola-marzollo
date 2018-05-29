package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Message;

/**
 * This class is used by sub-servers to delegate the master server.
 * @author dvdmff
 */
public class DelegateNetInterface implements ServerNetInterface {

    /**
     * The sub-server the instance refers to.
     */
    private final Server server;

    /**
     * Creates a new DelegateNetInterface for the given server.
     * @param server The sub-server that has to delegate its net interface.
     */
    public DelegateNetInterface(Server server) {
        this.server = server;
    }

    /**
     * Delegates the master server to send a message.
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        server.getMasterServer().getServerNetInterface().send(message);
    }

    /**
     * Delegates the master server to send a message.
     * @param client the client to be added.
     */
    @Override
    public void addClient(ClientNetInterface client) {
        server.getMasterServer().getServerNetInterface().addClient(client);
    }

    /**
     * Delegates the master server to send a message.
     * @param client the client to be removed.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        server.getMasterServer().getServerNetInterface().removeClient(client);
    }
}
