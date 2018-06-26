package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

public class DifficultyManager extends InputEventManager {

    private int difficulty;

    public DifficultyManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (difficulty == -1) {
                    difficulty = choice;
                    view.handleDifficultySelection(difficulty);
                }
            }
            else {
                showError();
            }
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    @Override
    public void showPrompt() {
        if(difficulty == -1){
            output.printTextNewLine("Choose the level of difficulty you wish to play. Enter a value between 1 and 5:");
        }
    }
}
