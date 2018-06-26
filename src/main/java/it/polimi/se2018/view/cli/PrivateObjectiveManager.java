package it.polimi.se2018.view.cli;


import it.polimi.se2018.view.ClientView;

public class PrivateObjectiveManager extends InputEventManager {

    private String name;

    public PrivateObjectiveManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    @Override
    public void handle(String input) {
        if (name == null) {
            name = input.trim();
            view.handlePrivateSelection(name);
        }
    }

    @Override
    public void showPrompt() {
        if (name == null) {
            output.printPrivateObjectiveCards(getPrivateCards());
            output.printTextNewLine("Enter the name of the Private Objective card you want to choose:");
        }
    }

}
