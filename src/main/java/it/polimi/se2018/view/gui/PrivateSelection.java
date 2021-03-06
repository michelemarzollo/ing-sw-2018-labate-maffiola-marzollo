package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * There class to make the player handle the selection of the
 * {@link it.polimi.se2018.model.PrivateObjectiveCard} at the beginning of the single-player
 * game.
 *
 * @author michelemarzollo
 */
public class PrivateSelection {

    /**
     * The path were to find the cards to show.
     */
    private static final String PATH = "images/cards/";

    /**
     * The extension of the files that contain the images of the cards.
     */
    private static final String EXTENSION = ".jpg";

    /**
     * The message to show after the selection.
     */
    private static final String WAITING_REASON = "Calculating the score...";

    /**
     * The main controller of the gui.
     */
    private JavaFxDisplayer parentController;

    /**
     * The name of the card that will be placed on the left in the screen.
     */
    private String leftCardName;

    /**
     * The name of the card that will be placed on the right in the screen.
     */
    private String rightCardName;

    /**
     * The {@link ImageView} that contains the {@link Image} of the left
     * card.
     */
    @FXML
    private ImageView leftCard;

    /**
     * The {@link ImageView} that contains the {@link Image} of the right
     * card.
     */
    @FXML
    private ImageView rightCard;

    /**
     * The setter for the {@code parentController}.
     *
     * @param parentController the main controller of the gui.
     */
    public void setParentController(JavaFxDisplayer parentController) {
        this.parentController = parentController;
    }

    /**
     * The action to do when the left card is clicked:
     * the choice must be confirmed by the user, and than the method to
     * send the message to the {@link it.polimi.se2018.controller.SinglePlayerController}
     * on the {@link it.polimi.se2018.view.ClientView} is called.
     */
    @FXML
    public void selectLeftCard() {
        boolean confirmed = parentController.displayConfirm(
                "You chose the " + leftCardName + " card.");
        if (confirmed) {
            parentController.displayWaitingView(WAITING_REASON);
            parentController.getView().handlePrivateSelection(leftCardName);
        } else parentController.askPrivateObjective();
    }

    /**
     * The action to do when the right card is clicked:
     * the choice must be confirmed by the user, and than the method to
     * send the message to the {@link it.polimi.se2018.controller.SinglePlayerController}
     * on the {@link it.polimi.se2018.view.ClientView} is called.
     */
    @FXML
    public void selectRightCard() {
        boolean confirmed = parentController.displayConfirm(
                "You chose the " + rightCardName + " card.");
        if (confirmed) {
            parentController.displayWaitingView(WAITING_REASON);
            parentController.getView().handlePrivateSelection(rightCardName);
        } else parentController.askPrivateObjective();
    }

    /**
     * The method  to set the images of the cards on the {@link ImageView}.
     * The information of which cards to show is contained in the
     * {@link ViewDataOrganizer}, whose reference is contained
     * in {@code parentController}.
     */
    public void setCards() {

        leftCardName = parentController.
                getDataOrganizer().getGameSetup().getPrivateObjectives()[0][0].getName();
        rightCardName = parentController.
                getDataOrganizer().getGameSetup().getPrivateObjectives()[0][1].getName();

        Image image1 = new Image(getClass().getResourceAsStream(PATH + leftCardName + EXTENSION));
        leftCard.setImage(image1);

        Image image2 = new Image(getClass().getResourceAsStream(PATH + rightCardName + EXTENSION));
        rightCard.setImage(image2);

    }

}
