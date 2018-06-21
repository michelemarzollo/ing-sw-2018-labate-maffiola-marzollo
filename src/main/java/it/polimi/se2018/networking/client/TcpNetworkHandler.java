package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Command;
import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

/**
 * This class represents the server on client side. It is the access point to
 * the server on client's side: it implements {@link ServerNetInterface}. It is
 * associated to a client when creating it.
 * It implements Runnable because the client needs to start a new thread of
 * execution when launching TcpNetworkHandler because the interaction with the
 * socket's stream (that are present here) are blocking methods.
 */
public class TcpNetworkHandler implements ServerNetInterface, Runnable {

    /**
     * The client implementation to which the TcpNetworkHandler is bounded.
     */
    private ClientNetInterface client;

    /**
     * The client socket that represents the ending point of the connection with
     * the server on client's side, so this is the link with the server since we
     * are on client side.
     */
    private final Socket clientConnection;

    /**
     * The input stream used to read from socket.
     */
    private final ObjectInputStream inputStream;
    /**
     * The output stream used to write to socket.
     */
    private final ObjectOutputStream outputStream;

    /**
     * Semaphore used to synchronize socket access.
     */
    private final Semaphore sentUsername = new Semaphore(0);

    /**
     * The constructor of the class. It bounds this handler to the {@code client} specified
     * and creates the client socket connecting to the server socket at the {@code address} and
     * {@code port} specified.
     *
     * @param address The IP address where to find the server socket.
     * @param port    The port where to find the server socket.
     */
    public TcpNetworkHandler(String address, int port) throws IOException {
        this.clientConnection = new Socket(address, port);
        outputStream = new ObjectOutputStream(clientConnection.getOutputStream());
        inputStream = new ObjectInputStream(clientConnection.getInputStream());
        Thread networkThread = new Thread(this);
        networkThread.start();
    }

    /**
     * Right after the creation of the handler it is run and puts himself on hold
     * of messages from the server through the socket connection until the connection
     * is closed.
     * When a message is received the handler invoke the {@code notify} method
     * on the {@link ClientImplementation} associated to it that will notify the
     * {@link it.polimi.se2018.view.View}. The messages that have to be notified are
     * show messages or model update messages.
     */
    @Override
    public void run() {
        try {
            sentUsername.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        while (!this.clientConnection.isClosed()) {
            try {
                Message message = (Message) inputStream.readObject();
                if (message == null)
                    //the connection has been closed
                    clientConnection.close();
                else
                    client.notify(message);

            } catch (SocketException e) {
                Logger.getDefaultLogger().log("Closed socket: terminating");
            } catch (IOException e) {
                Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                Logger.getDefaultLogger().log("Serialized class cannot be found" + e.getMessage());
            }
        }
    }

    /**
     * This method send through the connection the messages from this client
     * to the server (that will be {@link it.polimi.se2018.model.events.ViewMessage}).
     * It is invoked from the {@code update} of the {@link ClientImplementation}
     * that is {@link it.polimi.se2018.utils.Observer} of the {@link it.polimi.se2018.view.View}
     * and so is notified when the {@link it.polimi.se2018.view.View} is updated.
     *
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();

        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
        }
    }

    /**
     * The method to add a client to the server.
     *
     * @param client the client to add.
     * @param isMultiPlayer {@code true} if the client is playing in multi player mode;
     *                      {@code false} if it's playing in single player mode.
     *
     * @return {@code true} if the client had been added; {@code false} otherwise.
     */
    @Override
    public boolean addClient(ClientNetInterface client, boolean isMultiPlayer) {
        this.client = client;
        Command command = isMultiPlayer ? Command.LOGIN_MP : Command.LOGIN_SP;
        send(new Message(command, client.getUsername()));
        try {
            Message ack = (Message) inputStream.readObject();
            sentUsername.release();
            return ack.getCommand() == Command.ACK;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * The method to remove a client from the server.
     *
     * @param client the client to remove.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        close();
    }

    private void close() {
        try {
            clientConnection.close();
        } catch (IOException ignored) {
            //Do nothing
        }
    }

}
