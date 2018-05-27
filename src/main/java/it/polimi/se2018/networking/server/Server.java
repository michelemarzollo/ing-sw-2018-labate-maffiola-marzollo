package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;

import java.util.List;

public abstract class Server {

    private ServerNetInterface serverNetInterface;

    public Server(ServerNetInterface serverNetInterface) {
        this.serverNetInterface = serverNetInterface;
    }

    public ServerNetInterface getServerNetInterface() {
        return serverNetInterface;
    }

    public abstract List<ClientNetInterface> getClients();

    public abstract ClientNetInterface getClientFor(String name);

    public abstract boolean addClient(ClientNetInterface client);

    public abstract void removeClient(ClientNetInterface client);

    public abstract void start();

    public abstract void stop();

    public abstract boolean isRunning();
}
