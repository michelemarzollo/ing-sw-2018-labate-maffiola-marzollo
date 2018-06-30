package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * It is a subHandler for the turn management. It handles the input in the
 * case in which the player has chosen to skip his turn.
 */
public class SkipTurnHandler extends InputEventManager{

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
    public SkipTurnHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
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
            int choice = Integer.parseInt(input.trim());
            if (choice == 1) {
                manager.setSubHandler(null);
                view.handleEndTurn();
            } else {
                manager.setSubHandler(null);
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
            output.printTextNewLine("You chose to skip your turn, enter:\n" +
                    "1 to confirm\n" +
                    "Any other number to go back");
    }
}
