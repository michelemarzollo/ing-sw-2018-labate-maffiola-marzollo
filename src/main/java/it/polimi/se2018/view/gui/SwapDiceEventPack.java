package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Set of events used when the player has to select a die from the draft pool
 * and a die from the round track.
 */
public class SwapDiceEventPack extends BoardEventPack {

    /**
     * The index of the selected die from the draft pool.
     */
    private int index = -1;
    /**
     * The coordinates of the selected die from the round track.
     */
    private Coordinates roundTrackSelection;


    /**
     * Creates a new instance that uses the specified client view to handle
     * requests.
     * @param clientView The client view responsible for requests.
     */
    public SwapDiceEventPack(ClientView clientView) {
        super(clientView);
    }

    /**
     * Checks if all input has been gathered and, if so, makes a request.
     * <p>After the request the collected data are reset.</p>
     */
    private void trySwap() {
        if (index != -1 && roundTrackSelection != null) {
            getClientView().handleToolCardUsage(index, roundTrackSelection);
            reset();
        }
    }

    /**
     * Registers the selected index and tries to do the swap.
     * <p>The swap is successful only if all data have been collected.</p>
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        this.index = index;
        trySwap();
    }

    /**
     * Registers the selected coordinates and tries to do the swap.
     * <p>The swap is successful only if all data have been collected.</p>
     * @param coordinates The coordinates of the selected die.
     */
    @Override
    public void roundTrackHandler(Coordinates coordinates) {
        roundTrackSelection = coordinates;
        trySwap();
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
        board.getPlayerPatternContainer().setDisable(true);
        board.getDraftPoolContainer().setDisable(false);
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

    /**
     * Resets all collected input.
     */
    private void reset() {
        index = -1;
        roundTrackSelection = null;
    }
}
