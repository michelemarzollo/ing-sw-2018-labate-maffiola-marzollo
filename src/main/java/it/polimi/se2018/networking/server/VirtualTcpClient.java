package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.utils.Logger;

import java.io.*;
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
     * The server implementation, will be {@link DelegateNetInterface}.
     */
    private ServerNetInterface server;

    /**
     * The client's username.
     */
    private String username;

    /**
     * The client socket that represents the ending point of the connection
     * on client's side, so this is the link with the actual client since we are on
     * server side.
     */
    private Socket connection;

    /**
     * Constructor of the class.
     * @param server The {@link ServerNetInterface} of the VirtualTcpClient that
     *               represent the server implementation.
     * @param connection The socket representing client's connection.
     */
    public VirtualTcpClient(ServerNetInterface server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
            //Read the message
            Message message = (Message) inputStream.readObject();
            //First message is the username
            this.username = (String) message.getBody();

        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            Logger.getDefaultLogger().log("Serialized class cannot be found" + e.getMessage());
        }

    }

    /**
     * Getter for the username that is sent through the first message of the communication.
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
         while (!connection.isClosed()) {
            try {
                //Here the messages that arrive on the socket (when the send method is invoked
                //in the NetWorkHandler) are read.
                ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
                Message message = (Message) inputStream.readObject();
                if(message == null){
                    //The client has disconnected.
                    connection.close();
                    server.removeClient(this);
                    break;
                }
                server.send(message);
            } catch (IOException e) {
                Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
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
            ObjectOutputStream outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
        }
    }
}
