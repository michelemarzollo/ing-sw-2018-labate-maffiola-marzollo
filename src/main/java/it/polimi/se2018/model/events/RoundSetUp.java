package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Die;

/**
 * The message to update the view when a round is finished: the RoundTrack and the
 * DraftPool must be updated.
 *
 * @author michelemarzollo
 */
public class RoundSetUp extends ModelUpdate {

    /**
     * The updated roundTrack.
     */
    private Die[][] roundTrack;

    /**
     * The new draftPool.
     */
    private Die[] draftPool;

    /**
     * The number of the actual round.
     */
    private int roundNumber;

    /**
     * The constructor of the class.
     *
     * @param updateType  The kind of message it is.
     * @param roundTrack  The updated roundTrack.
     * @param draftPool   The new draftPool.
     * @param roundNumber The number of the actual round.
     */
    public RoundSetUp(String updateType, Die[][] roundTrack, Die[] draftPool, int roundNumber) {

        super(updateType);
        this.roundTrack = roundTrack;
        this.draftPool = draftPool;
        this.roundNumber = roundNumber;

    }

    /**
     * The getter for {@code roundTrack}.
     *
     * @return {@code roundTrack}
     */
    public Die[][] getRoundTrack() {
        return roundTrack;
    }

    /**
     * The getter for {@code draftPool}.
     *
     * @return {@code draftPool}
     */
    public Die[] getDraftPool() {
        return draftPool;
    }

    /**
     * The getter for {@code roundNumber}.
     *
     * @return {@code roundNumber}
     */
    public int getRoundNumber() {
        return roundNumber;
    }

}
