package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

public class DieIncrementManager extends InputEventManager{

    private int index;
    private int value;

    private CliInput gatherer;

    public DieIncrementManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        index = -1;
        value = -1;
        this.gatherer = gatherer;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (index == -1) {
                    index = choice;
                } else if (value == -1) {
                    if (choice == 1) {
                        view.handleToolCardUsage(index, true);
                        gatherer.setManager(new TurnHandlingManager(view, output));
                    } else if (choice == 2) {
                        view.handleToolCardUsage(index, false);
                        gatherer.setManager(new TurnHandlingManager(view, output));
                    } else showError();
                }
            } else showError();
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    @Override
    public void showPrompt() {
        if(index == -1){
            output.printDraftPool(getDraftPool());
            output.printTextNewLine("Enter the index of the die you wish to use: \n");
        }
        else if(value == -1){
            output.printTextNewLine("Enter 1 to increment the value of the selected die, 2 to decrement it:");
        }
    }
}
