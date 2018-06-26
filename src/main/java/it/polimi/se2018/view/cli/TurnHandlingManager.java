package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

public class TurnHandlingManager extends InputEventManager {

    private InputEventManager subHandler;

    public TurnHandlingManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    @Override
    public void handle(String input) {
        if(isNotMyTurn()){
            return;
        }
        if (!hasPlacedDie() && !hasUsedToolCard()) { //non ha ancora fatto niente
            handleFirstMove(input);
        } else {
            handleSecondMove(input);
        }
    }


    private void handleFirstMove(String input){
        if (subHandler == null) {
            handleFirstChoice(input);
        }
        else subHandler.handle(input);
    }

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
     * Handle the first move of the user.
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
     * Handle the second move of the user when he has already placed a die.
     *
     * @param input The action selected by the user between all the possible
     *                actions left.
     */
    private void handleSecondChoiceCard(String input) { //bloccante
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
     * Handle the second move of the user when he has already used a Tool Card.
     *
     * @param input The action selected by the user between all the possible
     *                actions left.
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

    private void showFirstActionPrompt() {
        output.printTextNewLine("It's your turn, enter:\n "
                + "1 to place a die\n"
                + "2 to use a tool card\n"
                + "3 to skip your turn\n"
                + "4 to see the game status");
    }

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

    public void setSubHandler(InputEventManager subHandler) {
        this.subHandler = subHandler;
    }

}

