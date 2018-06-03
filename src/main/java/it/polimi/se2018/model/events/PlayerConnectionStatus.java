package it.polimi.se2018.model.events;

import it.polimi.se2018.view.ViewDataOrganizer;

/**
 * Event to update views about the change in the connection
 * status of a player.
 * @author dvdmff
 */
public class PlayerConnectionStatus extends ModelUpdate {

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
    public PlayerConnectionStatus(String playerName, boolean connected) {
        super(ModelEvent.PLAYER_CONNECTION_STATUS);
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

    /**
     * Pushes this instance of PlayerConnectionStatus into the organizer.
     * @param organizer The organizer where the message will be pushed into.
     */
    @Override
    public void pushInto(ViewDataOrganizer organizer) {
        organizer.push(this);
    }
}
