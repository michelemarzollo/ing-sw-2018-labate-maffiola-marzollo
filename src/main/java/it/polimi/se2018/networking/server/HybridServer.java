package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a server that runs over other servers.
 * <p>The sub-servers handles the actual connection mechanisms
 * but it is the hybrid server that keeps track of the connections.</p>
 * @author dvdmff
 */
public class HybridServer extends Server {

    /**
     * The list of all subsystems of the server.
     */
    private final List<Server> subsystems;

    /**
     * The map that associates the username to the connection for all active clients.
     */
    private final Map<String, ClientNetInterface> clients;

    /**
     * Creates a new HybridServer with the specified SeverNetInterface.
     * @param serverNetInterface The ServerNetInterface that will be used to handle messages
     *                           sent from the clients.
     */
    public HybridServer(ServerNetInterface serverNetInterface) {
        super(serverNetInterface);
        clients = new HashMap<>();
        subsystems = new ArrayList<>();
        //TODO: add rmi and tcp servers
    }

    /**
     * Getter for all connected clients.
     * @return A list of all connected clients.
     */
    @Override
    public List<ClientNetInterface> getClients() {
        return new ArrayList<>(clients.values());
    }

    /**
     * Returns only the connection associated with the specified username.
     * @param name The name that is associated with the desired connection.
     * @return The connection associated with {@code name}.
     */
    @Override
    public ClientNetInterface getClientFor(String name) {
        return clients.get(name);
    }

    /**
     * Adds a new connection.
     * <p>If the username is already taken, nothing is done and false is returned.</p>
     * @param client The client to be added.
     * @return {@code true} if the client has been successfully added;
     * {@code false} otherwise.
     */
    @Override
    public boolean addClient(ClientNetInterface client) {
        if(clients.containsKey(client.getUsername()))
            return false;
        clients.put(client.getUsername(), client);
        return true;
    }

    /**
     * Removes a client connection if it exists.
     * @param client The client connection to be removed.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        clients.remove(client.getUsername());
    }

    /**
     * Starts the server.
     * <p>This is achieved by starting all sub-systems.</p>
     */
    @Override
    public void start() {
        for(Server server : subsystems)
            server.start();
    }

    /**
     * Stops the server.
     * <p>This is achieved by stopping all sub-systems.</p>
     */
    @Override
    public void stop() {
        for(Server server : subsystems)
            server.stop();
    }

    /**
     * Tells if the server is running.
     * <p>The server is considered running if a least one of its sub-systems is running.</p>
     * @return {@code true} if the server is running; {@code false} otherwise.
     */
    @Override
    public boolean isRunning() {
        return subsystems.stream().anyMatch(Server::isRunning);
    }
}
