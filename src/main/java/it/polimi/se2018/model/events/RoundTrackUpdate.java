package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;

import java.util.List;

/**
 * Represents a message containing information about the round track.
 * <p>This class is immutable.</p>
 *
 * @author dvdmff
 */
public class RoundTrackUpdate extends ModelUpdate {

    /**
     * The matrix of dice in the round track.
     */
    private final List<List<Die>> roundTrack;

    /**
     * The constructor of the class.
     * @param roundTrack The matrix of dice in the round track.
     */
    public RoundTrackUpdate(List<List<Die>> roundTrack) {
        super(ModelEvent.ROUND_TRACK_UPDATE);
        this.roundTrack = roundTrack;
    }

    /**
     * Getter for the dice in the round track.
     * @return A matrix of dice that are in the round track.
     */
    public List<List<Die>> getRoundTrack() {
        return roundTrack;
    }

    /**
     * Pushes this instance of RoundTrackUpdate into the organizer.
     * @param organizer The organizer where the message will be pushed into.
     */
    @Override
    public void pushInto(ViewDataOrganizer organizer) {
        organizer.push(this);
    }
}
