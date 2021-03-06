package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;

/**
 * This class manages the usage of tool cards that re-rolls a die.
 *
 * @author dvdmff
 */
public class ReRollDieBehaviour implements ToolCardBehaviour {

    /**
     * Tells if the requirements for the usage are met.
     * <p>This behaviour can only be used if the player has not placed a die yet.</p>
     *
     * @param game The game the tool card will be applied to.
     * @return {@code true} if the requirements are met; {@code false} otherwise.
     */
    @Override
    public boolean areRequirementsSatisfied(Game game) {
        return !game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie();

    }

    /**
     * Selects the view to let the user select a die from the draft pool.
     *
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showDieSelection();
    }

    /**
     * Re-rolls a die and forces the user to only use that die for the rest
     * of his turn.
     *
     * @param game    The game the effect has to be applied to.
     * @param message The message sent by th view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public ToolCardBehaviourResponse useToolCard(Game game, ViewMessage message) {
        SelectDie selectDie = (SelectDie) message;
        try {
            List<Die> draftPool = game.getDraftPool().getDice();
            Die rerolledDie = draftPool.get(selectDie.getDieIndex()).roll();
            draftPool.set(selectDie.getDieIndex(), rerolledDie);
            game.getDraftPool().setDice(draftPool);

            // force next selection
            int index = draftPool.indexOf(rerolledDie);
            game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(index);

        } catch (IndexOutOfBoundsException e) {
            selectDie.getView().showError("Invalid selection: index too big!");
            return ToolCardBehaviourResponse.FAILURE;
        }
        return ToolCardBehaviourResponse.SUCCESS;
    }
}
