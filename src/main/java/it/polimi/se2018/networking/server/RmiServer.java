package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class RmiServer extends Server {

    /**
     * The default port to connect with.
     */
    private static final int PORT = 1099;

    /**
     * The list of clients connected to the server.
     */
    private List<ClientNetInterface> clients;

    /**
     * The address of the server.
     */
    private String address;

    /**
     * The name of the exposed RMI service.
     */
    private String serviceName;

    /**
     * The attribute that indicates if the rmi server is running:
     * it's set to {@code true} if it is running, to {@code false} otherwise.
     */
    private boolean isRunning;

    /**
     * The constructor of the class.
     *
     * @param serverNetInterface the server that handles the communication
     *                           with the client independently from the connection.
     * @param address            the address.
     * @param serviceName        the name of the service.
     */
    public RmiServer(ServerNetInterface serverNetInterface, String address, String serviceName) {
        super(serverNetInterface);
        this.address = address;
        this.serviceName = serviceName;
    }

    /**
     * The getter for {@code clients} in the superclass.
     *
     * @return {@code clients}.
     */
    @Override
    public List<ClientNetInterface> getClients() {
        return clients;
    }

    /**
     * The method that return the client corresponding to a certain username.
     *
     * @param name the name of the client to search.
     * @return the client corresponding to the name.
     */
    @Override
    public ClientNetInterface getClientFor(String name) {
        for (ClientNetInterface client : clients) {
            if (client.getUsername().equals(name))
                return client;
        }
        return null;
    }

    /**
     * The method to add a client to the server.
     *
     * @param client the client to add.
     * @return {@code true} if the server was able to add a client,
     * {@code} false otherwise.
     */
    @Override
    public boolean addClient(ClientNetInterface client) {
        for (ClientNetInterface c : clients) {
            if (c.getUsername().equals(client.getUsername()))
                return false;
        }
        clients.add(client);
        return true;
    }

    /**
     * The method to remove a client from the server.
     *
     * @param client the client to remove.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        clients.remove(client);
    }

    /**
     * The method to start the RMI server.
     * <p>
     * It starts the Registry and makes it listen at the port {@code PORT}.
     * It creates an instance of the server, that offers the service to the client.</p>
     */
    @Override
    public void start() {

        isRunning = true;

        try {
            LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            System.out.println("Already present Registry!");
        }

        try {
            RmiServerImplementation serverImplementation =
                    new RmiServerImplementation(getServerNetInterface());
            Naming.rebind("//localhost/MyServer", serverImplementation);
        } catch (MalformedURLException e) {
            System.err.println("It is impossible to register the indicated object!");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        }

    }

    /**
     * The method to interrupt the sever.
     * <p>It unbinds the remote object.</p>
     */
    @Override
    public void stop() {
        try {
            Naming.unbind("//localhost/MyServer");
        } catch (RemoteException e) {
            System.err.println("Connection error: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            System.err.println("No binding associated to that name!");
        } catch (MalformedURLException e) {
            System.err.println("It is impossible to deregister the indicated object!");
        }
        isRunning = false;
    }

    /**
     * The method that tells if the server is running.
     *
     * @return {@code true} if the RMI server is running, {@code false} otherwise.
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
