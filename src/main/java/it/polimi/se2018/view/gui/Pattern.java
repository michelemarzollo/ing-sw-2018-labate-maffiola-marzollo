package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.events.PlayerStatus;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.ResourceManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.function.Consumer;

/**
 * This class is the JavaFX controller used when a pattern is displayed.
 *
 * @author dvdmff
 */
public class Pattern {

    /**
     * The container for tokens.
     */
    @FXML
    private HBox tokenContainer;

    /**
     * The label displaying the name of the player.
     */
    @FXML
    private Label playerNameLabel;

    /**
     * The label displaying the name of the pattern.
     */
    @FXML
    private Label patternNameLabel;

    /**
     * The grid pane displaying the schema and placed dice.
     */
    @FXML
    private GridPane patternGrid;

    /**
     * The container of the grid.
     */
    @FXML
    private BorderPane container;

    /**
     * The state of the displayed player.
     */
    private PlayerStatus playerStatus;

    /**
     * The handler that is called upon click events.
     */
    private Consumer<Coordinates> handler;

    /**
     * Enforces layout constrains.
     */
    @FXML
    private void initialize() {

        patternGrid.minHeightProperty().bind(patternGrid.minWidthProperty().multiply(0.8));
        patternGrid.maxHeightProperty().bind(patternGrid.maxWidthProperty().multiply(0.8));

        patternGrid.maxWidthProperty().bind(
                Bindings.min(
                        container.widthProperty().multiply(0.9),
                        container.heightProperty().multiply(0.9)
                )
        );
    }

    /**
     * Hides or displays the player name label.
     *
     * @param hide {@code true} if the player name label has to be hidden;
     *             {@code false} otherwise.
     */
    public void hidePlayerName(boolean hide) {
        playerNameLabel.setVisible(hide);
    }

    /**
     * Reduces the size of the pattern to the minimum possible.
     */
    public void minimize() {
        playerNameLabel.setFont(new Font(20));
        patternNameLabel.setFont(new Font(15));
    }

    /**
     * Sets a new player status and updates the data to display it.
     *
     * @param playerStatus The new player status.
     */
    public void setStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        refreshData();
    }

    /**
     * Refreshes the displayed content.
     */
    private void refreshData() {
        patternNameLabel.setText(playerStatus.getPattern().getName());
        playerNameLabel.setText(playerStatus.getPlayerName());
        fillGrid();
        fillTokens();
    }

    /**
     * Fills the pattern grid according to what is stored in playerStatus.
     */
    private void fillGrid() {
        Cell[][] grid = playerStatus.getPattern().getGrid();
        patternGrid.getChildren().clear();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                Cell cell = grid[row][col];
                AnchorPane cellRepresentation;
                if (cell.getDie() == null)
                    cellRepresentation = makeRestrictionAt(cell);
                else
                    cellRepresentation = makeDieAt(cell.getDie());

                insert(cellRepresentation, row, col);
            }
        }
    }

    /**
     * Set the style for the specified cell.
     * <p>If url is empty, no background will be loaded.</p>
     *
     * @param cell The node representing a cell.
     * @param url  The url of the background to use.
     */
    private void setCellStyle(AnchorPane cell, String url) {
        if (!url.isEmpty())
            cell.setStyle(
                    "-fx-background-image:url('" + url + "');" +
                            "-fx-background-position: center center;" +
                            "-fx-background-repeat: stretch;" +
                            "-fx-background-size: cover;" +
                            "-fx-border-color:black;" +
                            "-fx-border-width: 2px");
        else
            cell.setStyle("-fx-border-color:black;" +
                    "-fx-border-width: 2px");
    }

    /**
     * Creates a new ImageView that represents the specified die.
     *
     * @param die The die to be represented.
     * @return An image view filled with the proper die image.
     */
    private AnchorPane makeDieAt(Die die) {

        AnchorPane dieRepresentation = new AnchorPane();
        String url = ResourceManager.getInstance().getDieImageUrl(die);
        setCellStyle(dieRepresentation, url);

        return dieRepresentation;
    }

    /**
     * Creates a new ImageView that represents the restriction in the specified cell.
     *
     * @param cell The cell containing the restriction to display.
     * @return An image view filled with the proper restriction image.
     */
    private AnchorPane makeRestrictionAt(Cell cell) {
        AnchorPane restrictionRepresentation = new AnchorPane();
        String url;
        url = ResourceManager.getInstance().getCellImageUrl(cell);
        setCellStyle(restrictionRepresentation, url);

        return restrictionRepresentation;
    }

    /**
     * Inserts a cell in the grid.
     * <p>It also sets the click event handler to cells and adjust the size of the image.</p>
     *
     * @param cell The image view to be added.
     * @param row  The row index.
     * @param col  The column index.
     */
    private void insert(AnchorPane cell, int row, int col) {
        cell.prefHeightProperty().bindBidirectional(cell.prefWidthProperty());
        cell.setOnMouseClicked(this::onCellClick);
        patternGrid.add(cell, col, row);
    }

    /**
     * Populates the token container with tokens.
     * <p>The amount of tokens is read from the current player status.</p>
     */
    private void fillTokens() {
        tokenContainer.getChildren().clear();
        int radius = 10;
        for (int i = 0; i < playerStatus.getTokens(); i++) {
            Circle token = new Circle();
            token.setRadius(radius);
            token.setStroke(Color.BLACK);
            token.setFill(Color.WHITE);
            tokenContainer.getChildren().add(token);
        }
    }

    /**
     * Handles the click event on a cell of the grid.
     * <p>If the selection handler is not set, this method will do nothing.</p>
     *
     * @param event The mouse event.
     */
    private void onCellClick(MouseEvent event) {
        if (handler != null) {
            Node source = (Node) event.getSource();
            int col = GridPane.getColumnIndex(source);
            int row = GridPane.getRowIndex(source);
            handler.accept(new Coordinates(row, col));
        }
    }

    /**
     * Sets the handler used during click events.
     * <p>The parameter passed to the handler is a pair of coordinates of the selection.</p>
     *
     * @param handler The handler to be called during click events.
     */
    public void setSelectionHandler(Consumer<Coordinates> handler) {
        this.handler = handler;
    }

    /**
     * Sets a player as disconnected.
     * <p>Its name and pattern name labels will be set disabled to graphically
     * notify the player.</p>
     *
     * @param disconnected Flag to indicate if the player has to be set connected or disconnected.
     */
    public void setDisconnected(boolean disconnected) {
        playerNameLabel.setDisable(disconnected);
        patternNameLabel.setDisable(disconnected);
    }
}
