package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

/**
 * This class is used to encapsulate the data needed to
 * select a {@link it.polimi.se2018.model.ToolCard} that
 * the Player would like to use or to choose his {@link it.polimi.se2018.model.Pattern}
 * among their candidates.
 */
public class SelectCard extends ViewMessage{

    /**
     * The name of the selected card.
     */
    private String name;

    /**
     * Constructor of the class.
     * @param name The name of the selected card.
     * @param view The view reference.
     * @param action The action that the Player wants
     *               to perform.
     * @param playerName The name of the player that
     *                   is performing the action.
     */
    public SelectCard(String name,
                      View view, Action action, String playerName) {
        super(view, action, playerName);
        this.name = name;
    }

    /**
     * Getter for the name of the selected card.
     * @return The name of the selected card.
     */
    public String getName() {
        return name;
    }
}
