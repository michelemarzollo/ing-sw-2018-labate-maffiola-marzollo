package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the use of the Tool Cards that needs as a
 * parameter the index of the chosen die and the coordinates
 * for the placement of the die (special manager for the Cork-Backed Straightedge).
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the askIncrement method is invoked.
 */
public class ToolCardPlacementManager extends InputEventManager {

    /**
     * The index of the die in the Draft Pool.
     */
    private int dieIndex;

    /**
     * The coordinates for the placement.
     */
    private int row;
    private int col;

    /**
     * Reference to the input gatherer class: it is necessary to restore the
     * correct manager after the accomplishment of this task.
     */
    private CliInput gatherer;

    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     *@param gatherer The input gatherer to which this manager is bounded.
     */
    public ToolCardPlacementManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        dieIndex = -1;
        row = -1;
        col = -1;
        this.gatherer = gatherer;
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents here the index or his
     *              the coordinates for the placement.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input);
            if (dieIndex == -1) {
                handleDieIndex(choice);
            } else if (row == -1) {
                handleRow(choice);
            } else if (col == -1) {
                handleCol(choice);
            }
        } catch (NumberFormatException ex) {
            showError();
        }
    }

    /**
     * Handles the first move: the choice of the die in the Draft Pool.
     * @param choice The index entered by the user.
     */
    private void handleDieIndex(int choice) {
        if (choice < 0) {
            showError();
        } else {
            dieIndex = choice;
        }
    }

    /**
     * Handles the second move: the choice of the row where to place the die.
     * @param choice The input entered by the user.
     */
    private void handleRow(int choice) {
        if (choice < 0) {
            showError();
        } else {
            row = choice;
        }
    }

    /**
     * Handles the third move: the choice of the column where to place the die.
     * @param choice The input entered by the user.
     */
    private void handleCol(int choice) {
        if (choice < 0) {
            showError();
        } else {
            col = choice;
            view.handleToolCardUsage(dieIndex, new Coordinates(row, col));
            gatherer.setManager(new TurnHandlingManager(view, output));
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        if (dieIndex == -1) {
            output.printDraftPool(getDraftPool());
            output.printTextNewLine("Enter the index of the die:");
        } else if (row == -1) {
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the coordinates for your placement:");
            output.printText("Row (starting from 0): ");
        } else if (col == -1) {
            output.printText("Col (starting from 0): ");
        }
    }
}
