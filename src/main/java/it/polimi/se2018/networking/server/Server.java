package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for servers.
 * <p>Subclasses only have to deal with connections and all those mechanisms
 * that are required for the correct operations the network protocol they implement.</p>
 * <p>This class is immutable and thread-safe.</p>
 *
 * @author dvdmff
 */
public abstract class Server {

    /**
     * The net interface of the server.
     */
    private final ServerNetInterface serverNetInterface;

    /**
     * The super system of the server. May be null.
     */
    private final Server masterServer;

    /**
     * Map containing the set of clients which the server is responsible for.
     */
    private final Map<String, ClientNetInterface> clients;

    /**
     * Creates a master server.
     * <p>A master server is a server that has no super server and directly handles
     * connections.</p>
     */
    public Server() {
        this.serverNetInterface = new DefaultNetInterface(this);
        masterServer = null;
        clients = new HashMap<>();
    }

    /**
     * Creates a slave server.
     * <p>A slave server is a server that has a master server and thus doesn't
     * directly handle connections by itself, but delegates its master to do so.</p>
     *
     * @param masterServer The reference to the master server.
     */
    public Server(Server masterServer) {
        this.serverNetInterface = new DelegateNetInterface(this);
        this.masterServer = masterServer;
        clients = new HashMap<>();
    }

    /**
     * Getter for the net interface of the server.
     *
     * @return The net interface of the server.
     */
    public final ServerNetInterface getServerNetInterface() {
        return serverNetInterface;
    }

    /**
     * Getter for connected clients.
     *
     * @return A list of all the connected clients the server is responsible for.
     */
    public final synchronized List<ClientNetInterface> getClients() {
        return new ArrayList<>(clients.values());
    }

    /**
     * Returns only the connection associated with the specified username.
     *
     * @param name The name that is associated with the desired connection.
     * @return The connection associated with {@code name}.
     */
    public final synchronized ClientNetInterface getClientFor(String name) {
        return clients.get(name);
    }

    /**
     * Adds a new connection.
     * <p>If the username is already taken, nothing is done and false is returned.</p>
     *
     * @param client The client to be added.
     * @return {@code true} if the client has been successfully added;
     * {@code false} otherwise.
     */
    public final synchronized boolean addClient(ClientNetInterface client) {
        if (clients.containsKey(client.getUsername()))
            return false;
        clients.put(client.getUsername(), client);
        return true;
    }

    /**
     * Removes a client connection if it exists.
     *
     * @param client The client connection to be removed.
     */
    public final synchronized void removeClient(ClientNetInterface client) {
        clients.remove(client.getUsername());
    }

    /**
     * Starts the server.
     */
    public abstract void start();

    /**
     * Stops the server.
     */
    public abstract void stop();

    /**
     * Tells if the server is currently running.
     *
     * @return {@code true} if the server is running; {@code false} otherwise.
     */
    public abstract boolean isRunning();

    /**
     * Getter for the super system of the server.
     *
     * @return The reference to the super system, or {@code null} if it doesn't
     * exist.
     */
    public final Server getMasterServer() {
        return masterServer;
    }
}
