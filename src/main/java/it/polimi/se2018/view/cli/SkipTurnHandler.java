package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

public class SkipTurnHandler extends InputEventManager{

    private TurnHandlingManager manager;

    public SkipTurnHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice == 1) {
                manager.setSubHandler(null);
                view.handleEndTurn();
            } else {
                manager.setSubHandler(null);
            }
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    @Override
    public void showPrompt() {
            output.printTextNewLine("You chose to skip your turn, enter:\n" +
                    "1 to confirm\n" +
                    "Any other number to go back");
    }
}
