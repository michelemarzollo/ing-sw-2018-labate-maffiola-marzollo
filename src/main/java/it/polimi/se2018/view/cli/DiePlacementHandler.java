package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * It is a subHandler for the turn management. It handles the input in the
 * case in which the player has chosen to place a die.
 */
public class DiePlacementHandler extends  InputEventManager{

    /**
     * Indicates whether the user has confirmed to perform this action or not.
     */
    private boolean confirmDone;

    /**
     * The chosen index of the die in the DraftPool that has to be placed.
     */
    private int dieIndex;

    /**
     * The row coordinate for the placement.
     */
    private int row;

    /**
     * The column coordinate for the placement.
     */
    private int col;

    /**
     * Reference to the manager of the turn. It's used to set the
     * subHandler to null when the task has been accomplished.
     */
    private TurnHandlingManager manager;

    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     * @param manager The TurnHandlingManager.
     */
    public DiePlacementHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
        dieIndex = -1;
        row = -1;
        col = -1;
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents his choice.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input);
            if (!confirmDone) {
                handleConfirm(choice);
            } else if (dieIndex == -1) {
                handleDieIndex(choice);
            } else if (row == -1) {
                handleRow(choice);
            } else if (col == -1) {
                handleCol(choice);
            }
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    /**
     * Handles the first choice: the confirmation of the will of placing a die.
     * @param choice The choice performed by the user.
     */
    private void handleConfirm(int choice){
        if(choice == 1){
            setConfirmDone();
        }
        else{
            manager.setSubHandler(null);
        }
    }

    /**
     * Handles the second choice: the index of the die in the DraftPool.
     * @param choice The choice performed by the user.
     */
    private void handleDieIndex(int choice){
        if(choice < 0){
            showError();
        }
        else {
            dieIndex = choice;
        }
    }

    /**
     * Handles the third choice: the row for the placement
     * @param choice The choice performed by the user.
     */
    private void handleRow(int choice) {
        if(choice < 0){
            showError();
        }
        else {
            row = choice;
        }
    }

    /**
     * Handles the fourth choice: the column for the placement
     * @param choice The choice performed by the user.
     */
    private void handleCol(int choice) {
        if(choice < 0){
            showError();
        }
        else{
            col = choice;
            manager.setSubHandler(null);
            view.handlePlacement(dieIndex, new Coordinates(row, col));
        }
    }



    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        if(!confirmDone){
            showConfirmPrompt();
        }
        else{
            if(dieIndex == -1){
                output.printDraftPool(getDraftPool());
                output.printTextNewLine("Enter the index of the die:");
            }
            else if(row == -1){
                output.printPattern(getPattern());
                output.printTextNewLine("Enter the coordinates for your placement:");
                output.printText("Row (starting from 0): ");
            }
            else if(col == -1){
                output.printText("Col (starting from 0): ");

            }
        }
    }

    /**
     * Shows the confirmation prompt.
     */
    private void showConfirmPrompt() {
        output.printTextNewLine("You chose to place a die, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }

    /**
     * Setter for the confirmation.
     */
    private void setConfirmDone() {
        this.confirmDone = true;
    }
}
