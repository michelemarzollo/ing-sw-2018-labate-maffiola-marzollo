package it.polimi.se2018.model;

import java.util.List;

/**
 * The class represents the dice in the draft pool at a certain moment in the game.
 * It also contains, eventually, the die that must be used by the player while using a
 * specific ToolCard.
 *
 * @author michelemarzollo
 */
public class DraftPool {

    /**
     * The array that contains the dice in the draft pool at the moment.
     */
    private List<Die> dice;

    /**
     * The index of the die in {@code dice} that must be selected because a ToolCard
     * is being used.
     */
    private int forcedSelection;

    /**
     * The constructor of the class. When the class is built there is no forced selection:
     * the attribute <code>forcedSelection</code> is set to a non valid value -1.
     */
    public DraftPool() {
        this.dice = null;
        this.forcedSelection = -1;
    }

    /**
     * Returns the current amount of dice in the draft pool.
     *
     * @return the number of dice currently in the draft pool.
     */
    public int getAmount() {
        return dice.size();
    }

    /**
     * The getter for {@code dice}.
     *
     * @return the list {@code dice}.
     */
    public List<Die> getDice() {
        return dice;
    }

    /**
     * The setter for {@code dice}.
     *
     * @param dice the new list of dice to set.
     */
    public void setDice(List<Die> dice) {
        this.dice = dice;
    }

    /**
     * The getter for forcedSelection.
     *
     * @return the value of {@code forcedSelection}.
     */
    public int getForcedSelection() {
        return forcedSelection;
    }

    /**
     * The setter for forcedSelection.
     *
     * @param forcedSelection the index of the array that denotes the die that is forced to be used.
     */
    public void setForcedSelection(int forcedSelection) {
        this.forcedSelection = forcedSelection;
    }

    /**
     * The method to draft a die form the DraftPool.
     *
     * @param index the position of the die to draft in the list.
     */
    public void draft(int index) {
        dice.remove(index);
    }

    /**
     * The method to select a die from the DraftPool.
     *
     * @param index the index of the die to select in the DraftPool.
     * @return the die.
     */
    public Die select(int index) {
        return dice.get(index);
    }
}