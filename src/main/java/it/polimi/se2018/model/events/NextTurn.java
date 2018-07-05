package it.polimi.se2018.model.events;

import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.Turn;
import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;

/**
 * The event to update the view when the turn of a player is finished, and
 * another player must play it's turn.
 *
 * @author michelemarzollo
 * @author dvdmff
 */
public class NextTurn extends ModelUpdate {

    /**
     * The name of the next player to move.
     */
    private final String playerName;

    /**
     * Flag to indicate if the player has a second turn this round.
     */
    private final boolean secondTurnAvailable;

    /**
     * Flag to indicate if the player has already placed a die this turn.
     */
    private final boolean alreadyPlacedDie;

    /**
     * Flag to indicate if the player has already used a tool card this turn.
     */
    private final boolean alreadyUsedToolCard;

    /**
     * Index of the only die the player can draft.
     */
    private final int forcedSelectionIndex;

    /**
     * Index of the die that will be sacrificed to activate a tool card.
     */
    private final int sacrificeIndex;

    /**
     * The currently active tool card.
     */
    private final ToolCard activeToolCard;

    /**
     * Creates a message containing relevant information about the next turn.
     *
     * @param turn The next turn.
     */
    public NextTurn(Turn turn) {
        super(ModelEvent.NEXT_TURN);
        playerName = turn.getPlayer().getName();
        secondTurnAvailable = turn.isSecondTurnAvailable();
        alreadyPlacedDie = turn.hasAlreadyPlacedDie();
        alreadyUsedToolCard = turn.hasAlreadyUsedToolCard();
        forcedSelectionIndex = turn.getForcedSelectionIndex();
        sacrificeIndex = turn.getSacrificeIndex();
        activeToolCard = turn.getSelectedToolCard();
    }

    /**
     * Getter for the player name.
     *
     * @return The player name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Tells if the player has a second turn this round.
     *
     * @return {@code true} if the player has a second turn this round.
     * {@code false} otherwise.
     */
    public boolean isSecondTurnAvailable() {
        return secondTurnAvailable;
    }

    /**
     * Tells if the player has already placed a die this turn.
     *
     * @return {@code true} if the player has already placed a die this turn.
     * {@code false} otherwise.
     */
    public boolean isAlreadyPlacedDie() {
        return alreadyPlacedDie;
    }

    /**
     * Tells if the player has already used a tool card this turn.
     *
     * @return {@code true} if the player has already used a tool card this turn.
     * {@code false} otherwise.
     */
    public boolean isAlreadyUsedToolCard() {
        return alreadyUsedToolCard;
    }

    /**
     * Getter for the index of the forced selection.
     *
     * @return The index of the forced selection.
     */
    public int getForcedSelectionIndex() {
        return forcedSelectionIndex;
    }

    /**
     * Getter for the index of the sacrifice die.
     *
     * @return The index of the sacrifice die.
     */
    public int getSacrificeIndex() {
        return sacrificeIndex;
    }

    /**
     * Getter for the currently active tool card.
     *
     * @return The currently active tool card.
     */
    public ToolCard getActiveToolCard() {
        return activeToolCard;
    }

    /**
     * Pushes this instance of NextTurn into the organizer.
     *
     * @param organizer The organizer where the message will be pushed into.
     */
    @Override
    public void pushInto(ViewDataOrganizer organizer) {
        organizer.push(this);
    }

    /**
     * Defaults to Object.equals(Object).
     *
     * @param other The object to compare.
     * @return {@code true} if this and other are equals.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    /**
     * Defaults to Object.hashCode().
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Compare this object with the given one.
     *
     * @param other The NextTurn object to compare.
     * @return {@code true} if the two objects are equals.
     */
    public boolean equals(NextTurn other) {
        return other.getPlayerName().equals(getPlayerName()) &&
                other.isAlreadyPlacedDie() == isAlreadyPlacedDie() &&
                other.isAlreadyUsedToolCard() == isAlreadyUsedToolCard() &&
                other.isSecondTurnAvailable() == isSecondTurnAvailable();
    }
}
