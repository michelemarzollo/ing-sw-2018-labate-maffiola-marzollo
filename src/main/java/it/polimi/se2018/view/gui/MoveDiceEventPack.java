package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Set of events used when the player has to move two dices on its pattern.
 */
public class MoveDiceEventPack extends BoardEventPack {

    /**
     * The selected source positions.
     */
    private List<Coordinates> sources = new ArrayList<>();
    /**
     * The selected destination positions.
     */
    private List<Coordinates> destinations = new ArrayList<>();

    /**
     * Index of the last selected source-destination pair.
     */
    private int currentIndex = 0;

    /**
     * Flag used to tell if the max amount of dice has to be moved.
     */
    private boolean moveAll;

    /**
     * The amount of dice to move.
     */
    private int amount;

    /**
     * Reference to the game board.
     */
    private GameBoard board;

    /**
     * Creates a new instance that uses the specified client view to handle
     * requests.
     *
     * @param clientView The client view responsible for requests.
     * @param amount The max amount of dice to move.
     * @param moveAll {@code true} if exactly {@code amount} dice have to be moved.
     */
    public MoveDiceEventPack(ClientView clientView, int amount, boolean moveAll) {
        super(clientView);
        this.amount = amount;
        this.moveAll = moveAll;
        reset();
    }

    /**
     * Does noting.
     *
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        //It can't be used
    }

    /**
     * Does noting.
     *
     * @param coordinates The coordinates of the selected die.
     */
    @Override
    public void roundTrackHandler(Coordinates coordinates) {
        //It can't be used
    }

    /**
     * Gathers user position selections.
     * <p>The order in which selections are registered is: 1st source, 1st destination,
     * 2nd source, 2nd destination.</p>
     * <p>After all selections are collected, the client view is notified and
     * the data reset.</p>
     *
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        boolean goOn = true;
        if (sources.size() == currentIndex)
            sources.add(coordinates);
        else if (destinations.size() == currentIndex) {
            destinations.add(coordinates);
            ++currentIndex;
            if (currentIndex != amount && !moveAll)
                goOn = askContinue();
        }
        if (!goOn || currentIndex == amount) {
            board.restoreTurn();
            getClientView().handleToolCardUsage(
                    sources.toArray(new Coordinates[0]),
                    destinations.toArray(new Coordinates[0]));
            reset();
        }
    }

    /**
     * Asks the player to continue moving dice.
     *
     * @return {@code true} if the player chose to continue; {@code false} otherwise.
     */
    private boolean askContinue() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Move another die?");
        Optional<ButtonType> response = confirm.showAndWait();
        return response.orElse(ButtonType.CANCEL) == ButtonType.OK;
    }


    /**
     * Disables every control but the player pattern.
     *
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        this.board = board;
        board.getPlayerPatternContainer().setDisable(false);
        board.getDraftPoolContainer().setDisable(true);
        board.getToolCardContainer().setDisable(true);
    }

    /**
     * Does noting.
     *
     * @param cardName The name of the tool card.
     */
    @Override
    public void toolCardHandler(String cardName) {
        //It can't be used
    }

    /**
     * Resets the collected player selections.
     */
    private void reset() {
        sources.clear();
        destinations.clear();
    }
}
