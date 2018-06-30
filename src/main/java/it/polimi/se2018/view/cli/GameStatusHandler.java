package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;


/**
 * It is a subHandler for the turn management. It handles the input in the
 * case in which the player has chosen to ask for some information about
 * the game status.
 */
public class GameStatusHandler extends InputEventManager{

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
    public GameStatusHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
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
            switch (choice) {
                case 1:
                    if (!isSinglePlayer()) output.printPatterns(getPlayers());
                    else output.printPattern(getPattern());
                    manager.setSubHandler(null);
                    break;
                case 2:
                    output.printRoundTrack(getRoundTrack());
                    manager.setSubHandler(null);
                    break;
                case 3:
                    output.printPrivateObjectiveCards(getPrivateCards());
                    manager.setSubHandler(null);
                    break;
                case 4:
                    output.printPublicObjectiveCards(getPublicCards());
                    manager.setSubHandler(null);
                    break;
                case 5:
                    output.printToolCards(getToolCards());
                    manager.setSubHandler(null);
                    break;
                default:
                    showError();
                    break;
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
            output.printTextNewLine("What do you want to see? Enter\n" +
                    "1 for Patterns\n" +
                    "2 for Round Track\n" +
                    "3 for Private Objective Cards\n" +
                    "4 for Public Objective Cards\n" +
                    "5 for Tool Cards"
            );
    }
}
