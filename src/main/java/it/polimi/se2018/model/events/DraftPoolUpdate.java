package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;

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
     * The constructor of the class.
     *
     * @param dice The dice in the draft pool.
     */
    public DraftPoolUpdate(List<Die> dice) {
        super(ModelEvent.DRAFT_POOL_UPDATE);
        this.dice = dice;
    }

    /**
     * Getter for the dice list.
     *
     * @return The dice in the draft pool.
     */
    public List<Die> getDice() {
        return dice;
    }

    /**
     * Pushes this instance of DraftPoolUpdate into the organizer.
     *
     * @param organizer The organizer where the message will be pushed into.
     */
    @Override
    public void pushInto(ViewDataOrganizer organizer) {
        organizer.push(this);
    }
}
