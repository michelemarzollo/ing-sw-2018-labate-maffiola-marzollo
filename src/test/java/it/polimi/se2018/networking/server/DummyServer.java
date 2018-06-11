package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy ServerNetInterface only used in tests to isolate the behaviour under test.
 */
public class DummyServer implements ServerNetInterface {

    /**
     * List of added clients.
     */
    private List<ClientNetInterface> clients = new ArrayList<>();

    /**
     * Disabled.
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        //Do nothing
    }

    /**
     * Adds the client in the client list.
     * @param client the client to add.
     */
    @Override
    public boolean addClient(ClientNetInterface client) {
        return clients.add(client);
    }

    /**
     * Removes the clients with the same username as the provided one.
     * @param client the client to remove.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        clients.removeIf(c -> c.getUsername().equals(client.getUsername()));
    }

    /**
     * Getter for the list of added clients.
     * @return The list of added clients.
     */
    public List<ClientNetInterface> getClients(){
        return clients;
    }
}
