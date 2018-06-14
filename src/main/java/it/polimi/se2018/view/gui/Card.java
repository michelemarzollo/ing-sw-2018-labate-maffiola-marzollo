package it.polimi.se2018.view.gui;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

/**
 * This class handles the displaying of a card.
 *
 * @author dvdmff
 */
public class Card {

    private static final double MAX_X = 350;

    /**
     * The container holding the card.
     */
    private HBox container;

    /**
     * The image view holding the card image.
     */
    private BorderPane cardImage = new BorderPane();

    private String url;

    private Dialog<Boolean> cardDialog;

    /**
     * The name of the card.
     */
    private String cardName;

    private String realName;

    private final double ratio;

    /**
     * The handler to be called upon card selection.
     */
    private Consumer<String> handler;

    /**
     * Creates a new Card instance.
     *
     * @param container The HBox where the card is held.
     * @param cardName  The name of the card to be displayed.
     */
    public Card(HBox container, String cardName, double ratio) {
        this.ratio = ratio;
        this.container = container;
        realName = cardName;
        this.cardName = cardName.replaceAll("\\s+", "");
        initialize();
    }

    /**
     * Sets the handler called during click events.
     * <p>It also enables click events for this card.</p>
     *
     * @param handler The handler to be called during click events.
     */
    public void setOnClick(Consumer<String> handler) {
        this.handler = handler;
    }

    /**
     * Event handler for click events.
     * <p>It delegates the handler.</p>
     *
     * @param event The mouse event.
     */
    private void onClick(MouseEvent event) {
        boolean use = getZoomDialog().showAndWait().orElse(false);
        if (use && handler != null)
            handler.accept(realName);
    }

    /**
     * Loads the card image.
     *
     * @implNote Use a url manager instead of crafting the url.
     */
    private void initialize() {
        url =
                this.getClass().getResource("images/cards/" + cardName + ".jpg").toString();
        cardImage.setStyle(
                "-fx-background-image:url('" + url + "');" +
                        "-fx-background-position: center center;" +
                        "-fx-background-repeat: stretch;" +
                        "-fx-background-size: cover;");
        fitImage();
        cardImage.setOnMouseClicked(this::onClick);
    }

    /**
     * Fits the image according to the container.
     */
    private void fitImage() {
        cardImage.minHeightProperty().bind(cardImage.minWidthProperty().multiply(ratio));
        cardImage.maxHeightProperty().bind(cardImage.maxWidthProperty().multiply(ratio));

        cardImage.minWidthProperty().bind(
                Bindings.min(
                        container.widthProperty().multiply(0.3),
                        container.heightProperty().multiply(0.7)
                )
        );
        cardImage.maxWidthProperty().bind(
                Bindings.min(
                        container.widthProperty().multiply(0.3),
                        container.heightProperty().multiply(0.7)
                )
        );

        HBox.setHgrow(cardImage, Priority.ALWAYS);
    }

    public void insert() {
        container.getChildren().add(cardImage);
    }

    private Dialog<Boolean> getZoomDialog(){
        cardDialog = new Dialog<>();
        DialogPane pane = new DialogPane();
        cardDialog.setDialogPane(pane);
        BorderPane borderPane = new BorderPane();
        pane.setContent(borderPane);
        ImageView imageView = new ImageView(new Image(url, MAX_X, MAX_X * ratio, true, false));
        borderPane.setCenter(imageView);
        ButtonBar buttonBar = new ButtonBar();
        Button close = new Button("Close");
        close.setOnMouseClicked(this::close);
        buttonBar.getButtons().add(close);
        if(handler != null) {
            Button use = new Button("Use");
            use.setOnMouseClicked(this::use);
            buttonBar.getButtons().add(use);
        }
        buttonBar.setPadding(new Insets(15,0,0,0));
        borderPane.setBottom(buttonBar);
        return cardDialog;
    }

    private void close(MouseEvent event){
        cardDialog.setResult(false);
    }
    private void use(MouseEvent event){
        cardDialog.setResult(true);
    }
}
