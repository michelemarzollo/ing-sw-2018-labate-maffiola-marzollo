package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Turn;

/**
 * The event to update the view when the turn of a player is finished, and
 * another player must play it's turn.
 *
 * @author michelemarzollo
 */
public class NextTurn extends ModelUpdate {

    private final boolean secondTurn;
    /**
     * The name of the player whose turn is the one following the event.
     */
    private final String nextPlayer;

    /**
     * Creates a message containing relevant information about the next turn.
     * @param turn The next turn.
     */
    public NextTurn(Turn turn) {
        super(ModelEvent.NEXT_TURN);
        this.nextPlayer = turn.getPlayer().getName();
        this.secondTurn = !turn.isSecondTurnAvailable();
    }

    /**
     * The getter for {@code nextPlayer}.
     *
     * @return The next player.
     */
    public String getNextPlayer() {
        return nextPlayer;
    }

    public boolean isSecondTurn() {
        return secondTurn;
    }
}
