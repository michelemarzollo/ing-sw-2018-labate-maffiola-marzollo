package it.polimi.se2018.model.events;

/**
 * The event to update the view when the turn of a player is finished, and
 * another player must play it's turn.
 *
 * @author michelemarzollo
 */
public class NextTurn extends ModelUpdate {

    /**
     * The name of the player whose turn is the one following the event.
     */
    private String nextPlayer;

    /**
     * The constructor of the class.
     *
     * @param nextPlayer The name of the next {@link it.polimi.se2018.model.Player}.
     */
    public NextTurn(String nextPlayer) {
        super(ModelEvent.NEXT_TURN);
        this.nextPlayer = nextPlayer;
    }

    /**
     * The getter for {@code nextPlayer}.
     *
     * @return The next player.
     */
    public String getNextPlayer() {
        return nextPlayer;
    }

}
