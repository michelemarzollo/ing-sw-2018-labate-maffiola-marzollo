package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

public class MoveDiceManager extends InputEventManager{

    private int amount;
    private boolean moveAll;
    private int firstSourceRow;
    private int firstSourceCol;
    private int firstDestRow;
    private int firstDestCol;
    private int secondSourceRow;
    private int secondSourceCol;
    private int secondDestRow;
    private int secondDestCol;
    private boolean hasConfirmed;

    private CliInput gatherer;

    public MoveDiceManager(ClientView view, CliImagePrinter output, CliInput gatherer, int amount, boolean moveAll) {
        super(view, output);
        this.amount = amount;
        this.moveAll = moveAll;
        this.gatherer = gatherer;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 0) {
                if (firstSourceRow == -1) {
                    firstSourceRow = choice;
                } else if (firstSourceCol == -1) {
                    firstSourceCol = choice;
                } else if (firstDestRow == -1) {
                    firstDestRow = choice;
                } else if (firstDestCol == -1) {
                    firstDestCol = choice;
                    if (amount == 1) {
                        Coordinates[] source = {new Coordinates(firstSourceRow, firstSourceCol)};
                        Coordinates[] destination = {new Coordinates(firstDestRow, firstDestCol)};
                        view.handleToolCardUsage(source, destination);
                        gatherer.setManager(new TurnHandlingManager(view, output));
                    } else {
                        handleSecondMove(choice);
                    }
                }
            } else showError();
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    private void handleSecondMove(int choice) {
        if(!moveAll && !hasConfirmed){
            handleConfirm(choice);
        }

        if(secondSourceRow == -1){
            secondSourceRow = choice;
        }
        else if(secondSourceCol == -1){
            secondSourceCol = choice;
        }
        else if(secondDestRow == -1){
            secondDestRow = choice;
        }
        else if(secondDestCol == -1){
            secondDestCol = choice;
            Coordinates[] sources = new Coordinates[2];
            Coordinates[] destinations = new Coordinates[2];
            sources[0] = new Coordinates(firstSourceRow, firstSourceCol);
            sources[1] = new Coordinates(secondSourceRow, secondSourceCol);
            destinations[0] = new Coordinates(firstDestRow, firstDestCol);
            destinations[1] = new Coordinates(secondDestRow, secondDestCol);
            view.handleToolCardUsage(sources, destinations);
            gatherer.setManager(new TurnHandlingManager(view, output));

        }
    }

    private void handleConfirm(int choice) {
        if(choice == 1){
            setHasConfirmed();
        }
        else if(choice == 0){
            Coordinates[] source = {new Coordinates(firstSourceRow, firstSourceCol)};
            Coordinates[] destination = {new Coordinates(firstDestRow, firstDestCol)};
            view.handleToolCardUsage(source, destination);
            gatherer.setManager(new TurnHandlingManager(view, output));
        }
    }


    @Override
    public void showPrompt() {
        if(amount == 1){
            showOneMovePrompt();
        }
        else{
            showOneMovePrompt();
            showTwoMovePrompt();
        }
    }

    private void showOneMovePrompt() {
        if(firstSourceRow == -1){
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the source coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(firstSourceCol == -1){
            output.printText("Col (starting from 0): ");
        }
        else if(firstDestRow == -1){
            output.printTextNewLine("Enter the destination coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(firstDestCol == -1){
            output.printText("Col (starting from 0): ");
        }
    }

    private void showTwoMovePrompt() {
        if(!moveAll && !hasConfirmed){
            output.printTextNewLine("Do you want to move another die? Enter 1 if so, 0 otherwise");
        }
        if(secondSourceRow == -1){
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the second source coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(secondSourceCol == -1){
            output.printText("Col (starting from 0): ");

        }
        else if(secondDestRow == 1){
            output.printTextNewLine("Enter the second destination coordinates:");
            output.printText("Row (starting from 0): ");
        }
        else if(secondDestCol == -1){
            output.printText("Col (starting from 0): ");

        }
    }

    private void setHasConfirmed() {
        this.hasConfirmed = true;
    }
}
