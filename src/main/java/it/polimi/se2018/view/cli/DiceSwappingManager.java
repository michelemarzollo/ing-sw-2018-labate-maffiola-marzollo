package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the use of the Tool Cards that needs as a
 * parameter the index of the die in the Draft Pool and the coordinates
 * of the die in the Round Track (special manager for the Lens Cutter).
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the moveDice method is invoked.
 */
public class DiceSwappingManager extends InputEventManager {

    /**
     * The index of the die in the Draft Pool
     */
    private int index;

    /**
     * The coordinates for the die in the Round Track.
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
    public DiceSwappingManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        this.index = -1;
        this.row = -1;
        this.col = -1;
        this.gatherer = gatherer;
    }


    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents here the coordinates
     *              or the index.
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
                    view.handleToolCardUsage(index, new Coordinates(row, col), true);
                    gatherer.setManager(new TurnHandlingManager(view, output));
                }
            } else showError();
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
        if(index == -1){
            output.printDraftPool(getDraftPool());
            output.printRoundTrack(getRoundTrack());
            output.printTextNewLine("Enter the index of the die in the Draft Pool you want to swap:");
        }
        else if(row == -1){
            output.printTextNewLine("Enter the coordinates of the die in the Round Track you want to swap:");
            output.printText("Row (starting from 0): ");

        }
        else if(col == -1){
            output.printText("Col (starting from 0): ");
        }
    }
}
