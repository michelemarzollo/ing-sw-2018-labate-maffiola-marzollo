package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;


public class SelectDieManager extends InputEventManager{

    private int index;
    private  CliInput gatherer;

    public SelectDieManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        index = -1;
        this.gatherer = gatherer;
    }

    @Override
    public void handle(String input) {
        try {
            if (index == -1) {
                int choice = Integer.parseInt(input.trim());
                if (choice >= 0) {
                    view.handleToolCardUsage(choice);
                    gatherer.setManager(new TurnHandlingManager(view, output));
                } else {
                    showError();
                }
            }
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    @Override
    public void showPrompt() {
        if(index == -1) {
            output.printDraftPool(getDraftPool());
            output.printTextNewLine("Enter the index of the die you wish to use: \n");
        }
    }


}
