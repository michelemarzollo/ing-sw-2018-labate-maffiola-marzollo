package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.GameSetup;
import it.polimi.se2018.model.events.PlayerStatus;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;

import java.util.List;

/**
 * Abstract base class for input managers.
 * <p>Concrete subclasses will handle user's input and prompt pertinent messages
 * depending on the current game state and their purpose.</p>
 */
public abstract class InputEventManager {
    /**
     * The view to which all the managers will be bound.
     */
    private ClientView view;

    /**
     * The output destination to which all the managers will be
     * associated.
     */
    private CliPrinter output;

    /**
     * Constructor of the class.
     *
     * @param view   The client view.
     * @param output The output destination.
     */
    public InputEventManager(ClientView view, CliPrinter output) {
        this.view = view;
        this.output = output;
    }

    /**
     * This method handles in the correct way the user input: it is invoked
     * every time the user enters some input.
     *
     * @param input The String inserted by the user.
     */
    public abstract void handle(String input);

    /**
     * Shows the right prompt in every situation: it is displayed after each
     * user interaction.
     */
    public abstract void showPrompt();

    /**
     * Getter for the client view.
     *
     * @return The view to which the manager is bounded.
     */
    public ClientView getView() {
        return view;
    }

    /**
     * Getter for the output destination.
     *
     * @return The associated output destination.
     */
    public CliPrinter getOutput() {
        return output;
    }

    /**
     * Getter for the {@link ViewDataOrganizer} associated to the CliDisplayer.
     *
     * @return the {@link ViewDataOrganizer} to which the CliDisplayer refers.
     */
    public ViewDataOrganizer getDataOrganizer() {
        return view.getDataOrganizer();
    }


    /**
     * Helper method to display to the player the correct candidates in the choice of
     * the Pattern and for the getPrivateCards to retrieve the correct Private Objective Cards
     * of the user.
     * The index that the player occupies among the {@link GameSetup} players
     * corresponds to the index where his candidates are in the same message and
     * where his Private Objective Cards are: this method find that index.
     *
     * @param name  The player's name.
     * @param names The array of player's names.
     * @return The index where to find {@code name} among {@code names}.
     */
    int findPlayerIndex(String name, String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i])) return i;
        }
        return -1;
    }

    /**
     * Getter for the players' status in the game.
     *
     * @return a List of {@link PlayerStatus} that represent all the players
     * in the game.
     */
    List<PlayerStatus> getPlayers() {
        return getDataOrganizer().getAllPlayerStatus();
    }

    /**
     * Getter for the Public Objective Cards in the game.
     *
     * @return an array of the Public Objective Cards in the game.
     */
    PublicObjectiveCard[] getPublicCards() {
        return getDataOrganizer().getGameSetup().getPublicObjectives();
    }

    /**
     * Getter for the Private Objective Cards of the user.
     *
     * @return an array of the user's Private Objective Cards.
     */
    PrivateObjectiveCard[] getPrivateCards() {
        int playerIndex = findPlayerIndex(view.getPlayerName(), getDataOrganizer().getGameSetup().getPlayers());
        return getDataOrganizer().getGameSetup().getPrivateObjectives()[playerIndex];
    }

    /**
     * Getter for the Tool Cards in the game.
     *
     * @return an array of the Tool Cards in the game.
     */
    ToolCard[] getToolCards() {
        return getDataOrganizer().getGameSetup().getToolCards();
    }

    /**
     * Getter for the user's chosen Pattern.
     *
     * @return The Pattern chosen from the user at the beginning of the game.
     */
    Pattern getPattern() {
        return getDataOrganizer().getPlayerStatus(view.getPlayerName()).getPattern();
    }

    /**
     * Getter for the Round Track of the game.
     *
     * @return a List of Lists of Dice that represents the Round Track.
     */
    List<List<Die>> getRoundTrack() {
        return getDataOrganizer().getRoundTrack();
    }

    /**
     * getter for the Draft Pool of the game.
     *
     * @return a List of dice that represents the Draft Pool.
     */
    List<Die> getDraftPool() {
        return getDataOrganizer().getDraftPool();
    }

    /**
     * Verifies the game mode.
     *
     * @return {@code true} if the selected mode is Single Player,
     * {@code false} otherwise.
     */
    boolean isSinglePlayer() {
        return getDataOrganizer().getAllPlayerStatus().size() == 1;
    }

    /**
     * Verifies if the user has already placed a die in his turn or not.
     *
     * @return {@code true} it the user has already placed a die,
     * {@code false} otherwise.
     */
    boolean hasPlacedDie() {
        return view.getDataOrganizer().getNextTurn().isAlreadyPlacedDie();
    }

    /**
     * Verifies if the user has already used a Tool Card in his turn or not.
     *
     * @return {@code true} it the user has already used a Tool Card,
     * {@code false} otherwise.
     */
    boolean hasUsedToolCard() {
        return view.getDataOrganizer().getNextTurn().isAlreadyUsedToolCard();
    }

    /**
     * Displays an error message when the user insert an incorrect input
     * from a 'syntactical' point of view.
     */
    void showError() {
        output.println("Invalid Input!");
    }

    /**
     * Resets the event manager.
     * <p>By default, this operation is not supported.</p>
     *
     * @throws UnsupportedOperationException if not implemented by subclass.
     */
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
