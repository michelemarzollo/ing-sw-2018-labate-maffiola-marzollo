package it.polimi.se2018.networking.messages;

/**
 * The enumeration that describes which commands can be brought
 * by the {@link Message} in the network.
 *
 * @author michelemarzollo
 */
public enum Command {

    SHOW,
    SHOW_ERROR,
    MODEL_UPDATE,
    VIEW_MESSAGE,
    ACK,
    LOGIN_MP,
    PING,
    LOGIN_SP

}
