package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.ViewMessage;

/**
 * This interface represents the behavior of a tool card.
 */
public interface ToolCardBehaviour {

    /**
     * Tells if the requirements to activate the tool card are met in the game.
     *
     * @param game The game the tool card will be applied to.
     * @return {@code true} if all requirements are met; {@code false} otherwise.
     */
    boolean areRequirementsSatisfied(Game game);

    /**
     * Selects the correct view to gather the parameters the tool card
     * needs to be used.
     *
     * @param message The message sent by the view.
     */
    void askParameters(ViewMessage message);

    /**
     * Applies the effect of the tool card to the game.
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    ToolCardBehaviourResponse useToolCard(Game game, ViewMessage message);
}
