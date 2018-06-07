package it.polimi.se2018.view;

import java.util.Map;

/**
 * Interface for a display manager.
 * <p>It's used by the view to display data on screen.</p>
 */
public interface Displayer {

    /**
     * Displays the request of the information needed to start the game.
     */
    void displayLoginView();

    /**
     * Displays the multi player board.
     */
    void displayMultiPlayerGame();

    /**
     * Displays the single player board.
     */
    void displaySinglePlayerGame();

    /**
     * Displays an error message.
     * @param error The error message.
     */
    void displayError(String error);

    /**
     * Asks the player to select a pattern among candidates.
     */
    void askPattern();

    /**
     * Asks the player to select a private objective among the given ones.
     */
    void askPrivateObjective();

    /**
     * Displays the score board.
     */
    void displayScoreBoard(Map<String, Integer> scoreBoard);

    /**
     * Lets the player select a die from the draft pool.
     */
    void selectDie();

    /**
     * Lets the player select up to {@code amount} dice to move on its pattern.
     * @param amount The maximum amount of dice to be moved.
     */
    void moveDie(int amount);

    /**
     * Asks the wished difficulty to the player when in single player mode.
     */
    void askDifficulty();

    /**
     * Asks the player to select which dice to swap in the draft pool and round track.
     */
    void askDiceToSwap();

    /**
     * Asks the player to select a value for the selected die.
     */
    void askValueDestination();

    /**
     * Forces the displayed data to be refreshed.
     */
    void refreshDisplayedData();

    /**
     * Sets the data organizer that keeps track of received messages.
     * @param organizer The data organizer.
     */
    void setDataOrganizer(ViewDataOrganizer organizer);

    /**
     * Sets the client view that will handle requests.
     * @param view The view that will handle requests.
     */
    void setView(ClientView view);

    /**
     * Gets the client view that handles requests.
     * @return the view that handles requests.
     */
    ClientView getView();
}
