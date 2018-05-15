package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

/**
 * This Class represents the event through
 * which the view(Observable) notifies the
 * controller(Observer) about an action performed
 * by the user. Possible additional data requested
 * to perform the action requested will be encapsulated
 * by more specific events that inherit from this class.
 *
 * @author giorgiolabate
 */
public class ViewMessage {

    /**
     * {@link View} reference.
     */
    private View view;

    /**
     * String that describes the action
     * that the user want to perform.
     * The {@link it.polimi.se2018.controller.Controller}
     * will distinguish which action to perform parsing
     * this string.
     */
    private String action;

    /**
     * Name of Player that is
     * performing the action.
     */
    private String playerName;


    /**
     * Constructor of the class.
     * @param view The view reference.
     * @param action The action that the Player wants
     *               to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     */
    public ViewMessage(View view, String action, String playerName) {
        this.view = view;
        this.action = action;
        this.playerName = playerName;
    }


    /**
     * Getter for the {@link View} reference.
     * @return the reference to the {@link View}.
     */
    public View getView() {
        return view;
    }

    /**
     * Getter for the String that describes the
     * action.
     * @return a String that describes the
     * action.
     */
    public String getAction() {
        return action;
    }

    /**
     * Getter for the name of the Player
     * that is performing the action.
     * @return the name of the player that
     * is performing the action.
     */
    public String getPlayerName() {
        return playerName;
    }
}
