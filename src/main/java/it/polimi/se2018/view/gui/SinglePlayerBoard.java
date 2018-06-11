package it.polimi.se2018.view.gui;

/**
 * This class is a JavaFX controller for the single player game board.
 */
public class SinglePlayerBoard extends GameBoard {
    /**
     * Tells that the game is using the single player rule set.
     * @return Always {@code false}.
     */
    @Override
    boolean isMultiPlayer() {
        return false;
    }
}
