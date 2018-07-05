package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;
import it.polimi.se2018.utils.Coordinates;

/**
 * This class is used to encapsulate the data needed to place a Die in the
 * Player's Pattern.
 * It is used for a regular placement or when exploiting
 * 'Cork-backed Straightedge' {@link it.polimi.se2018.model.ToolCard}.
 */
public class PlaceDie extends ViewMessage {

    /**
     * Index of the chosen Die in the DraftPool
     * that has to be placed on the Player's Pattern.
     */
    private int dieIndex;

    /**
     * Row and column of the position chosen
     * on the Player's Pattern to place the Die.
     */
    private Coordinates destination;

    /**
     * Constructor of the Class.
     *
     * @param dieIndex    The position of the chosen Die
     *                    in the DraftPool.
     * @param destination The coordinates that indicates
     *                    the position on the Player's Pattern
     *                    where the Die has to be placed.
     * @param view        The view reference.
     * @param action      The action that the Player wants
     *                    to perform.
     * @param playerName  The name of the player that
     *                    is performing the action.
     */
    public PlaceDie(int dieIndex, Coordinates destination,
                    View view, Action action, String playerName) {
        super(view, action, playerName);
        this.dieIndex = dieIndex;
        this.destination = destination;
    }

    /**
     * Getter for the position of the chosen Die.
     *
     * @return The index of the Die in the DraftPool.
     */
    public int getDieIndex() {
        return dieIndex;
    }

    /**
     * Getter for the row and column of the destination
     * chosen for the Die.
     *
     * @return The Coordinates of the chosen position.
     */
    public Coordinates getDestination() {
        return destination;
    }
}
