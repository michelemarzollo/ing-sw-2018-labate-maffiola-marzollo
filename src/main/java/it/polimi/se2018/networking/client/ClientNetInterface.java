package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.messages.Message;

public interface ClientNetInterface {

    String getUserName();

    void notify(Message message);
}
