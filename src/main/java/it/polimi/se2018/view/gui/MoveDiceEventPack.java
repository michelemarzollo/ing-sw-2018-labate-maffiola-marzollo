package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Set of events used when the player has to move two dices on its pattern.
 */
public class MoveDiceEventPack extends BoardEventPack {

    /**
     * The selected source positions.
     */
    private Coordinates[] sources;
    /**
     * The selected destination positions.
     */
    private Coordinates[] destinations;

    /**
     * Creates a new instance that uses the specified client view to handle
     * requests.
     * @param clientView The client view responsible for requests.
     */
    public MoveDiceEventPack(ClientView clientView) {
        super(clientView);
        reset();
    }

    /**
     * Does noting.
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        //It can't be used
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
     * Gathers user position selections.
     * <p>The order in which selections are registered is: 1st source, 1st destination,
     * 2nd source, 2nd destination.</p>
     * <p>After all selections are collected, the client view is notified and
     * the data reset.</p>
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        if(sources[0] == null)
            sources[0] = coordinates;
        else if(destinations[0] == null)
            destinations[0] = coordinates;
        else if(sources[1] == null)
            sources[1] = coordinates;
        else {
            destinations[1] = coordinates;
            getClientView().handleToolCardUsage(sources, destinations);
            reset();
        }
    }

    /**
     * Disables every control but the player pattern.
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        board.getPlayerPatternContainer().setDisable(false);
        board.getDraftPoolContainer().setDisable(true);
        board.getToolCardContainer().setDisable(true);
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
     * Resets the collected player selections.
     */
    private void reset() {
        sources = new Coordinates[2];
        destinations = new Coordinates[2];
    }
}
