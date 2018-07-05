package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to place a die with a tool card.
 *
 * @author giorgiolabate
 */
public class ToolCardPlacementManager extends InputEventManager {

    /**
     * The index of the die in the Draft Pool.
     */
    private int dieIndex = -1;

    /**
     * The coordinates for the placement.
     */
    private int row = -1;
    private int col = -1;

    /**
     * Constructor of the class
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public ToolCardPlacementManager(ClientView view, CliPrinter output) {
        super(view, output);
    }


    /**
     * Handles the input entered by the user.
     * <p>Calls the proper handler in {@link ClientView} after collecting
     * the source index and the destination coordinates.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        int choice;
        try {
            choice = Integer.parseUnsignedInt(input);
        } catch (NumberFormatException ex) {
            showError();
            return;
        }

        if (dieIndex == -1) {
            dieIndex = choice;
        } else if (row == -1) {
            row = choice;
        } else if (col == -1) {
            col = choice;
            getView().handleToolCardUsage(dieIndex, new Coordinates(row, col));
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (dieIndex == -1) {
            getOutput().printDraftPool(getDraftPool());
            getOutput().println("Enter the index of the die:");
        } else if (row == -1) {
            getOutput().printPatternLarger(getPattern());
            getOutput().println("Enter the coordinates for your placement:");
            getOutput().print("Row (starting from 0): ");
        } else if (col == -1) {
            getOutput().print("Col (starting from 0): ");
        }
    }
}
