package it.polimi.se2018.model.events;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.View;

public class ChooseValue extends ViewMessage {

    /**
     * The value to be assigned to the die which was just drafted from the DiceBag.
     */
    private int value;

    /**
     * The coordinates of the cell in the pattern where the die should be placed.
     */
    private Coordinates destination;

    /**
     * Constructor of the class.
     *
     * @param view        the view reference.
     * @param action      the action that the Player wants to perform.
     * @param playerName  the name of the player that is performing
     *                    the action.
     * @param value       the value to be assigned to the die which was just
     *                    drafted from the DiceBag.
     * @param destination the coordinates of the cell in the pattern
     *                    where the die should be placed.
     */
    public ChooseValue(View view, Action action, String playerName,
                       int value, Coordinates destination) {
        super(view, action, playerName);
        this.value = value;
        this.destination = destination;
    }

    /**
     * The getter for {@code value}.
     *
     * @return {@code this.value}.
     */
    public int getValue() {
        return value;
    }

    /**
     * The getter for {@code destination}.
     *
     * @return {@code this.destination}
     */
    public Coordinates getDestination() {
        return destination;
    }
}
