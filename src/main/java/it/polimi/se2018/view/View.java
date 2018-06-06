package it.polimi.se2018.view;

import it.polimi.se2018.model.events.ModelUpdate;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

/**
 * Abstract base class for views.
 * <p>It is an observer for ModelUpdate and an observable for ViewMessage,
 * according to the MVC architecture.</p>
 */
public abstract class View extends Observable<ViewMessage>
        implements Observer<ModelUpdate> {

    /**
     * The name of the player the view is associated with.
     */
    private String playerName;

    /**
     * Shows the multi player game view.
     */
    public abstract void showMultiPlayerGame();

    /**
     * Shows the single player game view.
     */
    public abstract void showSinglePlayerGame();

    /**
     * Shows an error message
     * @param error the message that has to be displayed
     */
    public abstract void showError(String error);

    /**
     * Shows the pattern selection view.
     */
    public abstract void showPatternSelection();

    /**
     * Shows the private objective selection view.
     */
    public abstract void showPrivateObjectiveSelection();

    /**
     * Shows the score board view.
     */
    public abstract void showScoreBoard();

    /**
     * Shows the die selection view.
     */
    public abstract void showDieSelection();

    /**
     * Shows the move die selection view.
     * @param amount The amount of dice to move.
     */
    public abstract void showMoveSelection(int amount);

    /**
     * Shows the difficulty selection view.
     */
    public abstract void showDifficultySelection();

    /**
     * Shows the view specific to the lens cutter tool card.
     */
    public abstract void showLensCutterSelection();

    /**
     * Shows the destination and value selection view.
     */
    public abstract void showValueDestinationSelection();

    /**
     * Shows the game finished view.
     */
    public abstract void showFinalView();

    /**
     * Getter for the player name associated with the view.
     * @return The name of the player associated with the view
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Setter for the player name associated with the view.
     * @param playerName The name of the player associated with the view.
     */
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

}
