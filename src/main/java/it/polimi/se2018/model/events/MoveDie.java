package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;
import it.polimi.se2018.utils.Coordinates;

/** This class is used to encapsulate the data needed
 *  to move an already placed Die on the Player's
 *  {@link it.polimi.se2018.model.Pattern}.
 *  This action is allowed by the 'Eglomise Brush' and
 *  'Copper Foil Burnisher' {@link it.polimi.se2018.model.ToolCard}.
 *
 *  @author giorgiolabate
 */

public class MoveDie extends ViewMessage{

    /**
     * Row and column of the position now occupied
     * by the chosen Die on the Pattern.
     */
    private Coordinates source;

    /**
     * Row and column of the new position
     * for the chosen Die on the Pattern.
     */
    private Coordinates destination;

    /**
     * Constructor of the class.
     * @param source The coordinates of the position
     *               where the chosen Die is
     *               currently placed.
     * @param destination The coordinates of the new
     *                    chosen position for the Die
     *                    that was placed in the position
     *                    indicated by {@code source}.
     * @param view The view reference.
     * @param action The action that the Player wants
     *               to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     */
    public MoveDie(Coordinates source, Coordinates destination,
                   View view, String action, String playerName) {
        super(view, action, playerName);
        this.source = source;
        this.destination = destination;
    }

    /**
     * Getter for the current position of the Die
     * on the Pattern.
     * @return the coordinates that indicate the position.
     */
    public Coordinates getSource() {
        return source;
    }

    /**
     * Getter for the new position of the Die
     * on the Pattern.
     * @return the coordinates that indicate the position.
     */
    public Coordinates getDestination() {
        return destination;
    }
}
