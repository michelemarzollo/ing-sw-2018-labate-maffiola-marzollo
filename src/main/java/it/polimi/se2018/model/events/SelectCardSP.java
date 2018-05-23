package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

/**
 * This class is used to encapsulate the data needed to
 * select a {@link it.polimi.se2018.model.ToolCard} that
 * the Player would like to use in SinglePlayer mode.
 * The class extends {@link SelectCard} because in SinglePlayer
 * mode it is requested also to spend a Die from the {@link it.polimi.se2018.model.DraftPool}
 * to use a {@link it.polimi.se2018.model.ToolCard} and this message
 * contains this additional data.
 */

public class SelectCardSP extends SelectCard{

    /**
     * The {@link it.polimi.se2018.model.DraftPool}'s index of the Die that the
     * player wishes to spend to use the indicated {@link it.polimi.se2018.model.ToolCard}.
     */
    private int dieIndex;
    /**
     * Constructor of the class.
     * @param name The name of the selected card.
     * @param view The view reference.
     * @param action The action that the Player wants
 *                   to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     * @param dieIndex The index of the selected Die.
     */
    public SelectCardSP(String name, View view, Action action, String playerName, int dieIndex) {
        super(name, view, action, playerName);
        this.dieIndex = dieIndex;
    }

    /**
     * Getter for the position of the selected Die.
     * @return The index of the selected Die in the {@link it.polimi.se2018.model.DraftPool}.
     */
    public int getDieIndex() {
        return dieIndex;
    }
}
