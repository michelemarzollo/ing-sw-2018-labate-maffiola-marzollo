package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.events.PlayerConnectionStatus;
import it.polimi.se2018.model.events.PlayerStatus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * This class is a JavaFX controller for the multi player game board.
 */
public class MultiPlayerBoard extends GameBoard {

    /**
     * The HBox used to display opponents.
     */
    @FXML
    private HBox opponentsContainer;

    /**
     * The HBox used to display public cards.
     */
    @FXML
    private HBox publicCardContainer;

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
     * The HBox used to display the draft pool.
     */
    @FXML
    private HBox draftPoolContainer;

    /**
     * The HBox used to display the round track.
     */
    @FXML
    private HBox roundTrackContainer;

    /**
     * The BorderPane used to display the local player pattern.
     */
    @FXML
    private BorderPane playerPatternContainer;

    /**
     * The label to show the player who has the current turn.
     */
    @FXML
    private Label turnLabel;

    /**
     * Loads the pattern layout for an opponent.
     * @param playerStatus The status of the opponent.
     * @return The pattern layout for the opponent.
     */
    private BorderPane loadOpponentPattern(PlayerStatus playerStatus) {
        BorderPane pattern = loadPatternFor(playerStatus, true);
        HBox.setHgrow(pattern, Priority.ALWAYS);
        return pattern;
    }

    /**
     * Initializes the patterns for the opponents.
     */
    private void initializeOpponents() {
        if (getDisplayer().getDataOrganizer().getAllPlayerStatus() == null)
            return;

        String localPlayer = getDisplayer().getDataOrganizer().getLocalPlayer();
        opponentsContainer.getChildren().clear();
        for (PlayerStatus player : getDisplayer().getDataOrganizer().getAllPlayerStatus()) {
            if (!player.getPlayerName().equals(localPlayer)) {
                BorderPane pattern = loadOpponentPattern(player);
                checkConnected(pattern, player.getPlayerName());
                opponentsContainer.getChildren().addAll(pattern);
            }
        }
        opponentsContainer.setAlignment(Pos.CENTER);
    }

    /**
     * Checks if an opponent is connected or disconnected.
     * <p>If the player results disconnected, its pattern is altered in order to
     * display such information.</p>
     * @param pattern The pattern layout of the player.
     * @param playerName The name of the player to be checked.
     */
    private void checkConnected(BorderPane pattern, String playerName) {
        PlayerConnectionStatus connectionStatus =
                getDisplayer().getDataOrganizer().getConnectionStatus(playerName);

        if (connectionStatus == null || !connectionStatus.isConnected())
            pattern.setDisable(true);
        else
            pattern.setDisable(false);
    }

    /**
     * Initializes the board layout.
     */
    @Override
    void initializeLayout(){
        super.initializeLayout();
        initializeOpponents();
    }

    /**
     * Tells that the game is using the multi player rule set.
     * @return Always {@code true}.
     */
    @Override
    boolean isMultiPlayer() {
        return true;
    }

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

    HBox getDraftPoolContainer(){
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
}
