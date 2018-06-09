package it.polimi.se2018.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * This class is a JavaFX controller for the single player game board.
 */
public class SinglePlayerBoard extends GameBoard {

    /**
     * The BorderPane used to display the local player pattern.
     */
    @FXML
    private BorderPane playerPatternContainer;

    /**
     * The HBox used to display the round track.
     */
    @FXML
    private HBox roundTrackContainer;

    /**
     * The HBox used to display the draft pool.
     */
    @FXML
    private HBox draftPoolContainer;

    /**
     * The HBox used to display tool cards.
     */
    @FXML
    private HBox toolCardContainer;

    /**
     * The HBox used to display private cards.
     */
    @FXML
    private HBox privateCardContainer;

    /**
     * The HBox used to display public cards.
     */
    @FXML
    private HBox publicCardContainer;

    /**
     * The label to show the player who has the current turn.
     */
    @FXML
    private Label turnLabel;

    @Override
    HBox getPublicCardContainer() {
        return publicCardContainer;
    }

    @Override
    HBox getPrivateCardContainer() {
        return privateCardContainer;
    }

    @Override
    HBox getToolCardContainer() {
        return toolCardContainer;
    }

    @Override
    HBox getDraftPoolContainer() {
        return draftPoolContainer;
    }

    @Override
    HBox getRoundTrackContainer() {
        return roundTrackContainer;
    }

    @Override
    BorderPane getPlayerPatternContainer() {
        return playerPatternContainer;
    }

    @Override
    Label getTurnLabel() {
        return turnLabel;
    }

    /**
     * Tells that the game is using the single player rule set.
     * @return Always {@code false}.
     */
    @Override
    boolean isMultiPlayer() {
        return false;
    }
}
