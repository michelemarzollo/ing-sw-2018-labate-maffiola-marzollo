package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

public class PatternSelectionManager extends InputEventManager{

    private String pattern;

    public PatternSelectionManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    @Override
    public void handle(String input) {
        if(pattern == null) {
            pattern = input;
            view.handlePatternSelection(pattern);
        }
    }

    @Override
    public void showPrompt() {
        int playerIndex = findPlayerIndex(view.getPlayerName(), getDataOrganizer().getGameSetup().getPlayers());
        output.printPatternSelection(getDataOrganizer().getGameSetup().getCandidates()[playerIndex]);
        output.printTextNewLine("Enter the name of the pattern you want to choose:");
    }
}
