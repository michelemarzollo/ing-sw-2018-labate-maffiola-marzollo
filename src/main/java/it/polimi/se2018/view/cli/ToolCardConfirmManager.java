package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to confirm the usage of a tool card.
 *
 * @author giorgiolabate
 */
public class ToolCardConfirmManager extends InputEventManager {

    /**
     * Flag to indicate if the player has already confirmed.
     */
    private boolean gotConfirmation = false;

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public ToolCardConfirmManager(ClientView view, CliPrinter output) {
        super(view, output);
    }


    /**
     * Handles the input entered by the user.
     * <p>If the player confirmed, calls the proper handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice == 1) {
                gotConfirmation = true;
                getView().handleToolCardUsage();
            }

        } catch (NumberFormatException ex) {
            showError();
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        if (!gotConfirmation)
            getOutput().print("Do you want to activate the Tool Card effect? Enter:\n" +
                    "1 to confirm\n" +
                    "Any other number to go back");
    }
}
