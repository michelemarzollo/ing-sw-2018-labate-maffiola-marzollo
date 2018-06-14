package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.events.PlayerConnectionStatus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
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
     * Loads the pattern layout for an opponent.
     * @param playerName The status of the opponent.
     * @return The pattern layout for the opponent.
     */
    private AnchorPane loadOpponentPattern(String playerName) {
        AnchorPane pattern = loadPatternFor(playerName, true);
        HBox.setHgrow(pattern, Priority.ALWAYS);
        return pattern;
    }

    /**
     * Initializes the patterns for the opponents.
     */
    private void initializeOpponents() {
        if (getDisplayer().getDataOrganizer().getGameSetup() == null)
            return;

        String localPlayer = getDisplayer().getDataOrganizer().getLocalPlayer();
        opponentsContainer.getChildren().clear();
        for (String playerName : getDisplayer().getDataOrganizer().getGameSetup().getPlayers()) {
            if (!playerName.equals(localPlayer)) {
                AnchorPane pattern = loadOpponentPattern(playerName);
                checkConnected(pattern, playerName);
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
    private void checkConnected(AnchorPane pattern, String playerName) {
        PlayerConnectionStatus connectionStatus =
                getDisplayer().getDataOrganizer().getConnectionStatus(playerName);

        if (connectionStatus == null || connectionStatus.isConnected())
            pattern.setDisable(false);
        else
            pattern.setDisable(true);
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
}
