package it.polimi.se2018.networking.messages;

import java.io.Serializable;

/**
 * The messages that are exchanged in the network.
 *
 * @author michelemarzollo
 */
public class Message {

    /**
     * The body of the message.
     */
    private Serializable body;

    /**
     * The command of the message.
     */
    private Command command;

    /**
     * The constructor of the class.
     *
     * @param command the body of the message.
     * @param body    the command of the message.
     */
    public Message(Command command, Serializable body) {
        this.command = command;
        this.body = body;
    }

    /**
     * The getter for {@code body}.
     *
     * @return {@code body}.
     */
    public Serializable getBody() {
        return body;
    }

    /**
     * The getter for {@code command}.
     *
     * @return {@code command}.
     */
    public Command getCommand() {
        return command;
    }
}
