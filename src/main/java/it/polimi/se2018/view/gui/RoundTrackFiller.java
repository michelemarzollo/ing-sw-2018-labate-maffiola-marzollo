package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.Coordinates;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class handles the display of the round track.
 *
 * @author dvdmff
 */
public class RoundTrackFiller {

    /**
     * The HBox that contains the columns.
     */
    private HBox columnContainer;

    /**
     * The handler to be called during click events on dice.
     */
    private Consumer<Coordinates> selectionHandler;

    /**
     * Creates a new RoundTrackFiller instance that will use the specified VBox to
     * display dice.
     *
     * @param columnContainer The HBox where dice will be displayed.
     */
    public RoundTrackFiller(HBox columnContainer) {
        this.columnContainer = columnContainer;
    }

    /**
     * Loads the correct image for a die.
     *
     * @param die The die to be represented.
     * @return An image holding the representation of the die.
     * @deprecated Use a url manager instead.
     */
    @Deprecated
    private AnchorPane loadDieImage(Die die) {
        String url = getClass().getResource("images/dice/" + die.getColour() + die.getValue() + ".jpg").toString();
        AnchorPane dieImage = new AnchorPane();
        dieImage.setStyle(
                "-fx-background-image:url('" + url + "');" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: stretch;" +
                        "-fx-background-size: cover");
        return dieImage;
    }

    /**
     * Handles the click event on a die in the round track.
     * <p>If the selection handler is not set, this method will do nothing.</p>
     *
     * @param event The mouse event.
     */
    private void onClick(MouseEvent event) {
        if (selectionHandler != null) {

            Node source = (Node) event.getSource();
            for (int col = 0; col < columnContainer.getChildren().size(); ++col) {
                VBox dieContainer = (VBox) columnContainer.getChildren().get(col);
                int row = dieContainer.getChildren().indexOf(source);
                if (row != -1) {
                    selectionHandler.accept(new Coordinates(row, col));
                    return;
                }
            }
        }
    }

    /**
     * Displays the specified matrix of dice.
     *
     * @param leftovers The matrix of leftover dice in the round track.
     */
    public void setLeftoverDice(List<List<Die>> leftovers) {
        columnContainer.getChildren().clear();
        for (List<Die> column : leftovers) {
            VBox container = new VBox();
            container.setAlignment(Pos.TOP_CENTER);
            fitColumn(container, leftovers.size());
            for (Die die : column) {
                AnchorPane dieImage = loadDieImage(die);
                fitDie(container, dieImage);
                container.getChildren().add(dieImage);
                dieImage.setOnMouseClicked(this::onClick);
            }
            columnContainer.getChildren().add(container);
        }
    }

    private void fitColumn(VBox column, int num) {
        column.minWidthProperty().bind(
                columnContainer.widthProperty()
                        .subtract(columnContainer.getSpacing() * num)
                        .divide(num)
        );
        column.maxWidthProperty().bind(
                columnContainer.widthProperty()
                        .subtract(columnContainer.getSpacing() * num)
                        .divide(num)
        );

    }

    private void fitDie(VBox container, AnchorPane dieImage) {
        dieImage.minHeightProperty().bind(dieImage.minWidthProperty());
        dieImage.maxHeightProperty().bind(dieImage.maxWidthProperty());

        dieImage.minWidthProperty().bind(
                        container.widthProperty()
        );
        dieImage.maxWidthProperty().bind(
                        container.widthProperty()
        );
    }

    /**
     * Sets the handler for the selection of a die in the draft pool.
     *
     * @param selectionHandler The selection handler.
     */
    public void setSelectionHandler(Consumer<Coordinates> selectionHandler) {
        this.selectionHandler = selectionHandler;
    }
}
