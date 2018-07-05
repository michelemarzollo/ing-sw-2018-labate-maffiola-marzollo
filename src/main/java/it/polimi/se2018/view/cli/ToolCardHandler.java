package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to select a tool card.
 */
public class ToolCardHandler extends InputEventManager {

    /**
     * Reference to the manager of the turn.
     */
    private final TurnHandlingManager manager;

    /**
     * Indicates whether the user has confirmed to perform this action or not.
     */
    private boolean confirmDone;

    /**
     * Name of the chosen Tool Card.
     */
    private String name;

    /**
     * Index for the die in the Draft Pool that has to be spent in Single Player mode.
     */
    private int sacrificeIndex;

    /**
     * Constructor of the class
     *
     * @param view    The view to which this manager is bounded.
     * @param output  The output destination where the prompts of this manager
     *                are shown.
     * @param manager The TurnHandlingManager.
     */
    public ToolCardHandler(ClientView view, CliPrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
        sacrificeIndex = -1;
    }


    /**
     * Handles the input entered by the user.
     * <p>After collecting user confirmation, the card name and the sacrifice die
     * index (if in single player mode), calls the proper handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        if (!confirmDone) {
            try {
                int choice = Integer.parseUnsignedInt(input.trim());
                handleConfirm(choice);
            } catch (NumberFormatException ex) {
                showError();
            }
        } else if (name == null)
            handleName(input);

        else if (sacrificeIndex == -1 && isSinglePlayer()) {
            try {
                int choice = Integer.parseUnsignedInt(input.trim());
                handleIndex(choice);
            } catch (NumberFormatException ex) {
                showError();
            }
        }
    }

    /**
     * Handles the first choice: the confirmation of the will of placing a die.
     *
     * @param choice The choice performed by the user.
     */
    private void handleConfirm(int choice) {
        if (choice == 1)
            setConfirmDone();
        else
            manager.reset();
    }

    /**
     * Handles the second choice: the name of the Tool Card.
     *
     * @param input The choice performed by the user (the name entered).
     */
    private void handleName(String input) {
        name = input;
        if (!isSinglePlayer())
            getView().handleToolCardSelection(name);
    }

    /**
     * Handles the third choice: the index of the die that has to be spent.
     *
     * @param choice The choice performed by the user.
     */
    private void handleIndex(int choice) {
        sacrificeIndex = choice;
        getView().handleToolCardSelection(name, sacrificeIndex);
    }


    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (!confirmDone) {
            showConfirmPrompt();
        } else if (name == null) {
            getOutput().printToolCards(getToolCards(), getUsedToolCards());
            getOutput().println("Enter the name of the Tool Card:");
        } else if (sacrificeIndex == -1 && isSinglePlayer()) {
            getOutput().printDraftPool(getDraftPool());
            getOutput().println("Enter the index of the die you want to spend to use the tool card:");
        }

    }

    /**
     * Shows the confirmation prompt.
     */
    private void showConfirmPrompt() {
        getOutput().print("You chose to use a Tool Card, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
    }

    /**
     * Sets the confirmation flag.
     */
    private void setConfirmDone() {
        this.confirmDone = true;
    }
}
