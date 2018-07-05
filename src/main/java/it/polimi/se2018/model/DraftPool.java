package it.polimi.se2018.model;

import it.polimi.se2018.model.events.DraftPoolUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * The class represents the dice in the draft pool at a certain moment in the game.
 *
 * @author michelemarzollo
 */
public class DraftPool {

    /**
     * The array that contains the dice in the draft pool at the moment.
     */
    private List<Die> dice;

    /**
     * The reference to the {@link Game} to which the DraftPool belongs.
     */
    private final Game game;

    /**
     * The constructor of the class.
     *
     * @param game The game to which the DraftPool has to be bound.
     */
    public DraftPool(Game game) {
        this.dice = null;
        this.game = game;
    }

    /**
     * The method to notify the view of a change of its status.
     */
    private void notifyChange() {
        DraftPoolUpdate message = new DraftPoolUpdate(getDice());
        game.notifyObservers(message);
    }

    /**
     * The getter for {@code dice}.
     * <p>
     * A copy of the list is returned, in order not to expose
     * the rep.</p>
     *
     * @return The list {@code dice}.
     */
    public List<Die> getDice() {
        return new ArrayList<>(dice);
    }

    /**
     * The setter for {@code dice}.
     *
     * @param dice The new list of dice to set.
     */
    public void setDice(List<Die> dice) {
        this.dice = dice;
        notifyChange();
    }

    /**
     * The method to remove a die form the DraftPool.
     *
     * @param index the position of the die to draft in the list.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public void draft(int index) {
        dice.remove(index);
        notifyChange();
    }

    /**
     * The method to select a die from the DraftPool, without removing it.
     *
     * @param index the index of the die to select in the DraftPool.
     * @return the die.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Die select(int index) {
        return dice.get(index);
    }

}