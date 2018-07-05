package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;
import it.polimi.se2018.utils.Coordinates;

/**
 * This class is used to encapsulate the data needed for the action of swapping
 * one Die from the {@link it.polimi.se2018.model.DraftPool}
 * with a Die of the {@link it.polimi.se2018.model.RoundTrack}.
 * This action is allowed by the {@link it.polimi.se2018.model.ToolCard}
 * 'Lens Cutter'.
 *
 * @author giorgiolabate
 */
public class DiceSwap extends ViewMessage {
    /**
     * The index of the Die in the DraftPool that is chosen to be swapped.
     */
    private int sourceIndex;

    /**
     * The indexes that refers to the position of the Die in the RoundTrack
     * that must be swapped with the Die in the DraftPool.
     */
    private Coordinates destination;

    /**
     * Constructor of the class.
     *
     * @param sourceIndex The index used to select the
     *                    Die from the DraftPool.
     * @param destination The coordinates of the
     *                    Die in the RoundTrack.
     * @param view        The view reference.
     * @param action      The action that the Player wants
     *                    to perform.
     * @param playerName  The name of the player that
     *                    is performing the action.
     */
    public DiceSwap(int sourceIndex, Coordinates destination,
                    View view, Action action, String playerName) {
        super(view, action, playerName);
        this.sourceIndex = sourceIndex;
        this.destination = destination;
    }

    /**
     * Getter for the index of the Die in the DraftPool.
     *
     * @return The index of the chosen Die in the DraftPool.
     */
    public int getSourceIndex() {
        return sourceIndex;
    }

    /**
     * Getter for the coordinates of the Die in the RoundTrack.
     *
     * @return The coordinates of the position in the RoundTrack
     * at which the Die in the DraftPool must be placed through
     * the swapping.
     */
    public Coordinates getDestination() {
        return destination;
    }
}
