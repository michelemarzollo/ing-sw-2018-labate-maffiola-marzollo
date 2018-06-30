package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;


/**
 * This manager handles the use of the Tool Cards that needs as a
 * parameter the index of a Die from the DraftPool (for example the Flux Brush).
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the selectDie method is invoked.
 */
public class SelectDieManager extends InputEventManager{

    /**
     * The index of the chosen die.
     */
    private int index;

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
    public SelectDieManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        index = -1;
        this.gatherer = gatherer;
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents here the index
     *              of the die.
     */
    @Override
    public void handle(String input) {
        try {
            if (index == -1) {
                int choice = Integer.parseInt(input.trim());
                if (choice >= 0) {
                    view.handleToolCardUsage(choice);
                    gatherer.setManager(new TurnHandlingManager(view, output));
                } else {
                    showError();
                }
            }
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
        if(index == -1) {
            output.printDraftPool(getDraftPool());
            output.printTextNewLine("Enter the index of the die you wish to use: \n");
        }
    }


}
