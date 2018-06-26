package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

public class DiePlacementHandler extends  InputEventManager{

    private boolean confirmDone;
    private int dieIndex;
    private int row;
    private int col;

    private TurnHandlingManager manager;

    public DiePlacementHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
        dieIndex = -1;
        row = -1;
        col = -1;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input);
            if (!confirmDone) {
                handleConfirm(choice);
            } else if (dieIndex == -1) {
                handleDieIndex(choice);
            } else if (row == -1) {
                handleRow(choice);
            } else if (col == -1) {
                handleCol(choice);
            }
        }
        catch (NumberFormatException ex){
            showError();
        }
    }


    private void handleConfirm(int choice){
        if(choice == 1){
            setConfirmDone();
        }
        else{
            manager.setSubHandler(null);
        }
    }

    private void handleDieIndex(int choice){
        if(choice < 0){
            showError();
        }
        else {
            dieIndex = choice;
        }
    }

    private void handleRow(int choice) {
        if(choice < 0){
            showError();
        }
        else {
            row = choice;
        }
    }

    private void handleCol(int choice) {
        if(choice < 0){
            showError();
        }
        else{
            col = choice;
            manager.setSubHandler(null);
            view.handlePlacement(dieIndex, new Coordinates(row, col));
        }
    }




    @Override
    public void showPrompt() {
        if(!confirmDone){ //è la prima cosa da fare
            showConfirmPrompt();
        }
        else{
            if(dieIndex == -1){
                output.printDraftPool(getDraftPool());
                output.printTextNewLine("Enter the index of the die:");
            }
            else if(row == -1){
                output.printPattern(getPattern());
                output.printTextNewLine("Enter the coordinates for your placement:");
                output.printText("Row (starting from 0): ");
            }
            else if(col == -1){
                output.printText("Col (starting from 0): ");

            }
        }
    }

    private void showConfirmPrompt() {
        output.printTextNewLine("You chose to place a die, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }


    private void setConfirmDone() {
        this.confirmDone = true;
    }
}
