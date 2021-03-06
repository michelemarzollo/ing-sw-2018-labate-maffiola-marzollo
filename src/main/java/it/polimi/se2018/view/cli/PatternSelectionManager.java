package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to select a pattern among candidates.
 *
 * @author giorgiolabate
 */
public class PatternSelectionManager extends InputEventManager {

    /**
     * Flag to indicate if the pattern has already been chosen.
     */
    private boolean alreadyChosen = false;

    /**
     * Flag to indicate if the player has already been prompted.
     */
    private boolean alreadyPrompted = false;

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public PatternSelectionManager(ClientView view, CliPrinter output) {
        super(view, output);
    }

    /**
     * Handles the input entered by the user.
     * <p>Calls the proper handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        if (!alreadyChosen) {
            alreadyChosen = true;
            getView().handlePatternSelection(input);
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (alreadyChosen || alreadyPrompted)
            return;

        int playerIndex = findPlayerIndex(getView().getPlayerName(), getDataOrganizer().getGameSetup().getPlayers());
        getOutput().printPatternSelection(getDataOrganizer().getGameSetup().getCandidates()[playerIndex]);
        getOutput().println("Enter the name of the pattern you want to choose:");
        alreadyPrompted = true;
    }

    /**
     * Resets the input manager.
     */
    @Override
    public void reset() {
        alreadyChosen = false;
        alreadyPrompted = false;
    }
}
