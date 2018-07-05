package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;


/**
 * Input manager used to select a die from the draft pool.
 *
 * @author giorgiolabate
 */
public class SelectDieManager extends InputEventManager {

    /**
     * The index of the chosen die.
     */
    private int index = -1;

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public SelectDieManager(ClientView view, CliPrinter output) {
        super(view, output);
    }


    /**
     * Handles the input entered by the user.
     * <p>After collecting the selected index, calls the proper
     * handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        try {
            if (index == -1) {
                int choice = Integer.parseUnsignedInt(input.trim());
                getView().handleToolCardUsage(choice);
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
        if (index == -1) {
            getOutput().printDraftPool(getDraftPool());
            getOutput().println("Enter the index of the die you wish to use: \n");
        }
    }


}
