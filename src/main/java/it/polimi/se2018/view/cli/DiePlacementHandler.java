package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to place a die in player's pattern.
 *
 * @author giorgiolabate
 */
public class DiePlacementHandler extends InputEventManager {

    /**
     * Indicates whether the user has confirmed to perform this action or not.
     */
    private boolean confirmDone = false;

    /**
     * The chosen index of the die in the DraftPool that has to be placed.
     */
    private int dieIndex = -1;

    /**
     * The row coordinate for the placement.
     */
    private int row = -1;

    /**
     * The column coordinate for the placement.
     */
    private int col = -1;

    /**
     * Reference to the manager of the turn.
     */
    private final TurnHandlingManager manager;

    /**
     * Constructor of the class.
     *
     * @param view    The view to which this manager is bounded.
     * @param output  The output destination where the prompts of this manager
     *                are shown.
     * @param manager The TurnHandlingManager.
     */
    public DiePlacementHandler(ClientView view, CliPrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
    }

    /**
     * Handles the choice of the user if to confirm or not to place a die.
     *
     * @param choice The choice performed by the user.
     */
    private void handleConfirm(int choice) {
        if (choice == 1) {
            setConfirmDone();
        } else {
            manager.reset();
        }
    }

    /**
     * Handles the input entered by the user.
     * <p>After collecting user confirmation, the source index and the destination coordinates,
     * calls the proper handler of {@link ClientView}.</p>
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

        if (!confirmDone) {
            handleConfirm(choice);
        } else if (dieIndex == -1) {
            dieIndex = choice;
        } else if (row == -1) {
            row = choice;
        } else if (col == -1) {
            col = choice;
            getView().handlePlacement(dieIndex, new Coordinates(row, col));
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (!confirmDone) {
            showConfirmPrompt();
        } else {
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

    /**
     * Shows the confirmation prompt.
     */
    private void showConfirmPrompt() {
        getOutput().println("You chose to place a die, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }

    /**
     * Sets the confirmation flag.
     */
    private void setConfirmDone() {
        this.confirmDone = true;
    }
}
