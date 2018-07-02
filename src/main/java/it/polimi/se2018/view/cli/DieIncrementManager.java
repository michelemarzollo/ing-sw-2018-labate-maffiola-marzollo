package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to increment or decrement a die.
 */
public class DieIncrementManager extends InputEventManager {

    /**
     * Index of the chosen die in the Draft Pool.
     */
    private int index = -1;

    /**
     * Represents the choice about incrementing or decrementing the die's value.
     */
    private int value = -1;


    /**
     * Constructor of the class.
     *
     * @param view     The view to which this manager is bounded.
     * @param output   The output destination where the prompts of this manager
     *                 are shown.
     */
    public DieIncrementManager(ClientView view, CliPrinter output) {
        super(view, output);
    }

    /**
     * Handles the input entered by the user.
     * <p>After collecting the index of the die in the draft pool and the choice if to
     * increment or decrement, calls the proper handler of {@link ClientView}</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (index == -1) {
                    index = choice;
                } else if (value == -1) {
                    boolean increment = choice == 1;
                    value = choice;
                    getView().handleToolCardUsage(index, increment);
                }
            } else showError();
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
        if (index == -1) {
            getOutput().printDraftPool(getDraftPool());
            getOutput().println("Enter the index of the die you wish to use: \n");
        } else if (value == -1) {
            getOutput().println("Enter 1 to increment the value of the selected die, 2 to decrement it:");
        }
    }
}
