package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the use of the Tool Cards that needs no
 * parameters (special manager for the Glazing Hammer).
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the askIncrement method is invoked.
 */
public class ToolCardConfirmManager extends InputEventManager{


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
    public ToolCardConfirmManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        this.gatherer = gatherer;
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents here the confirmation
     *              about the usage of the Tool Card.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if(choice == 1){
                view.handleToolCardUsage();
            }
            gatherer.setManager(new TurnHandlingManager(view, output));
        }
        catch(NumberFormatException ex){
            showError();
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        output.printText("Do you want to activate the Tool Card effect? Enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }
}
