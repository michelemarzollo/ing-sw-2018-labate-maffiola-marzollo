package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;


public class GameStatusHandler extends InputEventManager{

    private TurnHandlingManager manager;

    public GameStatusHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
    }

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
