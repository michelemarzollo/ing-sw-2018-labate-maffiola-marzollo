package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.ResourceManager;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class handles the display of the draft pool.
 *
 * @author dvdmff
 */
public class DraftPoolFiller {

    /**
     * Maximum height for the dice container.
     */
    private static final double MAX_HEIGHT = 100;

    /**
     * The container where dice are to be placed.
     */
    private HBox diceContainer;

    /**
     * The handler to be called during click events on dice.
     */
    private Consumer<Integer> handler;

    private int forcedSelectionIndex = -1;
    private int sacrificeIndex = -1;

    /**
     * Creates a new DraftPoolFiller instance that will use the specified HBox to
     * display dice.
     *
     * @param diceContainer The HBox where dice will be displayed.
     */
    public DraftPoolFiller(HBox diceContainer) {
        this.diceContainer = diceContainer;
        diceContainer.setMaxHeight(MAX_HEIGHT);
    }

    /**
     * Creates a node with the correct image for the given die.
     *
     * @param die The die to be represented.
     * @return A node holding the representation of the given die.
     */
    private AnchorPane getImageFor(Die die) {
        String url = ResourceManager.getInstance().getDieImageUrl(die);
        AnchorPane dieImage = new AnchorPane();
        dieImage.setStyle(
                "-fx-background-image:url('" + url + "');" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: stretch;" +
                        "-fx-background-size: cover");
        return dieImage;
    }

    /**
     * Handles the click event on a die in the draft pool.
     * <p>If the selection handler is not set, this method will do nothing.</p>
     *
     * @param event The mouse event.
     */
    private void onClick(MouseEvent event) {
        if (handler != null) {
            Node source = (Node) event.getSource();
            int index = diceContainer.getChildren().indexOf(source);
            setSelected(index);
            handler.accept(index);
        }
    }

    /**
     * Sets the node at the given index as selected.
     * <p>If a die is already set as </p>
     *
     * @param index The index of the node to select.
     */
    private void setSelected(int index) {
        for (int i = 0; i < diceContainer.getChildren().size(); ++i) {
            Node node = diceContainer.getChildren().get(i);
            if (i == index) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(0.4);
                colorAdjust.setContrast(0.2);
                node.setEffect(colorAdjust);
            } else if (resetEffect(i))
                node.setEffect(null);
        }
    }

    /**
     * Tells if the effect of the die at the given index can be reset.
     * @param index The index of the die.
     * @return {@code true} if the effect can be reset.
     */
    private boolean resetEffect(int index){
        return (sacrificeIndex != -1 && sacrificeIndex != index)
                || index == forcedSelectionIndex
                || (sacrificeIndex == -1 && forcedSelectionIndex == -1);
    }

    /**
     * Displays the given list of dice.
     *
     * @param dice The list of dice in the draft pool.
     */
    public void setDice(List<Die> dice) {
        forcedSelectionIndex = -1;
        sacrificeIndex = -1;
        diceContainer.getChildren().clear();
        for (Die die : dice) {
            AnchorPane dieImage = getImageFor(die);
            fitDie(dieImage, dice.size());
            dieImage.setOnMouseClicked(this::onClick);
            diceContainer.getChildren().add(dieImage);
        }
    }

    /**
     * Disables the die at the given node.
     *
     * @param node The node containing the die to disable.
     */
    private void disableDie(Node node) {
        ColorAdjust effect = new ColorAdjust();
        effect.setSaturation(-1);
        node.setDisable(true);
        node.setEffect(effect);
    }

    /**
     * Disables the die at the given index.
     *
     * @param index The index of the die to disable.
     */
    public void setSacrifice(int index) {
        sacrificeIndex = index;
        disableDie(diceContainer.getChildren().get(index));
    }

    /**
     * Disables all dice but the one at the given index.
     *
     * @param index The index of the die to keep enabled.
     */
    public void setForcedSelection(int index) {
        forcedSelectionIndex = index;
        for (int i = 0; i < diceContainer.getChildren().size(); i++) {
            if (i != index)
                disableDie(diceContainer.getChildren().get(i));
        }
    }

    /**
     * Fits a die in the dice container.
     *
     * @param dieImage The AnchorPane containing the image of the die.
     * @param num      The number of dice in a row.
     */
    private void fitDie(AnchorPane dieImage, int num) {
        dieImage.minHeightProperty().bind(dieImage.minWidthProperty());
        dieImage.maxHeightProperty().bind(dieImage.maxWidthProperty());

        dieImage.maxWidthProperty().bind(
                Bindings.min(
                        diceContainer.widthProperty()
                                .subtract(diceContainer.getSpacing() * num)
                                .divide(num),
                        diceContainer.heightProperty().multiply(0.9)
                )
        );
        HBox.setHgrow(dieImage, Priority.ALWAYS);
    }

    /**
     * Sets the handler for the selection of a die in the draft pool.
     *
     * @param handler The selection handler.
     */
    public void setSelectionHandler(Consumer<Integer> handler) {
        this.handler = handler;
    }
}
