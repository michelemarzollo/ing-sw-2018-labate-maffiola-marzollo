package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Die;

import java.util.List;

/**
 * Represents a message containing information about the draft pool.
 * <p>This class is immutable.</p>
 *
 * @author dvdmff
 */
public class DraftPoolUpdate extends ModelUpdate {

    /**
     * The list of dice in the draft pool.
     */
    private final List<Die> dice;

    /**
     * The index of the mandatory selection.
     */
    private final int forcedSelectionIndex;

    /**
     * The constructor of the class.
     *
     * @param dice                 The dice in the draft pool.
     * @param forcedSelectionIndex The index of the mandatory selection or -1
     *                             if it isn't the case.
     */
    public DraftPoolUpdate(List<Die> dice, int forcedSelectionIndex) {
        super(ModelEvent.DRAFT_POOL_UPDATE);
        this.dice = dice;
        this.forcedSelectionIndex = forcedSelectionIndex;
    }

    /**
     * Getter for the dice list.
     * @return The dice in the draft pool.
     */
    public List<Die> getDice() {
        return dice;
    }

    /**
     * Getter for the forced selection index.
     * @return The index of the forced selection or -1 if it isn't the case.
     */
    public int getForcedSelectionIndex() {
        return forcedSelectionIndex;
    }
}
