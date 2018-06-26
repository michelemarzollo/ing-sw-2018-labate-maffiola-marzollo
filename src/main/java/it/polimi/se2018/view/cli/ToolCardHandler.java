package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;


public class ToolCardHandler extends InputEventManager {

    private TurnHandlingManager manager;

    private boolean confirmDone;
    private String name;
    private int sacrificeIndex;

    public ToolCardHandler(ClientView view, CliImagePrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
        sacrificeIndex = -1;
    }

    @Override
    public void handle(String input) {
        if(!confirmDone){
            try {
                int choice = Integer.parseInt(input.trim());
                handleConfirm(choice);
            }
            catch (NumberFormatException ex){
                showError();
            }
        }
        else if(name == null){
            handleName(input);
        }
        else if(sacrificeIndex == -1 && isSinglePlayer()) {
            try {
                int choice = Integer.parseInt(input.trim());
                handleIndex(choice);
            }
            catch (NumberFormatException ex){
                showError();
            }
        }
    }


    private void handleIndex(int choice) {
        if(choice < 0){
            showError();
        }
        else{
            manager.setSubHandler(null);
            view.handleToolCardSelection(name, sacrificeIndex);
        }
    }

    private void handleName(String input) {
        name = input;
        if (!isSinglePlayer()) {
            manager.setSubHandler(null);
            view.handleToolCardSelection(name);
        }
    }

    private void handleConfirm(int choice) {
        if(choice == 1){
            setConfirmDone(true);
        }
        else{
            manager.setSubHandler(null);
        }
    }

    @Override
    public void showPrompt() {
        if (!confirmDone) {
            showConfirmPrompt();
        }
        else if (name == null) {
            output.printToolCards(getToolCards());
            output.printTextNewLine("Enter the name of the Tool Card:");
        }
        else if(sacrificeIndex == -1 && isSinglePlayer()){
                output.printDraftPool(getDraftPool());
                output.printTextNewLine("Enter the index of the die you want to spend to use the tool card:");
        }

    }

    private void showConfirmPrompt() {
        output.printText("You chose to use a Tool Card, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }

    public void setConfirmDone(boolean confirmDone) {
        this.confirmDone = confirmDone;
    }
}
