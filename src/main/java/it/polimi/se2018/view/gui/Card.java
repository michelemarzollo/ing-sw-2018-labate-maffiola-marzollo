package it.polimi.se2018.view.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

/**
 * This class handles the displaying of a card.
 * @author dvdmff
 */
public class Card {

    /**
     * The container holding the card.
     */
    private HBox container;

    /**
     * The image view holding the card image.
     */
    private ImageView cardImage = new ImageView();

    /**
     * The name of the card.
     */
    private String cardName;

    /**
     * The handler to be called upon card selection.
     */
    private Consumer<String> handler;

    /**
     * Creates a new Card instance.
     * @param container The HBox where the card is held.
     * @param cardName The name of the card to be displayed.
     */
    public Card(HBox container, String cardName){
        this.container = container;
        this.cardName = cardName;
        initialize();
    }

    /**
     * Sets the handler called during click events.
     * <p>It also enables click events for this card.</p>
     * @param handler The handler to be called during click events.
     */
    public void setOnClick(Consumer<String> handler){
        this.handler = handler;
        cardImage.setOnMouseClicked(this::onClick);
    }

    /**
     * Event handler for click events.
     * <p>It delegates the handler.</p>
     * @param event The mouse event.
     */
    private void onClick(MouseEvent event){
        if(handler != null)
            handler.accept(cardName);
    }

    /**
     * Loads the card image.
     * @implNote Use a url manager instead of crafting the url.
     */
    private void initialize(){
        Image image = new Image(
                this.getClass().getResource("cards/ " + cardName + ".jpg").toString());
        cardImage.setImage(image);
        fitImage();
    }

    /**
     * Fits the image according to the container.
     */
    private void fitImage() {
        cardImage.setFitHeight(container.getPrefHeight());
    }

    /**
     * Returns an image view representing the card.
     * @return An image view representing the card.
     */
    public ImageView getCard(){
        return cardImage;
    }
}
