package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

public class ToolCardConfirmManager extends InputEventManager{

    private CliInput gatherer;

    public ToolCardConfirmManager(ClientView view, CliImagePrinter output, CliInput gatherer) {
        super(view, output);
        this.gatherer = gatherer;
    }

    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseInt(input.trim());
            if(choice == 1){
                view.handleToolCardUsage();
            }
            gatherer.setManager(new TurnHandlingManager(view, output));
        }
        catch(NumberFormatException ex){
            showError();
        }
    }

    @Override
    public void showPrompt() {
        output.printText("Do you want to activate the Tool Card effect? Enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }
}
