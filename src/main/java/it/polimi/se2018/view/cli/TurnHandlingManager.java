package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the central phase of the game: it handles the
 * evolution of a turn.
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the displayTurnView is invoked.
 */

public class TurnHandlingManager extends InputEventManager {

    /**
     * This manager has a subHandler for each of the four actions the user
     * can choose to perform: place a die, use a Tool Card, ask for information,
     * skip the turn.
     */
    private InputEventManager subHandler;

    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public TurnHandlingManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network. In this case the real handling will be delegated to the
     * current subHandler: this methods only set the correct subHandler with the
     * {@code handleFirstMove} and {@code handleSecondMove} invocations if the user
     * has still to choose the action otherwise it delegates.
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        if(isNotMyTurn()){
            return;
        }
        if (!hasPlacedDie() && !hasUsedToolCard()) {
            handleFirstMove(input);
        } else {
            handleSecondMove(input);
        }
    }

    /**
     * Handles the first Move in the Turn.
     * @param input The String inserted by the user.
     */
    private void handleFirstMove(String input){
        if (subHandler == null) {
            handleFirstChoice(input);
        }
        else subHandler.handle(input);
    }

    /**
     * Handles the second Move in the Turn.
     * @param input The String inserted by the user.
     */
    private void handleSecondMove(String input){
        if (subHandler == null) {
            if (!hasPlacedDie() && hasUsedToolCard()) {
                handleSecondChoiceDie(input);
            }
            if (!hasUsedToolCard() && hasPlacedDie()) {
                handleSecondChoiceCard(input);
            }
        }
        else subHandler.handle(input);
    }

     /**
      * Handle the first move's choice setting the correct subHandler.
      *
      * @param input The action selected by the user between all the possible
      *              actions left.
      */
    private void handleFirstChoice(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            switch (choice) {
                case 1:
                    setSubHandler(new DiePlacementHandler(view, output, this));
                    break;
                case 2:
                    setSubHandler(new ToolCardHandler(view, output, this));
                    break;
                case 3:
                    setSubHandler(new SkipTurnHandler(view, output, this));
                    break;
                case 4:
                    setSubHandler(new GameStatusHandler(view, output, this));
                    break;
                default:
                    showError();
                    break;
            }
        }
        catch (NumberFormatException ex) {
            showError();
        }
    }

    /**
     * Handle the second move's choice of the user when he has already placed a die
     * setting the correct subHandler.
     *
     * @param input The action selected by the user between all the possible
     *                actions left.
     */
    private void handleSecondChoiceCard(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            switch (choice) {
                case 1:
                    setSubHandler(new ToolCardHandler(view, output, this));
                    break;
                case 2:
                    setSubHandler(new SkipTurnHandler(view, output, this));
                    break;
                case 3:
                    setSubHandler(new GameStatusHandler(view, output, this));
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
     * Handle the second move's choice of the user when he has already used a Tool Card
     * setting the correct SubHandler.
     *
     * @param input The action selected by the user between all the possible
     *              actions left.
     */
    private void handleSecondChoiceDie(String input) { //bloccante
        try {
            int choice = Integer.parseInt(input.trim());
            switch (choice) {
                case 1:
                    setSubHandler(new DiePlacementHandler(view, output, this));
                    break;
                case 2:
                    setSubHandler(new ToolCardHandler(view, output, this));
                    break;
                case 3:
                    setSubHandler(new GameStatusHandler(view, output, this));
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
        if (isNotMyTurn()) {
            output.printTextNewLine("It's " + getDataOrganizer().getNextTurn().getPlayerName() + "'s turn, please wait");
            return;
        }
        if (!hasPlacedDie() && !hasUsedToolCard()) { //non ha ancora fatto niente
            if (subHandler == null) {
                showFirstActionPrompt();
            } else {
                subHandler.showPrompt();
            }
        }
        else {
            if (subHandler == null) {
                showSecondActionPrompt();
            } else {
                subHandler.showPrompt();
            }
        }
    }

    /**
     * Shows the first move's prompt.
     */
    private void showFirstActionPrompt() {
        output.printTextNewLine("It's your turn, enter:\n "
                + "1 to place a die\n"
                + "2 to use a tool card\n"
                + "3 to skip your turn\n"
                + "4 to see the game status");
    }

    /**
     * Shows the second move's prompt.
     */
    private void showSecondActionPrompt() {
        if (hasPlacedDie()) {
            output.printTextNewLine("It's your turn, you have already placed a die, enter:\n"
                    + "1 to use a tool card\n"
                    + "2 to skip your turn\n"
                    + "3 to see the game status");
        } else {
            output.printTextNewLine("It's your turn, you have already used a tool card, enter:\n"
                    + "1 to place a die\n"
                    + "2 to skip your turn\n"
                    + "3 to see the game status");
        }

    }

    /**
     * Helper method to verify if it is the user's turn.
     *
     * @return {@code true} if now it is the user's turn, {@code false}
     * otherwise.
     */
    private boolean isNotMyTurn() {
        return !view.getPlayerName().equals(getDataOrganizer().getNextTurn().getPlayerName());
    }

    /**
     * Setter for the subHandler.
     * @param subHandler The correct subHandler for the turn handling set by the handle
     *                   method.
     */
    public void setSubHandler(InputEventManager subHandler) {
        this.subHandler = subHandler;
    }

}

