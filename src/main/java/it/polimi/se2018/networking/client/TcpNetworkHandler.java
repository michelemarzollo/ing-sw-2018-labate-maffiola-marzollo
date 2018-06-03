package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
    private Socket clientConnection;

    /**
     * The constructor of the class. It bounds this handler to the {@code client} specified
     * and creates the client socket connecting to the server socket at the {@code address} and
     * {@code port} specified.
     * @param address The IP address where to find the server socket.
     * @param port The port where to find the server socket.
     */
    public TcpNetworkHandler(String address, int port) {

        try {
            this.clientConnection = new Socket(address, port);
            this.run();
        } catch (UnknownHostException e) {
            Logger.getDefaultLogger().log("The IP address of the host could not be determined " + e.getMessage());
        } catch (IOException e) {
            Logger.getDefaultLogger().log("an I/O error occurs when creating the clientConnection " + e.getMessage());
        }
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
            while (!this.clientConnection.isClosed()) {
                ObjectInputStream inputStream = new ObjectInputStream(clientConnection.getInputStream());
                Message message = (Message) inputStream.readObject();
                if (message == null) { //the server has been closed
                    clientConnection.close();
                    break;
                } else {
                    client.notify(message);
                }
            }
        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            Logger.getDefaultLogger().log("Serialized class cannot be found" + e.getMessage());
        }
    }

    //la send viene invocata dalla update della ClientImplementation che è observer dei viewMessage e quindi
    //viene notificata quando la View viene aggiornata

    /**
     * This method send through the connection the messages from this client
     * to the server (that will be {@link it.polimi.se2018.model.events.ViewMessage}).
     * It is invoked from the {@code update} of the {@link ClientImplementation}
     * that is {@link it.polimi.se2018.utils.Observer} of the {@link it.polimi.se2018.view.View}
     * and so is notified when the {@link it.polimi.se2018.view.View} is updated.
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        try {

            ObjectOutputStream outputStream = new ObjectOutputStream(clientConnection.getOutputStream());
            outputStream.writeObject(message);
            outputStream.flush();

        } catch (IOException e) {
            Logger.getDefaultLogger().log("An error occurred: " + e.getMessage());
        }
    }

    /**
     * The method to add a client to the server.
     * @param client the client to add.
     */
    @Override
    public void addClient(ClientNetInterface client) {
        this.client = client;
        //TODO authentication
        //does nothing because this operation is handled server side by the TcpGatherer.
    }

    /**
     * The method to remove a client from the server.
     * @param client the client to remove.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        //does nothing because this operation is handled server side by the VirtualTcpClient.
    }

}
