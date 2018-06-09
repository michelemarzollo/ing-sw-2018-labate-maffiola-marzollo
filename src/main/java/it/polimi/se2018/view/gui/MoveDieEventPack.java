package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Set of events used when the player has to move one die on its pattern.
 */
public class MoveDieEventPack extends BoardEventPack {

    /**
     * The selected source coordinates.
     */
    private Coordinates source;

    /**
     * Creates a new instance that uses the specified client view to handle
     * requests.
     * @param clientView The client view responsible for requests.
     */
    public MoveDieEventPack(ClientView clientView) {
        super(clientView);
    }

    /**
     * Does nothing.
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        //It can't be used
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
     * Gathers user position selections.
     * <p>The order in which selections are registered is: 1st source, 1st destination.</p>
     * <p>After all selections are collected, the client view is notified and
     * the data reset.</p>
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        if(source == null)
            source = coordinates;
        else{
            getClientView().handleToolCardUsage(source, coordinates);
            source = null;
        }
    }

    /**
     * Disables every control but the player pattern.
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        board.getDraftPoolContainer().setDisable(true);
        board.getPlayerPatternContainer().setDisable(false);
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
