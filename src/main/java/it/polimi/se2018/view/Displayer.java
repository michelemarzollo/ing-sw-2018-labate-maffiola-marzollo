package it.polimi.se2018.view;

/**
 * Interface for a display manager.
 * <p>It's used by the view to display data on screen.</p>
 */
public interface Displayer {
    /**
     * Displays the login window.
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
     * Displays a wait message before the starting of the game.
     */
    void displayWaitMessage();

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
    void displayScoreBoard();

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
     * Asks the player to select a die from the Draft Pool and to choose whether
     * to increment or decrement its value.
     */
    void selectDieAndIncrement();


    /**
     * Forces the displayed data to be refreshed.
     */
    void refreshDisplayedData();

    /**
     * Sets the {@link ViewDataOrganizer} from which the Model's informations
     * will be retrieved.
     * @param organizer the {@link ViewDataOrganizer} that will be bound to the
     *                  Displayer.
     */
    void setDataOrganizer(ViewDataOrganizer organizer);

    /**
     * The getter for the {@link ViewDataOrganizer}, that keeps the track
     * of model messages.
     * @return the organizer.
     */
    ViewDataOrganizer getDataOrganizer();

    /**
     * Sets the client view that will handle requests.
     * @param view The view that will handle requests.
     */
    void setView(ClientView view);

    /**
     * The getter for the client view that handles the requests.
     * @return the client view.
     */
    ClientView getView();

}
