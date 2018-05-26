package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

/**
 * This class is used to encapsulate the data needed
 * to select a Die in the {@link it.polimi.se2018.model.DraftPool}.
 * This action is needed when using 'Flux Brush', 'Flux Remover',
 * 'Grinding Stone' and 'Running Pliers'
 * {@link it.polimi.se2018.model.ToolCard}.
 */
public class SelectDie extends ViewMessage {

    /**
     * Index of the chosen Die in the DraftPool
     * that has been selected by the Player.
     */
    private int dieIndex;

    /**
     * Constructor of the class.
     *
     * @param dieIndex   The position of the selected Die
     *                   in the DraftPool.
     * @param view       The view reference.
     * @param action     The action that the Player wants
     *                   to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     */
    public SelectDie(int dieIndex,
                     View view, Action action, String playerName) {
        super(view, action, playerName);
        this.dieIndex = dieIndex;
    }

    /**
     * Getter for the position of the selected Die.
     *
     * @return The index of the selected Die in the DraftPool.
     */
    public int getDieIndex() {
        return dieIndex;
    }
}
