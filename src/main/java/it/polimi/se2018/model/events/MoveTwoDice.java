package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;
import it.polimi.se2018.utils.Coordinates;

/** This class is used to encapsulate the data needed
 *  to move two dice on the Player's pattern.
 *  This action is allowed by the 'Lathekin' and by
 *  the 'Tap Wheel' {@link it.polimi.se2018.model.ToolCard}.
 *
 *  @author giorgiolabate
 */

 public class MoveTwoDice extends ViewMessage {

    /**
     * Array containing two coordinates
     * that indicates row and column of the positions
     * now occupied by the chosen dice on the Pattern.
     */
    private Coordinates[] sources;

    /**
     * Array containing two coordinates
     * that indicates row and column of the new
     * positions of the chosen dice on the Pattern.
     */
    private Coordinates[] destinations;

    /**
     * Constructor of the class.
     * @param sources The coordinates of the positions
     *                where the chosen dice are
     *                currently placed.
     * @param destinations The coordinates of the new
     *                     chosen positions for the dice
     *                     that were placed in the position
     *                     indicated by {@code sources}.
     * @param view The view reference.
     * @param action The action that the Player wants
     *               to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     */
    public MoveTwoDice(Coordinates[] sources, Coordinates[] destinations,
                       View view, Action action, String playerName) {
        super(view, action, playerName);
        this.sources = sources;
        this.destinations = destinations;
    }

    /**
     * Getter for the current positions of the Die
     * on the Pattern.
     * @return An array of two coordinates that indicate
     * the positions.
     */
    public Coordinates[] getSources() {
        return sources;
    }

    /**
     * Getter for the new positions of the dice
     * on the Pattern.
     * @return An array of two coordinates that indicate
     * the positions.
     */
    public Coordinates[] getDestinations() {
        return destinations;
    }
}
