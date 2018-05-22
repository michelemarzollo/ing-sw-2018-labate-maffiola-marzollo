package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.ViewMessage;

/**
 * This interface represents the behavior of a tool card.
 */
public interface ToolCardBehavior {

    /**
     * Selects the correct view to gather the parameters the tool card
     * needs to be used.
     * @param message The message sent by the view.
     */
    void askParameters(ViewMessage message);

    /**
     * Applies the effect of the tool card to the game.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by th view.
     */
    void useToolCard(Game game, ViewMessage message);
}
