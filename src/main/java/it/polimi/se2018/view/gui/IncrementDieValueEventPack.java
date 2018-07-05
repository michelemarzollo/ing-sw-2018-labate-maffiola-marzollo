package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Set of events that are used when the player has to choose a die and
 * whether to increment or decrement it.
 */
public class IncrementDieValueEventPack extends BoardEventPack {

    private GameBoard board;

    /**
     * Creates a new instance that uses the specified client view as request handler
     * and the given dialog to interact with the user.
     * @param clientView The client view responsible for requests.
     */
    public IncrementDieValueEventPack(ClientView clientView) {
        super(clientView);
    }

    /**
     * Asks the user to select to increment or decrement the value of the selected die.
     * <p>After user response, it makes a request through the client view.</p>
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        ButtonType increment = new ButtonType("Increment");
        ButtonType decrement = new ButtonType("Decrement");
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "What do you want to do?",
                increment,
                decrement);

        Optional<ButtonType> response = alert.showAndWait();
        boolean isIncrement = response.orElse(increment) == increment;
        board.restoreTurn();
        getClientView().handleToolCardUsage(index, isIncrement);
    }

    /**
     * Does noting.
     * @param coordinates The coordinates of the selected die.
     */
    @Override
    public void roundTrackHandler(Coordinates coordinates) {
        //It can't be used
    }

    /**
     * Does noting.
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        //It can't be used
    }

    /**
     * Does noting.
     * @param cardName The name of the tool card.
     */
    @Override
    public void toolCardHandler(String cardName) {
        //It can't be used
    }

    /**
     * Disables everything but the draft pool.
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        this.board = board;
        board.getPlayerPatternContainer().setDisable(true);
        board.getDraftPoolContainer().setDisable(false);
        board.getToolCardContainer().setDisable(true);
    }
}
