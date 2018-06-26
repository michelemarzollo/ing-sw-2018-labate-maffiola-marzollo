package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

public class ToolCardPlacementManager extends InputEventManager {

    private int dieIndex;
    private int row;
    private int col;

    private CliInput gatherer;

    public ToolCardPlacementManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        dieIndex = -1;
        row = -1;
        col = -1;
        this.gatherer = gatherer;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input);
            if (dieIndex == -1) {
                handleDieIndex(choice);
            } else if (row == -1) {
                handleRow(choice);
            } else if (col == -1) {
                handleCol(choice);
            }
        } catch (NumberFormatException ex) {
            showError();
        }
    }


    private void handleDieIndex(int choice) {
        if (choice < 0) {
            showError();
        } else {
            dieIndex = choice;
        }
    }

    private void handleRow(int choice) {
        if (choice < 0) {
            showError();
        } else {
            row = choice;
        }
    }

    private void handleCol(int choice) {
        if (choice < 0) {
            showError();
        } else {
            col = choice;
            view.handleToolCardUsage(dieIndex, new Coordinates(row, col));
            gatherer.setManager(new TurnHandlingManager(view, output));
        }
    }


    @Override
    public void showPrompt() {
        if (dieIndex == -1) {
            output.printDraftPool(getDraftPool());
            output.printTextNewLine("Enter the index of the die:");
        } else if (row == -1) {
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the coordinates for your placement:");
            output.printText("Row (starting from 0): ");
        } else if (col == -1) {
            output.printText("Col (starting from 0): ");
        }
    }
}
