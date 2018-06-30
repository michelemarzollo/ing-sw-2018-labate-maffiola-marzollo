package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the use of the Tool Cards that needs as a
 * parameter the coordinates for moving dice on the Draft Pool
 * (for example the 'Tap Wheel').
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the moveDice method is invoked.
 */
public class MoveDiceManager extends InputEventManager{

    /**
     * Indicates the number of dice that have to be moved.
     */
    private int amount;

    /**
     * Indicates whether it is mandatory to move both dice (in the
     * case in which the {@code amount} is equals to two) or not.
     */
    private boolean moveAll;

    /**
     * These eight int will contain the coordinates for the moves.
     */
    private int firstSourceRow;
    private int firstSourceCol;
    private int firstDestRow;
    private int firstDestCol;
    private int secondSourceRow;
    private int secondSourceCol;
    private int secondDestRow;
    private int secondDestCol;

    /**
     * Indicates whether the user has chosen to move both dice
     * (if he has this possibility) or not.
     */
    private boolean hasConfirmed;

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
    public MoveDiceManager(ClientView view, CliImagePrinter output, CliInput gatherer, int amount, boolean moveAll) {
        super(view, output);
        this.amount = amount;
        this.moveAll = moveAll;
        this.gatherer = gatherer;
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents here the coordinates
     *              or the confirm.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (firstSourceRow == -1) {
                    firstSourceRow = choice;
                } else if (firstSourceCol == -1) {
                    firstSourceCol = choice;
                } else if (firstDestRow == -1) {
                    firstDestRow = choice;
                } else if (firstDestCol == -1) {
                    firstDestCol = choice;
                    if (amount == 1) {
                        Coordinates[] source = {new Coordinates(firstSourceRow, firstSourceCol)};
                        Coordinates[] destination = {new Coordinates(firstDestRow, firstDestCol)};
                        view.handleToolCardUsage(source, destination);
                        gatherer.setManager(new TurnHandlingManager(view, output));
                    } else {
                        handleSecondMove(choice);
                    }
                }
            } else showError();
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    /**
     * Handles the second interaction for the second movement on the Pattern.
     * @param choice The input inserted by the user that represents here the coordinates
     *               or the confirm.
     */
    private void handleSecondMove(int choice) {
        if(!moveAll && !hasConfirmed){
            handleConfirm(choice);
        }

        if(secondSourceRow == -1){
            secondSourceRow = choice;
        }
        else if(secondSourceCol == -1){
            secondSourceCol = choice;
        }
        else if(secondDestRow == -1){
            secondDestRow = choice;
        }
        else if(secondDestCol == -1){
            secondDestCol = choice;
            Coordinates[] sources = new Coordinates[2];
            Coordinates[] destinations = new Coordinates[2];
            sources[0] = new Coordinates(firstSourceRow, firstSourceCol);
            sources[1] = new Coordinates(secondSourceRow, secondSourceCol);
            destinations[0] = new Coordinates(firstDestRow, firstDestCol);
            destinations[1] = new Coordinates(secondDestRow, secondDestCol);
            view.handleToolCardUsage(sources, destinations);
            gatherer.setManager(new TurnHandlingManager(view, output));

        }
    }

    /**
     * Handles the confirm in the case in which is it possible to move two dice.
     * @param choice The choice inserted by the user.
     */
    private void handleConfirm(int choice) {
        if(choice == 1){
            setHasConfirmed();
        }
        else if(choice == 0){
            Coordinates[] source = {new Coordinates(firstSourceRow, firstSourceCol)};
            Coordinates[] destination = {new Coordinates(firstDestRow, firstDestCol)};
            view.handleToolCardUsage(source, destination);
            gatherer.setManager(new TurnHandlingManager(view, output));
        }
    }


    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        if(amount == 1){
            showOneMovePrompt();
        }
        else{
            showOneMovePrompt();
            showTwoMovePrompt();
        }
    }

    /**
     * Shows the prompt for the Tool Cards that allows only to move one die or
     * for the first step for those that allows to move two dice.
     */
    private void showOneMovePrompt() {
        if(firstSourceRow == -1){
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the source coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(firstSourceCol == -1){
            output.printText("Col (starting from 0): ");
        }
        else if(firstDestRow == -1){
            output.printTextNewLine("Enter the destination coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(firstDestCol == -1){
            output.printText("Col (starting from 0): ");
        }
    }

    /**
     * Shows the prompt for the second movement.
     */
    private void showTwoMovePrompt() {
        if(!moveAll && !hasConfirmed){
            output.printTextNewLine("Do you want to move another die? Enter 1 if so, 0 otherwise");
        }
        if(secondSourceRow == -1){
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the second source coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(secondSourceCol == -1){
            output.printText("Col (starting from 0): ");

        }
        else if(secondDestRow == 1){
            output.printTextNewLine("Enter the second destination coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(secondDestCol == -1){
            output.printText("Col (starting from 0): ");

        }
    }

    /**
     * Setter for the confirm.
     */
    private void setHasConfirmed() {
        this.hasConfirmed = true;
    }
}
