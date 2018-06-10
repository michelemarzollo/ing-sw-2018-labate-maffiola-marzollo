package it.polimi.se2018.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The controller to show a stage to confirm or not a previously made choice.
 *
 * @author michelemarzollo
 */
public class ConfirmationView {

    /**
     * The text label that displays the request.
     */
    @FXML
    private Label text;

    /**
     * The button to confirm.
     */
    @FXML
    private Button yesButton;

    /**
     * The button to choose again.
     */
    @FXML
    private Button noButton;

    /**
     * The stage that is opened when a the user must confirm it's choice.
     */
    private Stage dialogStage;

    /**
     * The flag that says if the answer is confirmed.
     */
    private boolean isConfirmed = false;

    /**
     * The setter for the question to ask.
     *
     * @param text the text to show.
     */
    public void setText(String text) {
        this.text.setText(text);
    }

    /**
     * The setter for {@code dialogStage}.
     *
     * @param dialogStage the stage that is opened when a the user must confirm it's choice.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * The method that is called after the mous click in any button.
     *
     * @param e the event from the mouse.
     * @return {@code true} if the {@code yesButton} was clicked, {@code false}
     * if the {@code noButton} was clicked.
     */
    @FXML
    public boolean confirmSelection(ActionEvent e) {
        isConfirmed = e.getSource().equals(yesButton);
        dialogStage.close();
        return isConfirmed;
    }

    /**
     * The method to call to see if the decision was confirmed.
     *
     * @return {@code true} if it was confirmed, false otherwise.
     */
    public boolean isConfirmed() {
        return isConfirmed;
    }

}
