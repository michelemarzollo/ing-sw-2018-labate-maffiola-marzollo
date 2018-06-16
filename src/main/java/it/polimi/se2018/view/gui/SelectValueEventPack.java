package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Set of events used when the player has to select a value for the die and place it
 * on its pattern.
 */
public class SelectValueEventPack extends BoardEventPack {

    /**
     * The colour of the die.
     */
    private final Colour colour;
    /**
     * The value of the die selected by the user.
     */
    private int value = -1;

    /**
     * Creates a new instance that uses the specified client view to handle
     * requests.
     * @param clientView The client view responsible for the client.
     * @param colour The colour of the die to select.
     */
    public SelectValueEventPack(ClientView clientView, Colour colour) {
        super(clientView);
        this.colour = colour;
    }

    /**
     * Registers the value of the selected die.
     * <p>By construction, the index of the die is the value of the die minus one.</p>
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        //Can't be used
    }

    private Dialog<Integer> makeDialog(){
        Dialog<Integer> diceDialog = new Dialog<>();
        DialogPane pane = new DialogPane();
        pane.setMinWidth(400);
        pane.setMinHeight(300);
        BorderPane borderPane = new BorderPane();
        pane.setContent(borderPane);
        diceDialog.setDialogPane(pane);
        HBox diceContainer = new HBox();
        borderPane.setCenter(diceContainer);
        DraftPoolFiller filler = new DraftPoolFiller(diceContainer);
        filler.setDice(getDice());
        filler.setSelectionHandler(i -> diceDialog.setResult(i + 1));
        return diceDialog;
    }

    /**
     * Does nothing.
     * @param coordinates The coordinates of the selected die.
     */
    @Override
    public void roundTrackHandler(Coordinates coordinates) {
        //It can't be used
    }

    /**
     * Makes a request through the client view, if the user already supplied a value for the die.
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        if(value != -1)
            getClientView().handleToolCardUsage(coordinates, value);
    }

    /**
     * Creates a list of die of the same colour with all the possible values.
     * <p>The list is ordered in ascending order.</p>
     * @return A list of die in ascending order of value of the same colour.
     */
    private List<Die> getDice(){
        List<Die> dice = new ArrayList<>();
        for(int i = 1; i <= 6; ++i)
            dice.add(new Die(i, new Random(), colour));
        return dice;
    }

    /**
     * Disables the tool card selection and fills the draft pool with dice of
     * the same colour and different values.
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        board.getToolCardContainer().setDisable(true);
        board.getDraftPoolContainer().setDisable(true);
        Optional<Integer> result = makeDialog().showAndWait();
        value = result.orElse(-1);
    }

    /**
     * Does noting.
     * @param cardName The name of the tool card.
     */
    @Override
    public void toolCardHandler(String cardName) {
        //It can't be used
    }
}
