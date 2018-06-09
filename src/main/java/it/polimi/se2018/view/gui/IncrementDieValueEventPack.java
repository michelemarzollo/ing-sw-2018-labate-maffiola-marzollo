package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Set of events that are used when the player has to choose a die and
 * whether to increment or decrement it.
 */
public class IncrementDieValueEventPack extends BoardEventPack {

    /**
     * The dialog used to ask the player whether to increment or decrement,
     */
    private final IncrementDialog dialog;

    /**
     * Creates a new instance that uses the specified client view as request handler
     * and the given dialog to interact with the user.
     * @param clientView The client view responsible for requests.
     * @param dialog The increment dialog to use.
     */
    public IncrementDieValueEventPack(ClientView clientView, IncrementDialog dialog) {
        super(clientView);
        this.dialog = dialog;
    }

    /**
     * Asks the user to select to increment or decrement the value of the selected die.
     * <p>After user response, it makes a request through the client view.</p>
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        dialog.askUser();
        getClientView().handleToolCardUsage(index, dialog.isIncrement());
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
        board.getPlayerPatternContainer().setDisable(true);
        board.getDraftPoolContainer().setDisable(false);
        board.getToolCardContainer().setDisable(true);
    }
}
