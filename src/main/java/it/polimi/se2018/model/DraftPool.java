package it.polimi.se2018.model;

import java.util.ArrayList;
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
     * The constructor of the class. When the class is built there is no forced selection:
     * the attribute <code>forcedSelection</code> is set to a non valid value -1.
     */
    public DraftPool() {
        this.dice = null;
    }

    /**
     * Returns the current amount of dice in the draft pool.
     *
     * @return The number of dice currently in the draft pool.
     */
    public int getAmount() {
        return dice.size();
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
    }

    /**
     * The method to draft a die form the DraftPool.
     *
     * @param index the position of the die to draft in the list.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public void draft(int index) {
        dice.remove(index);
    }

    /**
     * The method to select a die from the DraftPool.
     *
     * @param index the index of the die to select in the DraftPool.
     * @return the die.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public Die select(int index) {
        return dice.get(index);
    }


    /**
     * Method that returns {@code true} if {@code colour}
     * corresponds to at least a Die's colour in {@code draftPool}.
     * This method is useful to detect if a {@link ToolCard} can be used or not
     * in single player mode: Tool Cards may be used only by spending
     * a Die from the {@link DraftPool} that matches the ToolCard's Colour.
     *
     * @param colour The colour of the toolCard that the Player is
     *               trying to use.
     * @return {@code true} if a Die in the {@code draftPool} is
     * of the {@code colour} requested, {@code false} otherwise.
     */
    public boolean isPresent(Colour colour) {
        for (Die die : getDice()) {
            if (die.getColour().equals(colour)) return true;
        }
        return false;
    }
}