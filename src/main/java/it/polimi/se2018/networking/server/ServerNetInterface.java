package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Message;

public interface ServerNetInterface {

    void send(Message message);

    void addClient(ClientNetInterface client);
}
