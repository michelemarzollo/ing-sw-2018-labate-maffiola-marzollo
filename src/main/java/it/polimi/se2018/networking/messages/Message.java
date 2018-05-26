package it.polimi.se2018.networking.messages;

import java.io.Serializable;

public class Message {

    private Serializable body;

    private Command command;

    public Message (Command command, Serializable body){
        this.command = command;
        this.body = body;
    }

    public Serializable getBody() {
        return body;
    }

    public Command getCommand() {
        return command;
    }
}
