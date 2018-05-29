package it.polimi.se2018.networking.server;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a server that runs over other servers.
 * <p>The sub-servers handles the actual connection mechanisms
 * but it is the hybrid server that keeps track of the connections.</p>
 * @author dvdmff
 */
public class HybridServer extends Server {

    /**
     * The list of all subServers of the server.
     */
    private final List<Server> subServers;

    /**
     * Creates a new HybridServer with the specified SeverNetInterface.
     */
    public HybridServer(String address, String serviceName, int port) {
        super();
        subServers = new ArrayList<>();
        subServers.add(new RmiServer(this, address, serviceName));
        //subServers.add(new TcpServer(this, address, port));
    }

    /**
     * Starts the server.
     * <p>This is achieved by starting all sub-systems.</p>
     */
    @Override
    public void start() {
        for(Server server : subServers)
            server.start();
    }

    /**
     * Stops the server.
     * <p>This is achieved by stopping all sub-systems.</p>
     */
    @Override
    public void stop() {
        for(Server server : subServers)
            server.stop();
    }

    /**
     * Tells if the server is running.
     * <p>The server is considered running if a least one of its sub-systems is running.</p>
     * @return {@code true} if the server is running; {@code false} otherwise.
     */
    @Override
    public boolean isRunning() {
        return subServers.stream().anyMatch(Server::isRunning);
    }
}
