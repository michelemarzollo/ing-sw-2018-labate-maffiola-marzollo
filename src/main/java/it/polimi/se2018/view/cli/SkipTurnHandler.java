package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to let the player ends its turn early.
 */
public class SkipTurnHandler extends InputEventManager {

    /**
     * Reference to the manager of the turn.
     */
    private final TurnHandlingManager manager;

    /**
     * Constructor of the class.
     *
     * @param view    The view to which this manager is bounded.
     * @param output  The output destination where the prompts of this manager
     *                are shown.
     * @param manager The TurnHandlingManager.
     */
    public SkipTurnHandler(ClientView view, CliPrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
    }


    /**
     * Handles the input entered by the user.
     * <p>After asking the user confirmation, calls the proper handler
     * in {@link ClientView} if tha player confirmed.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice == 1)
                getView().handleEndTurn();
            else
                manager.setSubHandler(null);

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
        getOutput().println("You chose to skip your turn, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }
}
