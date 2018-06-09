package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Set of events used when the player has to select a die from the draft pool.
 */
public class SelectDieEventPack extends BoardEventPack {

    /**
     * Creates a new instance that uses the specified client view to handle
     * requests.
     * @param clientView The client view responsible for requests.
     */
    public SelectDieEventPack(ClientView clientView) {
        super(clientView);
    }

    /**
     * Makes a request through the client view.
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        getClientView().handleToolCardUsage(index);
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
     * Does nothing.
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        //It can't be used
    }

    /**
     * Disables every control but the draft pool.
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        board.getDraftPoolContainer().setDisable(false);
        board.getPlayerPatternContainer().setDisable(true);
        board.getToolCardContainer().setDisable(true);
    }

    /**
     * Does nothing.
     * @param cardName The name of the tool card.
     */
    @Override
    public void toolCardHandler(String cardName) {
        //It can't be used
    }
}
