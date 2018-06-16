package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Turn;
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
     * Tells if the tool card can be applied.
     * <p>This tool card can only be applied if the player hasn't placed a die yet
     * and it's his second turn.</p>
     *
     * @param game The game the tool card will be applied to.
     * @return {@code true} if the player hasn't already placed a die and it's his
     * second turn; {@code false} otherwise.
     */
    @Override
    public boolean areRequirementsSatisfied(Game game) {
        Turn currentTurn = game.getTurnManager().getCurrentTurn();
        return !currentTurn.hasAlreadyPlacedDie() &&
                !game.getTurnManager().isSecondTurnAvailable();
    }

    /**
     * Does nothing.
     * <p>The card doesn't require parameters.</p>
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showConfirm();
    }

    /**
     * Re-rolls all dice in the draft pool.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by th view.
     * @return Always {@code true}.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {
        List<Die> newDraftPool = game.getDraftPool().getDice().stream()
                .map(Die::roll)
                .collect(Collectors.toList());
        game.getDraftPool().setDice(newDraftPool);
        return true;
    }
}
