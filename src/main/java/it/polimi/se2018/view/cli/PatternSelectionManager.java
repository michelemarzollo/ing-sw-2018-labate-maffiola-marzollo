package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the  phase of the game when the user has to choose
 * his Pattern.
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the askPattern is invoked.
 */
public class PatternSelectionManager extends InputEventManager{

    /**
     * The name of the chosen Pattern.
     */
    private String pattern;

    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public PatternSelectionManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents the name
     *              of the Pattern.
     */
    @Override
    public void handle(String input) {
        if(pattern == null) {
            pattern = input;
            view.handlePatternSelection(pattern);
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt() {
        int playerIndex = findPlayerIndex(view.getPlayerName(), getDataOrganizer().getGameSetup().getPlayers());
        output.printPatternSelection(getDataOrganizer().getGameSetup().getCandidates()[playerIndex]);
        output.printTextNewLine("Enter the name of the pattern you want to choose:");
    }
}
