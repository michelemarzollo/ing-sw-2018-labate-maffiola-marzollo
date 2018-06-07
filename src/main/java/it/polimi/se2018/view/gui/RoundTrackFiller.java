package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.Coordinates;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private Image loadDieImage(Die die) {
        String url = "/dice/" + die.getColour() + die.getValue() + ".jpg";
        return new Image(url);
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
            for (Die die : column) {
                ImageView dieImage = new ImageView(loadDieImage(die));
                dieImage.setPreserveRatio(true);
                container.getChildren().add(dieImage);
                dieImage.setOnMouseClicked(this::onClick);
            }
            columnContainer.getChildren().add(container);
        }
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
