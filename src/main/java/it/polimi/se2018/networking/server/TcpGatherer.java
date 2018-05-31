package it.polimi.se2018.networking.server;

import it.polimi.se2018.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * This class' task is to handle connections: it adds new clients to
 * the server creating {@link VirtualTcpClient} that are immediately started
 * only if a thread in threadPool is idle (otherwise is queued)
 * to be ready to handle messages received through the socket.
 * It implements Runnable because the server needs to start a new thread of
 * execution when launching TcpGatherer because the accept method of
 * {@link ServerSocket} is a blocking method.
 */
public class TcpGatherer implements Runnable{

    /**
     * The server implementation, will be {@link DelegateNetInterface}.
     */
    private ServerNetInterface server;

    /**
     * The port associated to the server and so to his
     * gatherer.
     */
    private int port;

    /**
     * The {@link ServerSocket} of the server: it represents the ending point
     * of the connection on server's side.
     */
    private ServerSocket serverSocket;

    /**
     * ThreadPool that uses a fixed number of threads (256). At any point,
     * at most the fixed number of threads will be active processing tasks.
     * If incoming requests are submitted when all threads are active,
     * they will wait in a queue until a thread is available.
     */
    private ExecutorService threadPool =
            Executors.newFixedThreadPool(256);
    /**
     * Getter for serverSocket.
     * @return The socket that represents the server's side of the connection.
     */
    public ServerSocket getServerSocket(){
        return serverSocket;
    }

    /**
     * Getter for the threadPool.
     * @return The threadPool of the running server.
     */
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    /**
     * Constructor of the class.
     * @param server The {@link ServerNetInterface} of the TcpServer that
     *               represent the server implementation.
     * @param port The port of the server to which the gatherer has to bind
     *             the {@link ServerSocket}.
     */
    public TcpGatherer(ServerNetInterface server, int port) {
        this.server = server;
        this.port = port;
        try {
            //Creates the serverSocket binding it to the specified port.
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred opening the server socket: " + e.getMessage());
        }
    }

    /**
     * The gatherer is immediately run after creation. It puts itself on hold of
     * clients' request of connection and when it receives them and their socket it
     * creates and run a {@link VirtualTcpClient} (if a thread in threadPool is idle)
     * that is added to server's clients. Before adding the new virtual client it must be
     * sure that the userName has been correctly set, so this action is made in {@link VirtualTcpClient}
     * constructor.
     */
    public void run(){

        while(!serverSocket.isClosed()) {
            try {
                Socket clientSocket;
                clientSocket = serverSocket.accept();
                //Creates a new VirtualTcpClient binding it to its socket and to the server implementation.
                VirtualTcpClient vc = new VirtualTcpClient(server, clientSocket);
                //Invokes the addClient method of its serverImplementation (DelegateNetInterface)
                //that delegates the task to the DefaultNetInterface that actually adds the client
                //and associates a VirtualView to it.
                server.addClient(vc);
                //The VirtualTcpClient is not executed a priori, but only if a
                //thread is currently idle.
                this.threadPool.execute(vc);

            } catch (IOException e) {
                Logger.getDefaultLogger().log("An error occurred when waiting for a connection: " + e.getMessage());
            }
        }

    }
}
