package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the use of the Tool Cards that needs as a
 * parameter the index for the chosen die and the coordinates
 * for the placement of the die (special manager for the Grozing Pliers).
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the askIncrement method is invoked.
 */
public class DieIncrementManager extends InputEventManager{

    /**
     * Index of the chosen die in the Draft Pool.
     */
    private int index;

    /**
     * Represents the choice about incrementing or decrementing the die's value.
     */
    private int value;


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
    public DieIncrementManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        index = -1;
        value = -1;
        this.gatherer = gatherer;
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents here the index or his
     *              choice about incrementing or decrementing the die's value.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (index == -1) {
                    index = choice;
                } else if (value == -1) {
                    if (choice == 1) {
                        value = choice;
                        view.handleToolCardUsage(index, true);
                        gatherer.setManager(new TurnHandlingManager(view, output));
                    } else if (choice == 2) {
                        value = choice;
                        view.handleToolCardUsage(index, false);
                        gatherer.setManager(new TurnHandlingManager(view, output));
                    } else showError();
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
            output.printTextNewLine("Enter the index of the die you wish to use: \n");
        }
        else if(value == -1){
            output.printTextNewLine("Enter 1 to increment the value of the selected die, 2 to decrement it:");
        }
    }
}
