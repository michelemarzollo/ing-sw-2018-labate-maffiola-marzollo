package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

public class PlaceDieEventPack extends BoardEventPack {

    /**
     * The index of the selected die.
     */
    private int index = -1;

    /**
     * Reference to the game board.
     */
    private GameBoard board;

    /**
     * Creates a new instance that uses the specified client view to handle
     * requests.
     *
     * @param clientView The client view responsible for requests.
     */
    public PlaceDieEventPack(ClientView clientView) {
        super(clientView);
    }

    /**
     * Registers the selected die index.
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        this.index = index;
    }

    /**
     * Does nothing.
     *
     * @param coordinates The coordinates of the selected die.
     */
    @Override
    public void roundTrackHandler(Coordinates coordinates) {
        //It can't be used.
    }

    /**
     * If a die has already been selected, makes a request to the client view
     * and resets the index.
     * <p>If it's not the case, it does nothing.</p>
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        if(index != -1){
            board.restoreTurn();
            getClientView().handleToolCardUsage(index, coordinates);
            index = -1;
        }
    }

    /**
     * Disables the tool card container.
     *
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        this.board = board;
        board.getToolCardContainer().setDisable(true);
        board.getPlayerPatternContainer().setDisable(false);
        board.getDraftPoolContainer().setDisable(false);
    }

    /**
     * Does nothing.
     *
     * @param cardName The name of the tool card.
     */
    @Override
    public void toolCardHandler(String cardName) {
        //It can't be used.
    }
}
