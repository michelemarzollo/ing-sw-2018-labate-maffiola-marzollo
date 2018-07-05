package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to select the difficulty in single player mode.
 *
 * @author giorgiolabate
 */
public class DifficultyManager extends InputEventManager {

    /**
     * Indicates the chosen level of the difficulty.
     */
    private int difficulty = -1;

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public DifficultyManager(ClientView view, CliPrinter output) {
        super(view, output);
    }

    /**
     * Handles the input entered by the user.
     * <p>After collecting the difficulty, calls the proper handler of {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        try {
            if (difficulty == -1) {
                difficulty = Integer.parseUnsignedInt(input.trim());
                getView().handleDifficultySelection(difficulty);
            }
        } catch (NumberFormatException ex) {
            showError();
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (difficulty == -1) {
            getOutput().println("Choose the level of difficulty you wish to play. Enter a value between 1 and 5:");
        }
    }

    /**
     * Resets the input manager.
     */
    @Override
    public void reset() {
        difficulty = -1;
    }
}
