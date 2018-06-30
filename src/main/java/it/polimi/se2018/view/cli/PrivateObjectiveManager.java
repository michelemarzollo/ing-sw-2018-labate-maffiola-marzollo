package it.polimi.se2018.view.cli;


import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the final phase of the game when the user
 * has to choose his Private Objective Card.
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the askPrivateObjective is invoked.
 */
public class PrivateObjectiveManager extends InputEventManager {

    /**
     * The name of the chosen Private Objective Card.
     */
    private String name;

    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public PrivateObjectiveManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents his choice
     *              for the Private Card.
     */
    @Override
    public void handle(String input) {
        if (name == null) {
            name = input.trim();
            view.handlePrivateSelection(name);
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        if (name == null) {
            output.printPrivateObjectiveCards(getPrivateCards());
            output.printTextNewLine("Enter the name of the Private Objective card you want to choose:");
        }
    }

}
