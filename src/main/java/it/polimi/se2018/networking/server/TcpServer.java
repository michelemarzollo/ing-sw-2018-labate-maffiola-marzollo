package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.utils.Logger;

import java.io.IOException;

/**
 * This class implements the server functionality to handle socket connections.
 * The {@link HybridServer} will create an instance of this class (will be
 * its master server) and will then starts this server in tandem with the
 * {@link RmiServer}.
 *
 * @author giorgiolabate
 */
public class TcpServer extends Server {

    /**
     * The port associated to the server.
     */
    private int port;

    private Thread gathererThread;

    /**
     * Reference to a {@link TcpGatherer} instance whose role is to take care
     * of the connection's requests by the clients.
     */
    private TcpGatherer gatherer;

    /**
     * Tells if the server is running or if it has not been started/has been
     * stopped.
     */
    private boolean isRunning;

    /**
     * Constructor to make this server a super-server.
     *
     * @param port The port at which the server is bound.
     */
    public TcpServer(int port) {
        super();
        this.port = port;
    }

    /**
     * Constructor to make this server a slave-server.
     *
     * @param server The master-server
     * @param port   The port at which the server is bound.
     */
    public TcpServer(Server server, int port) {
        //The super class constructor instantiates also the serverNetInterface
        //for this server. For the TcpServer it will be the DelegateNetInterface.
        super(server);
        this.port = port;
    }

    /**
     * Starts the server launching a {@link TcpGatherer} and setting isRunning to
     * true: the server is actually active and ready to receive client connections.
     */
    @Override
    public void start() {
        try {
            gatherer = new TcpGatherer(getServerNetInterface(), port);
            gathererThread = new Thread(gatherer);
            gathererThread.start();
            isRunning = true;
        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }
    }

    /**
     * Stops the server imposing the {@link TcpGatherer} to close the
     * {@link java.net.ServerSocket} on which it is waiting for client connections,
     * to shutdown the {@code threadPool} and setting isRunning to false:
     * the server is not active anymore and it is not ready to receive client connections.
     */
    @Override
    public void stop() {
        try {
            if (gatherer != null)
                gatherer.close();
            if (gathererThread != null)
                gathererThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        isRunning = false;

        for (ClientNetInterface client : getClients())
            getServerNetInterface().removeClient(client);
    }

    /**
     * Tells if the server is active or not.
     *
     * @return {@code true} if the server has been correctly started creating
     * a {@link java.net.ServerSocket} through the gatherer, {@code false} otherwise.
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
