package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Command;
import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class represents a Client on the server side (it implements
 * {@link ClientNetInterface}): it is the access point to client on the server.
 * It implements Runnable because the gatherer needs to start a new thread of
 * execution when launching VirtualTcpClient because the interaction with the
 * socket's stream (that are present here) are blocking methods.
 */
public class VirtualTcpClient implements ClientNetInterface, Runnable {

    /**
     * The server implementation.
     */
    private final ServerNetInterface server;

    /**
     * The client's username.
     */
    private String username;

    /**
     * The client socket that represents the ending point of the connection
     * on client's side, so this is the link with the actual client since we are on
     * server side.
     */
    private final Socket connection;

    /**
     * The output stream used to write to the socket.
     */
    private final ObjectOutputStream outputStream;
    /**
     * The input stream used to read from the socket.
     */
    private final ObjectInputStream inputStream;

    /**
     * Constructor of the class.
     *
     * @param server     The {@link ServerNetInterface} of the VirtualTcpClient that
     *                   represent the server implementation.
     * @param connection The socket representing client's connection.
     */
    public VirtualTcpClient(ServerNetInterface server, Socket connection) throws IOException {
        this.server = server;
        this.connection = connection;
        inputStream = new ObjectInputStream(connection.getInputStream());
        outputStream = new ObjectOutputStream(connection.getOutputStream());
    }

    /**
     * Tries to retrieve the username from the client.
     * <p>The username must be the first message sent by the client.</p>
     *
     * @return {@code true} if the username has been received; {@code false} otherwise.
     */
    private boolean receiveUsername() {
        try {
            //Read the message
            Message message = (Message) inputStream.readObject();
            //First message is the username
            if (message.getCommand() == Command.LOGIN) {
                this.username = (String) message.getBody();
            }
            return true;
        } catch (ClassNotFoundException e) {
            Logger.getDefaultLogger().log("Serialized class cannot be found" + e.getMessage());
        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }
        return false;
    }

    /**
     * Getter for the username that is sent through the first message of the communication.
     *
     * @return Client's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * This method is invoked when starting a VirtualTcpClient.
     * When launched, the VirtualTcpClient puts itself on hold of messages that are
     * the messages sent from the real clients (that will be ViewMessages), so the server
     * receives messages through VirtualTcpClients. When a message is received its management
     * is delegated to the server implementation (that will be the DelegateNetInterface)
     * through the {@code server.send(message)} invocation.
     */
    @Override
    public void run() {
        initializeConnection();
        while (!connection.isClosed()) {
            try {
                //Here the messages that arrive on the socket (when the send method is invoked
                //in the NetWorkHandler) are read.
                Message message = (Message) inputStream.readObject();

                if (message == null)
                    //The client has disconnected.
                    terminate();
                else
                    server.send(message);
            } catch (IOException e) {
                Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
                terminate();
            } catch (ClassNotFoundException e) {
                Logger.getDefaultLogger().log("Serialized class cannot be found" + e.getMessage());
            }
        }
    }


    /**
     * With this method a message is sent on the socket connection to the real client.
     * A message is sent when there is a model update or there is something to show on the real
     * client, this method is invoked by the {@link it.polimi.se2018.view.VirtualView} that is
     * {@link it.polimi.se2018.utils.Observer} of the model.
     */
    @Override
    public void notify(Message message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Retrieves the username and adds the connection to the server.
     * <p>If at least one of the two operations fails, the client connection is terminated.</p>
     * <p>If the initialization is successful, the client is notified with an ack</p>
     */
    private void initializeConnection() {
        boolean gotUsername = receiveUsername();
        if (!gotUsername)
            terminate();

        boolean added = server.addClient(this);
        if (!added)
            terminate();

        notify(new Message(Command.ACK, ""));
    }

    /**
     * Terminates the client connection.
     */
    private void terminate() {
        try {
            connection.close();
        } catch (IOException e) {
            //Do nothing
        }
    }
}
