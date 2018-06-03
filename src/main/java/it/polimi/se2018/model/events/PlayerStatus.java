package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Pattern;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.view.ViewDataOrganizer;

/**
 * Represents a message containing information about a player status, excluded
 * connection status.
 * <p>This class is immutable.</p>
 * @author dvdmff
 */
public class PlayerStatus extends ModelUpdate {

    /**
     * The name of the player.
     */
    private final String playerName;

    /**
     * The number of tokens of the player.
     */
    private final int tokens;

    /**
     * The pattern of the player.
     */
    private final Pattern pattern;

    /**
     * The constructor of the class.
     *
     * @param player The player whose status is to be notified.
     */
    public PlayerStatus(Player player) {
        super(ModelEvent.PLAYER_STATUS);
        this.playerName = player.getName();
        this.tokens = player.getTokens();
        this.pattern = player.getPattern();
    }

    /**
     * Getter for the player name.
     * @return The player name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter for the number of tokens of the player.
     * @return The number of tokens of the player.
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Getter for the pattern of the player.
     * @return The pattern of the player.
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Pushes this instance of PlayerStatus into the organizer.
     * @param organizer The organizer where the message will be pushed into.
     */
    @Override
    public void pushInto(ViewDataOrganizer organizer) {
        organizer.push(this);
    }
}
