package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * This class holds some events that are changed often in the GameBoard class.
 * @author dvdmff
 */
public abstract class BoardEventPack {

    /**
     * The client view responsible for the client.
     */
    private final ClientView clientView;

    /**
     * Creates a new BoardEventPack that uses the specified client view.
     * @param clientView The client view responsible for the client.
     */
    public BoardEventPack(ClientView clientView) {
        this.clientView = clientView;
    }

    /**
     * This method is called when a die from the draft pool is selected.
     * @param index The index of the selected die.
     */
    public abstract void draftPoolHandler(int index);

    /**
     * This method is called when a die from the round track is selected.
     * @param coordinates The coordinates of the selected die.
     */
    public abstract void roundTrackHandler(Coordinates coordinates);

    /**
     * This method is called when a cell from the player pattern is selected.
     * @param coordinates The coordinates of the selected cell.
     */
    public abstract void patternHandler(Coordinates coordinates);

    /**
     * Enables or disables some controls coherently to what actions can be performed.
     * @param board The GameBoard instance displaying the game.
     */
    public abstract void prepareControls(GameBoard board);

    /**
     * This method is called when a tool card is selected.
     * @param cardName The name of the tool card.
     */
    public abstract void toolCardHandler(String cardName);

    /**
     * Getter for the client view that is responsible for this client.
     * @return The client view that is responsible for this client.
     */
    ClientView getClientView() {
        return clientView;
    }
}
