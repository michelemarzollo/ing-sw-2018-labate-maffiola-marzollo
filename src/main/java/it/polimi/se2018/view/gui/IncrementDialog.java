package it.polimi.se2018.view.gui;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This class displays a dialog window to let the player select whether to increment
 * or decrement.
 */
public class IncrementDialog {

    /**
     * The stage used to display the dialog.
     */
    private Stage stage;

    /**
     * Flag to indicate the user choice;
     */
    private boolean increment;

    /**
     * Creates a new IncrementDialog that uses the given stage to display
     * itself.
     *
     * @param stage The stage used to display the dialog.
     */
    public IncrementDialog(Stage stage) {
        this.stage = stage;
        initializeLayout();
    }

    /**
     * Initializes the layout of the dialog.
     */
    private void initializeLayout() {
        BorderPane container = new BorderPane();
        Label information = new Label();
        information.setText("What do you want to do?");
        ButtonBar buttons = new ButtonBar();
        Button incrementButton = new Button();
        incrementButton.setText("Increment");
        incrementButton.setOnMouseClicked(this::onIncrementClick);
        Button decrementButton = new Button();
        decrementButton.setText("Decrement");
        decrementButton.setOnMouseClicked(this::onDecrementClick);
        buttons.getButtons().addAll(decrementButton, incrementButton);
        container.setTop(information);
        container.setBottom(buttons);
    }

    /**
     * Event called when the decrement button is clicked.
     *
     * @param event The mouse event.
     */
    private void onDecrementClick(MouseEvent event) {
        increment = false;
        stage.close();
    }

    /**
     * Event called when the increment button is clicked.
     *
     * @param event The mouse event.
     */
    private void onIncrementClick(MouseEvent event) {
        increment = true;
        stage.close();
    }

    /**
     * Displays the dialog and waits for user response.
     */
    public void askUser() {
        stage.showAndWait();
    }

    /**
     * Returns the user choice.
     * <p>If called before {@link IncrementDialog#askUser()} it will return {@code false}.</p>
     *
     * @return {@code true} if the user selected to increment; {@code false} if the user has
     * not yet selected anything or has selected to decrement.
     */
    public boolean isIncrement() {
        return increment;
    }
}
