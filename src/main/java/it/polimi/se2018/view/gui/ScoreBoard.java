package it.polimi.se2018.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Map;

/**
 * This class is a JavaFX controller to handle the display of the score board.
 */
public class ScoreBoard {

    /**
     * The grid used to keep track of the tens of the player score.
     */
    @FXML
    private GridPane tensGrid;

    /**
     * The grid used to keep track of the units of the player score.
     */
    @FXML
    private GridPane unitsGrid;

    /**
     * The VBox that contains the name of the players.
     */
    @FXML
    private VBox playerNames;

    /**
     * The displayed score board.
     */
    private Map<String, Integer> scoreBoardMap;

    /**
     * The assignment of colours depending of the player position.
     */
    private static final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.PURPLE};

    /**
     * Sets and displays the specified score board.
     *
     * @param scoreBoardMap The score board to display.
     */
    public void setScoreBoardMap(Map<String, Integer> scoreBoardMap) {
        this.scoreBoardMap = scoreBoardMap;
        start();
    }

    /**
     * Places a marker in the given grid in te position identified by {@code cellNum}
     * for the player placed in the specified position.
     *
     * @param grid        The grid where the marker has to be placed.
     * @param cellNum     The number of the cell to fill.
     * @param playerIndex The position of the player.
     */
    private void fillGrid(GridPane grid, int cellNum, int playerIndex) {
        int radius = 20;
        if (cellNum > 0 && cellNum < 10) {
            Circle marker = new Circle();
            marker.setRadius(radius);
            marker.setFill(colors[playerIndex]);
            marker.setStroke(Color.BLACK);
            GridPane markerGrid = (GridPane) grid.getChildren().get(cellNum - 1);
            markerGrid.add(marker, playerIndex / 2, playerIndex % 2);
        }
    }

    /**
     * Displays the score board according to currently saved data.
     */
    private void start() {
        int playerIndex = 0;
        for (Map.Entry<String, Integer> entry : scoreBoardMap.entrySet()) {

            int score = entry.getValue();
            int tens = score / 10;
            int units = score % 10;

            fillGrid(tensGrid, tens, playerIndex);
            fillGrid(unitsGrid, units, playerIndex);

            Label playerName = new Label();
            String text = String.format("%d. %s (%d)", playerIndex + 1, entry.getKey(), entry.getValue());
            playerName.setText(text);
            playerName.setTextFill(colors[playerIndex]);
            playerName.setStyle("-fx-font-weight: bold;" +
                    "-fx-font-size: 20px");
            playerNames.getChildren().add(playerName);

            ++playerIndex;
        }
    }

}
