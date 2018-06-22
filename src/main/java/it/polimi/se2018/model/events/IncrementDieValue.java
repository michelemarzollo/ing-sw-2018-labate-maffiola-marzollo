package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

/**
 * This class is used to encapsulate the data needed
 * to perform the action of incrementing or decrementing
 * a drafted Die's value.
 *
 * @author giorgiolabate
 */
public class IncrementDieValue extends SelectDie {

    /**
     * Boolean that indicates that the Die's value
     * has to be incremented by one if {@code true}
     * or that it has to be decremented by one if {@code false}.
     */
    private boolean increment;

    /**
     * Constructor of the class.
     *
     * @param dieIndex   The index of the chosen Die
     *                   in the DraftPool.
     * @param increment  The boolean that indicates if the
     *                   Die's value has to be incremented
     *                   or decremented.
     * @param view       The view reference.
     * @param action     The action that the Player wants
     *                   to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     */
    public IncrementDieValue(int dieIndex, boolean increment,
                             View view, Action action, String playerName) {
        super(dieIndex, view, action, playerName);
        this.increment = increment;
    }

    /**
     * Getter for the boolean that indicates if the
     * Die's value has to be incremented or decremented.
     *
     * @return {@code true} if an increment of the Die is
     * requested, {@code false} otherwise (a decrement
     * of the Die is requested).
     */
    public boolean isIncrement() {
        return increment;
    }
}
