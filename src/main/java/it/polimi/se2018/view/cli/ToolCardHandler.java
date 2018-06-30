package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * It is a subHandler for the turn management. It handles the input in the
 * case in which the player has chosen to use a Tool Card.
 */
public class ToolCardHandler extends InputEventManager {

    /**
     * Reference to the manager of the turn. It's used to set the
     * subHandler to null when the task has been accomplished.
     */
    private TurnHandlingManager manager;

    /**
     * Indicates whether the user has confirmed to perform this action or not.
     */
    private boolean confirmDone;

    /**
     * Name of the chosen Tool Card.
     */
    private String name;

    /**
     * Index for the die in the Draft Pool that has to be spent in Single Player mode.
     */
    private int sacrificeIndex;

    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     * @param manager The TurnHandlingManager.
     */
    public ToolCardHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
        sacrificeIndex = -1;
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
        if(!confirmDone){
            try {
                int choice = Integer.parseInt(input.trim());
                handleConfirm(choice);
            }
            catch (NumberFormatException ex){
                showError();
            }
        }
        else if(name == null){
            handleName(input);
        }
        else if(sacrificeIndex == -1 && isSinglePlayer()) {
            try {
                int choice = Integer.parseInt(input.trim());
                handleIndex(choice);
            }
            catch (NumberFormatException ex){
                showError();
            }
        }
    }

    /**
     * Handles the first choice: the confirmation of the will of placing a die.
     * @param choice The choice performed by the user.
     */
    private void handleConfirm(int choice) {
        if(choice == 1){
            setConfirmDone();
        }
        else{
            manager.setSubHandler(null);
        }
    }

    /**
     * Handles the second choice: the name of the Tool Card.
     * @param input The choice performed by the user (the name entered).
     */
    private void handleName(String input) {
        name = input;
        if (!isSinglePlayer()) {
            manager.setSubHandler(null);
            view.handleToolCardSelection(name);
        }
    }

    /**
     * Handles the third choice: the index of the die that has to be spent.
     * @param choice The choice performed by the user.
     */
    private void handleIndex(int choice) {
        if(choice < 0){
            showError();
        }
        else{
            manager.setSubHandler(null);
            view.handleToolCardSelection(name, sacrificeIndex);
        }
    }


    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        if (!confirmDone) {
            showConfirmPrompt();
        }
        else if (name == null) {
            output.printToolCards(getToolCards());
            output.printTextNewLine("Enter the name of the Tool Card:");
        }
        else if(sacrificeIndex == -1 && isSinglePlayer()){
                output.printDraftPool(getDraftPool());
                output.printTextNewLine("Enter the index of the die you want to spend to use the tool card:");
        }

    }

    /**
     * Shows the confirmation prompt.
     */
    private void showConfirmPrompt() {
        output.printText("You chose to use a Tool Card, enter:\n" +
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
