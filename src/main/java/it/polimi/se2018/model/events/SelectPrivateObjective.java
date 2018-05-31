package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.view.View;


/**
 * This class is used to encapsulate the data needed
 * to select the {@link it.polimi.se2018.model.PrivateObjectiveCard}
 * in Solo mode.
 * At the end of the Game the Player is asked to choose
 * between the two dealt {@link it.polimi.se2018.model.PrivateObjectiveCard}
 * and this message contains the Colour of the chosen one.
 *
 * @author giorgiolabate
 */
public class SelectPrivateObjective extends ViewMessage{

    private Colour colour;
    /**
     * Constructor of the class.
     * @param view       The view reference.
     * @param action     The action that the Player wants
     *                   to perform.
     * @param playerName The name of the player that has to choose.
     * @param colour The colour of the {@link it.polimi.se2018.model.PrivateObjectiveCard}
     *               that the Player wishes to choose.
     */
    public SelectPrivateObjective(View view, Action action, String playerName, Colour colour) {
        super(view, action, playerName);
        this.colour = colour;
    }

    /**
     * Getter for the colour of the {@link it.polimi.se2018.model.PrivateObjectiveCard}.
     * @return The colour chosen by the Player for his {@link it.polimi.se2018.model.PrivateObjectiveCard}.
     */
    public Colour getColour() {
        return colour;
    }
}
