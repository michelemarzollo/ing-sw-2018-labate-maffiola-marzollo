package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

/**
 * This class is used to encapsulate the data needed
 * to select the difficulty level in Solo mode.
 * The setup of the Game will depend on this choice:
 * the chosen number of ToolCards depends on the difficulty
 * level the user wishes to play.
 *
 * @author giorgiolabate
 */

public class SelectDifficulty extends ViewMessage{

    /**
     * The level of difficulty chosen
     * by the user
     */
    private int difficulty;

    /**
     * Constructor of the class.
     * @param difficulty The level of difficulty.
     * @param view The view reference.
     * @param action The action that the Player wants
     *               to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     */
    public SelectDifficulty(int difficulty,
                            View view, Action action, String playerName){
        super(view, action, playerName);
        this.difficulty = difficulty;
    }

    /**
     * Getter for the level of the difficulty.
     * @return the chosen level of difficulty.
     */
    public int getDifficulty() {
        return difficulty;
    }
}
