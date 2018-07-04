package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.events.PlayerStatus;
import it.polimi.se2018.utils.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * The controller for the view that handles the pattern selection.
 * It shows the images of the patterns among which the player can choose
 * the one to use during the game, and then calls the method to notify
 * the controller.
 *
 * @author michelemarzollo
 */
public class PatternSelection {

    private static final int ROW_HEIGHT = 400;

    private static final String WAITING_MESSAGE = "The game is starting...";

    /**
     * The main controller of the gui.
     */
    private JavaFxDisplayer parentController;

    /**
     * The array of patterns associated to the player.
     */
    private it.polimi.se2018.model.Pattern[] patterns;

    /**
     * The grid where the pattern are placed.
     * Each pattern is in a cell of the grid.
     */
    @FXML
    private GridPane mainGrid;

    /**
     * The setter for the {@code parentController}.
     *
     * @param parentController the main controller of the gui.
     */
    public void setParentController(JavaFxDisplayer parentController) {
        this.parentController = parentController;
    }

    /**
     * The setter for the patterns associated to the player.
     *
     * @param patterns the array of patterns.
     */
    public void setPatterns(it.polimi.se2018.model.Pattern[] patterns) {
        this.patterns = patterns;
    }

    /**
     * The method to set the grid that shows all patterns.
     * <p>
     * The grid will have two columns, and a number of rows variable depending
     * on how many patterns must be shown.</p>
     */
    public void setGrid() {
        mainGrid.addRow(0);
        mainGrid.addRow(1);
        for (int i = 0; i < patterns.length; i++) {
            int col = i % 2;
            int row = i / 2;
            try {
                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(this.getClass().getResource("pattern.fxml"));
                AnchorPane patternView = loader.load();
                patternView.setMinHeight(ROW_HEIGHT);
                Pattern controller = loader.getController();

                mainGrid.setMinHeight(ROW_HEIGHT);
                //creates a mock PlayerStatus to use the methods of the Pattern controller
                PlayerStatus playerStatus = new PlayerStatus("", patterns[i].getDifficulty(), patterns[i]);

                controller.setStatus(playerStatus);
                controller.hidePlayerName(true);

                BorderPane borderPane = new BorderPane(patternView);
                borderPane.setOnMouseClicked(this::handleClick);

                mainGrid.add(borderPane, col, row);

            } catch (IOException e) {
                Logger.getDefaultLogger().log(e.getMessage());
            }
        }
    }

    /**
     * The method that handles the click on a pattern. A confirmation is requested to
     * the user, and, if confirmed, the method on the {@link it.polimi.se2018.view.ClientView}
     * is called.
     *
     * @param event the event of the mouse click.
     */
    private void handleClick(MouseEvent event) {
        Node source = (Node) event.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        int patterNumber = colIndex + rowIndex * 2;

        boolean confirmed = parentController.displayConfirm("You chose the " +
                patterns[patterNumber].getName() + " pattern.");

        if (confirmed) {
            parentController.displayWaitingView(WAITING_MESSAGE);
            parentController.getView().handlePatternSelection(patterns[patterNumber].getName());
        } else parentController.askPattern();
    }

}
