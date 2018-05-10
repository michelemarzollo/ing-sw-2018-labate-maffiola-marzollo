package it.polimi.se2018.model;

import java.util.List;

/**
 * The class represents the dice in the draft pool at a certain moment in the game.
 * It also contains, eventually, the die that must be used by the player while using a
 * specific ToolCard
 *
 * @author michelemarzollo
 */
public class DraftPool {

    /**
     * The array that contains the dice in the draft pool at the moment
     */
    private List<Die> dice;

    /**
     * The index of the die in {@code dice} that must be selected because a ToolCard
     * is being used
     */
    private int forcedSelection;

    /**
     * The constructor of the class. When the class is built there is no forced selection:
     * the attribute <code>forcedSelection</code> is set to a non valid value -1
     */
    public DraftPool() {
        this.dice = null;
        this.forcedSelection = -1;
    }

    /**
     * The getter for {@code dice}
     *
     * @return The list {@code dice}
     */
    public List<Die> getDice() {
        return dice;
    }

    /**
     * The setter for {@code dice}
     *
     * @param dice The new list of dice to set
     */
    public void setDice(List<Die> dice) {
        this.dice = dice;
    }

    /**
     * The getter for forcedSelection
     *
     * @return The value of {@code forcedSelection}
     */
    public int getForcedSelection() {
        return forcedSelection;
    }

    /**
     * The setter for forcedSelection
     *
     * @param forcedSelection The index of the array that denotes the die that is forced to be used
     */
    public void setForcedSelection(int forcedSelection) {
        this.forcedSelection = forcedSelection;
    }

    /**
     * The method to draft a die form the DraftPool
     *
     * @param index The position of the dice to draft in the list
     * @return The die in position {@code index}
     */
    public Die draft(int index) {
        //The temporary value that allows to remove the dice
        Die d = dice.get(index);
        dice.remove(index);
        return d;
    }
}