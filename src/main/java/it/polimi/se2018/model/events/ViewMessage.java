package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

import java.io.Serializable;

/**
 * This Class represents the event through which the View(Observable) notifies the
 * Controller(Observer) about an action performed by the user.
 * Possible additional data requested to perform an action will be
 * encapsulated by more specific events that inherit from this class.
 *
 * @author giorgiolabate
 */
public class ViewMessage implements Serializable {

    /**
     * {@link View} reference.
     */
    private transient View view;

    /**
     * Action that describes what the user want to do.
     * The {@link it.polimi.se2018.controller.Controller} will distinguish which
     * action to perform through this attribute actual value.
     */
    private Action action;

    /**
     * Name of Player that is performing the action.
     */
    private String playerName;


    /**
     * Constructor of the class.
     *
     * @param view       the view reference.
     * @param action     the action that the Player wants to perform.
     * @param playerName the name of the player that is performing the action.
     */
    public ViewMessage(View view, Action action, String playerName) {
        this.view = view;
        this.action = action;
        this.playerName = playerName;
    }


    /**
     * Getter for the {@link View} reference.
     *
     * @return the reference to the {@link View}.
     */
    public View getView() {
        return view;
    }

    /**
     * Getter for the Action.
     *
     * @return the Action that has to be performed.
     */
    public Action getAction() {
        return action;
    }

    /**
     * Getter for the name of the Player
     * that is performing the action.
     *
     * @return the name of the player that
     * is performing the action.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Setter for the View reference.
     *
     * @param view the View reference.
     */
    public void setView(View view) {
        this.view = view;
    }
}
