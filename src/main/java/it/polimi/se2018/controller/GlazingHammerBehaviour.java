package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the "Glazing Hammer" tool card.
 * <p>The effect of this tool card is to re-roll all dice that are
 * currently in the draft pool.</p>
 * <p>This tool card doesn't require any parameters to be applied.</p>
 */
public class GlazingHammerBehaviour implements ToolCardBehaviour {
    /**
     * Does nothing.
     * <p>The card doesn't require parameters.</p>
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        //do nothing
    }

    /**
     * Re-rolls all dice in the draft pool.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by th view.
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {
        List<Die> newDraftPool = game.getDraftPool().getDice().stream()
                .map(Die::roll)
                .collect(Collectors.toList());
        game.getDraftPool().setDice(newDraftPool);
    }
}
