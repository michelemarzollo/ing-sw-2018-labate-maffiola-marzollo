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
     * Shows the view to select and increment a die.
     */
    public abstract void showDieIncrementSelection();

    /**
     * Shows the move die selection view.
     * @param amount The amount of dice to move.
     */
    public abstract void showMoveSelection(int amount);

    /**
     * Shows the move die selection view and allows to move up to 2 dice.
     */
    public abstract void showMoveUpToTwo();

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

    /**
     * Show the die placement view.
     */
    public abstract void showPlaceDie();

    /**
     * Shows a confirmation view.
     */
    public abstract void showConfirm();
}
