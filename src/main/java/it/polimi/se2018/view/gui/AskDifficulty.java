package it.polimi.se2018.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * The controller for the screen that asks for difficulty in single-player
 * mode. It's related to the fxml file ask_difficulty.fxml.
 *
 * @author michelemarzollo
 */
public class AskDifficulty {

    /**
     * The message to be shown.
     */
    private static final String ERROR_MESSAGE = "The difficulty must be an integer between 1 and 5.";

    /**
     * The maximum difficulty of the single-player game.
     */
    private static final int MAX_DIFFICULTY = 5;

    /**
     * The main controller, which instantiates this one and has the reference to the
     * client view.
     */
    private JavaFxDisplayer parentController;

    /**
     * The text label that displays the request.
     */
    @FXML
    private Label label;

    /**
     * The {@link TextField} to insert the difficulty.
     */
    @FXML
    private TextField textField;

    /**
     * The button to confirm the decision.
     */
    @FXML
    private Button okButton;

    /**
     * The method that handles the getting of the difficulty and the calling
     * of the method of the ClientView to notify the controller.
     *
     * @see it.polimi.se2018.view.ClientView#handleDifficultySelection(int)
     */
    @FXML
    public void getDifficulty() {
        if (textField.getText().length() == 1) {
            try {
                int difficulty = Integer.parseInt(textField.getText());
                if (difficulty < 1 || difficulty > MAX_DIFFICULTY)
                    parentController.displayError(ERROR_MESSAGE);
                else {
                    parentController.displayWaitingView("The game is starting...");
                    parentController.getView().handleDifficultySelection(difficulty);
                }
            } catch (NumberFormatException e) {
                parentController.displayError(ERROR_MESSAGE);
            }
        } else parentController.displayError(ERROR_MESSAGE);
    }

    /**
     * The setter of the class. Sets the 'parent' controller and the fixed
     * text to be shown.
     *
     * @param parentController the controller that instantiates this one.
     */
    public void setController(JavaFxDisplayer parentController) {
        this.parentController = parentController;
        label.setText("Please select the difficulty of the game:\nit must be a number between " +
                "1 and " + MAX_DIFFICULTY + ":");
    }

}
