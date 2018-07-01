package it.polimi.se2018.view.cli;


import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to select a private objective card among candidates.
 */
public class PrivateObjectiveManager extends InputEventManager {

    /**
     * The name of the chosen Private Objective Card.
     */
    private String name;

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public PrivateObjectiveManager(ClientView view, CliPrinter output) {
        super(view, output);
    }


    /**
     * Handles the input entered by the user.
     * <p>After collecting the selected private objective card, calls the
     * proper handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        if (name == null) {
            name = input.trim();
            getView().handlePrivateSelection(name);
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (name == null) {
            getOutput().printPrivateObjectiveCards(getPrivateCards());
            getOutput().println("Enter the name of the Private Objective card you want to choose:");
        }
    }

    /**
     * Resets the input manager.
     */
    @Override
    public void reset(){
        name = null;
    }
}
