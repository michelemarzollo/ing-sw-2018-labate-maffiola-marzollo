package it.polimi.se2018.networking.server;

import it.polimi.se2018.utils.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer extends Server {

    /**
     * The default port to connect with.
     */
    private static final int PORT = 1099;

    /**
     * The url of the server.
     */
    private final String url;

    /**
     * The attribute that indicates if the rmi server is running:
     * it's set to {@code true} if it is running, to {@code false} otherwise.
     */
    private boolean isRunning = false;

    /**
     * The exposed server interface
     */
    private RmiServerInterface serverImplementation;

    /**
     * The constructor of the class.
     *
     * @param address     the address.
     * @param serviceName the name of the service.
     */
    public RmiServer(String address, String serviceName) {
        super();
        this.url = "//" + address + "/" + serviceName;
    }

    /**
     * The constructor of the class.
     *
     * @param superSystem the super-system to which the rmi server refers to.
     * @param address     the address.
     * @param serviceName the name of the service.
     */
    public RmiServer(Server superSystem, String address, String serviceName) {
        super(superSystem);
        this.url = "//" + address + "/" + serviceName;
    }

    /**
     * The method to start the RMI server.
     * <p>It starts the Registry and makes it listen at the port {@code PORT}.
     * It creates an instance of the server, that offers the service to the client.</p>
     */
    @Override
    public void start() {

        try {
            LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log("Already present Registry!");
        }

        try {
            serverImplementation = new RmiServerImplementation(getServerNetInterface());
            Naming.rebind(url, serverImplementation);
            isRunning = true;
        } catch (MalformedURLException e) {
            Logger.getDefaultLogger().log("It is impossible to register the indicated object!");
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log("Connection error: " + e.getMessage() + "!");
        }

    }

    /**
     * The method to interrupt the server.
     * <p>It unbinds the remote object.</p>
     */
    @Override
    public void stop() {
        try {
            Naming.unbind(url);
            UnicastRemoteObject.unexportObject(serverImplementation, true);
        } catch (RemoteException e) {
            Logger.getDefaultLogger().log("Connection error: " + e.getMessage() + "!");
        } catch (NotBoundException e) {
            //Unreachable
            Logger.getDefaultLogger().log("No binding associated to that name!");
        } catch (MalformedURLException e) {
            Logger.getDefaultLogger().log("It is impossible to deregister the indicated object!");
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
