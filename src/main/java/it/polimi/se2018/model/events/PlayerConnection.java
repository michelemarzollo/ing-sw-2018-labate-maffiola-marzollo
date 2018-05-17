package it.polimi.se2018.model.events;

/**
 * Event to update views about the change in the connection
 * status of a player.
 * @author dvdmff
 */
public class PlayerConnection extends ModelUpdate {

    /**
     * The name of the involved player.
     */
    private final String playerName;
    /**
     * Flag to indicate if the player is connected.
     */
    private final boolean connected;

    /**
     * Creates a new instance with the specified name and connection flag.
     * @param playerName The name of the player who changed his connection status.
     * @param connected Flag for the new connection status.
     */
    public PlayerConnection(String playerName, boolean connected) {
        super(ModelEvent.PLAYER_CONNECTION);
        this.playerName = playerName;
        this.connected = connected;
    }

    /**
     * Getter for the involved player name.
     * @return The name of the involved player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter for the connection flag.
     * @return {@code true} if the player is connected;
     * {@code false} otherwise.
     */
    public boolean isConnected() {
        return connected;
    }
}
