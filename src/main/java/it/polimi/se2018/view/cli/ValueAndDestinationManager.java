package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to select a value for a die and place it on the player's pattern.
 *
 * @author giorgiolabate
 */
public class ValueAndDestinationManager extends InputEventManager {

    /**
     * The value that the user chose for the drafted die.
     */
    private int value = -1;

    /**
     * The coordinates for the placement.
     */
    private int row = -1;
    private int col = -1;

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public ValueAndDestinationManager(ClientView view, CliPrinter output) {
        super(view, output);
    }


    /**
     * Handles the input entered by the user.
     * <p>After collecting the die value and the destinations coordinates, calls the
     * proper handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (value == -1 && choice <= 6 && choice >= 1) {
                    value = choice;
                } else if (row == -1) {
                    row = choice;
                } else if (col == -1) {
                    col = choice;
                    getView().handleToolCardUsage(new Coordinates(row, col), value);
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
        if (value == -1) {
            getOutput().printPatternLarger(getPattern());
            getOutput().printDraftPool(getDraftPool());
            getOutput().println("The index of your chosen die in the draft pool is " +
                    getDataOrganizer().getNextTurn().getForcedSelectionIndex());
            getOutput().println("Enter the value you want to assign to the chosen die:");
        } else if (row == -1) {
            getOutput().println("Enter the coordinates of the space in your pattern where you want to place the die:");
            getOutput().print("Row (starting from 0): ");

        } else if (col == -1) {
            getOutput().print("Col (starting from 0): ");
        }
    }
}
