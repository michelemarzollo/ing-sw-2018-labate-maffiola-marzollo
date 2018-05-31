package it.polimi.se2018.networking.server;

import it.polimi.se2018.utils.Logger;

import java.io.IOException;

/**
 * This class implements the server functionality to handle socket connections.
 * The {@link HybridServer} will create an instance of this class (will be
 * its master server) and will then starts this server in tandem with the
 * {@link RmiServer}.
 */
public class TcpServer extends Server {


    //private ArrayList<ClientNetInterface> clients; //remove?
    /**
     * The IP address where to find the server.
     */
    private String address;

    /**
     * The port associated to the server.
     */
    private int port;

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
     * @param address The IP address of the server.
     * @param port The port at which the server is bound.
     */
    public TcpServer(String address, int port) {
        super();
        this.address = address;
        this.port = port;
       // clients = new ArrayList<>();
    }

    /**
     * Constructor to make this server a slave-server. It will be invoked by the
     * {@link HybridServer} that will be its master-server.
     * @param server The master-server
     * @param address The IP address of the server.
     * @param port The port at which the server is bound.
     */
    public TcpServer(Server server, String address, int port) {
        //The super class constructor instantiates also the serverNetInterface
        //for this server. For the TcpServer it will be the DelegateNetInterface.
        super(server);
        this.address = address;
        this.port = port;
        //clients = new ArrayList<>();
    }

    /**
     * Starts the server launching a {@link TcpGatherer} and setting isRunning to
     * true: the server is actually active and ready to receive client connections.
     */
    @Override
    public void start() {
        gatherer = new TcpGatherer(getServerNetInterface(), port);
        gatherer.run();
        isRunning = true;
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
            gatherer.getThreadPool().shutdown();
            gatherer.getServerSocket().close();
        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred when stopping the server");
        }
        isRunning = false;
    }

    /**
     * Tells if the server is active or not.
     * @return {@code true} if the server has been correctly started creating
     * a {@link java.net.ServerSocket} through the gatherer, {@code false} otherwise.
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
