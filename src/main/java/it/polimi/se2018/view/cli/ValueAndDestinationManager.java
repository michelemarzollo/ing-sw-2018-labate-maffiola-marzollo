package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

public class ValueAndDestinationManager extends InputEventManager{

    private int value;
    private int row;
    private int col;

    private CliInput gatherer;

    public ValueAndDestinationManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        this.value = -1;
        this.row = -1;
        this.col = -1;
        this.gatherer = gatherer;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (value == -1) {
                    value = choice;
                } else if (row == -1) {
                    row = choice;
                } else if (col == -1) {
                    col = choice;
                    view.handleToolCardUsage(new Coordinates(row, col), value);
                    gatherer.setManager(new TurnHandlingManager(view, output));

                }
            } else showError();
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    @Override
    public void showPrompt() {
        if(value == -1){
            output.printPattern(getPattern());
            output.printDraftPool(getDraftPool());
            output.printTextNewLine("The index of your chosen die in the draft pool is" +
                    getDataOrganizer().getNextTurn().getForcedSelectionIndex());
            output.printTextNewLine("Enter the value you want to assign to the chosen die:");
        }
        else if(row == -1){
            output.printTextNewLine("Enter the coordinates of the space in your pattern where you want to place the die:");
            output.printText("Row (starting from 0): ");

        }
        else if(col == -1){
            output.printText("Col (starting from 0): ");
        }
    }
}
