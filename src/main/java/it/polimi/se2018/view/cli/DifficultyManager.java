package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the one of the first phase of the game when the user
 * has to choose the level of difficulty he wishes to play if he has chosen
 * to play in Single Player Mode.
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the askDifficulty is invoked.
 */
public class DifficultyManager extends InputEventManager {

    /**
     * Indicates the chosen level of the difficulty.
     */
    private int difficulty;

    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public DifficultyManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The input inserted by the user that represents the level of
     *              difficulty chosen.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (difficulty == -1) {
                    difficulty = choice;
                    view.handleDifficultySelection(difficulty);
                }
            }
            else {
                showError();
            }
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        if(difficulty == -1){
            output.printTextNewLine("Choose the level of difficulty you wish to play. Enter a value between 1 and 5:");
        }
    }
}
