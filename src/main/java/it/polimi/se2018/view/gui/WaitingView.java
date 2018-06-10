package it.polimi.se2018.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Displays a window on the screen to tell that the user must wait a few
 * moments before he can continue with the game.
 *
 * @author michelemarzollo
 */
public class WaitingView {

    /**
     * The text to show (the reason why the user must wait).
     */
    @FXML
    private Label text;

    /**
     * The setter of {@code text}.
     *
     * @param text the text to show.
     */
    public void setText(String text) {
        this.text.setText(text);
    }
}
