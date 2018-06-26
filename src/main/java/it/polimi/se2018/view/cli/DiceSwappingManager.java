package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

public class DiceSwappingManager extends InputEventManager {


    private int index;
    private int row;
    private int col;

    private CliInput gatherer;

    public DiceSwappingManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        this.index = -1;
        this.row = -1;
        this.col = -1;
        this.gatherer = gatherer;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (index == -1) {
                    index = choice;
                } else if (row == -1) {
                    row = choice;
                } else if (col == -1) {
                    col = choice;
                    view.handleToolCardUsage(index, new Coordinates(row, col), true);
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
        if(index == -1){
            output.printDraftPool(getDraftPool());
            output.printRoundTrack(getRoundTrack());
            output.printTextNewLine("Enter the index of the die in the Draft Pool you want to swap:");
        }
        else if(row == -1){
            output.printTextNewLine("Enter the coordinates of the die in the Round Track you want to swap:");
            output.printText("Row (starting from 0): ");

        }
        else if(col == -1){
            output.printText("Col (starting from 0): ");
        }
    }
}
