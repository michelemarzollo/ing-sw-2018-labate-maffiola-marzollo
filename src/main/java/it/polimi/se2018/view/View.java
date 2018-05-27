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
    private final String playerName;

    /**
     * Creates a new view for the specified player.
     * @param playerName The name of the player.
     */
    protected View(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Shows the multi player game view.
     */
    public abstract void showMultiPlayerGame();

    /**
     * Shows the single player game view.
     */
    public abstract void showSinglePlayerGame();

    /**
     * Shows an error message.
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
    /*
    public abstract void handlePlacement(int index, Coordinates destination);
    public abstract void handleToolCardSelection(String name);
    public abstract void handleEndTurn();
    public abstract void handlePatternSelection(String patternName);
    public abstract void handleToolCardUsage(String name, int index, boolean increment);
    public abstract void handleToolCardUsage(String name, int index);
    public abstract void handleToolCardUsage(String name, int index, Coordinates destination);
    public abstract void handleToolCardUsage(String name, Coordinates source, Coordinates destination);
    public abstract void handleToolCardUsage(String name, Coordinates[] sources, Coordinates[] destinations);
    public abstract void handleDisconnect();
*/

    /**
     * Getter for the player name associated with he view.
     * @return The name of the player associated with he view
     */
    public String getPlayerName() {
        return playerName;
    }

}
