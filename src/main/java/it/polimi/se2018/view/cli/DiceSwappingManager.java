package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to swap a die from the draft pool with a die from the round track.
 *
 * @author giorgiolabate
 */
public class DiceSwappingManager extends InputEventManager {

    /**
     * The index of the die in the Draft Pool
     */
    private int index = -1;

    /**
     * The coordinates for the die in the Round Track.
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
    public DiceSwappingManager(ClientView view, CliPrinter output) {
        super(view, output);
    }

    /**
     * Handles the input entered by the user.
     * <p>After collecting the index of the die in the draft pool and the coordinates of the
     * die in the round track, it calls the proper handler of {@link ClientView}.</p>
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
                } else if (row == -1) {
                    row = choice;
                } else if (col == -1) {
                    col = choice;
                    getView().handleToolCardUsage(index, new Coordinates(row, col), true);
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
            getOutput().printRoundTrack(getRoundTrack());
            getOutput().println("Enter the index of the die in the Draft Pool you want to swap:");
        } else if (row == -1) {
            getOutput().println("Enter the coordinates of the die in the Round Track you want to swap:");
            getOutput().print("Row (starting from 0): ");

        } else if (col == -1) {
            getOutput().print("Col (starting from 0): ");
        }
    }
}
