package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;

/**
 * The class to describe the behaviour of the Grinding Stone ToolCard
 * {@link it.polimi.se2018.model.ToolCard}. (Tool Card 10)
 * <p>
 * The ToolCard allows to flip to the opposite face the die drafted from
 * the {@link it.polimi.se2018.model.DraftPool}.</p>
 */
public class GrindingStoneBehaviour implements ToolCardBehaviour {

    /**
     * Tells if the tool card can be applied.
     * <p>This tool card can only be applied if the player hasn't placed a die yet.</p>
     *
     * @param game The game the tool card will be applied to.
     * @return {@code true} if the player hasn't already placed a die;
     * {@code false} otherwise.
     */
    @Override
    public boolean areRequirementsSatisfied(Game game) {
        return !game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie();
    }

    /**
     * Selects the correct view to gather the parameters the tool card
     * needs to be used.
     *
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showDieSelection();
    }

    /**
     * The action associated to the Grinding Stone ToolCard.
     * <p>
     * The die is flipped and left in the DraftPool. The attribute
     * {@code forcedSelection} in the current turn is set to have the index of
     * the die that was just flipped.</p>
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {

        SelectDie selectMessage = (SelectDie) message;
        try {
            //Update of the DraftPool inserting the flipped die
            List<Die> dice = game.getDraftPool().getDice();

            Die die = dice.get(selectMessage.getDieIndex());
            dice.remove(selectMessage.getDieIndex());
            die = die.flip();
            dice.add(selectMessage.getDieIndex(), die);

            game.getDraftPool().setDice(dice);

            //Setting of the forced selection in the current turn
            game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(
                    selectMessage.getDieIndex());
        } catch (IndexOutOfBoundsException e) {
            selectMessage.getView().showError("Bad index!");
            return false;
        }

        return true;
    }
}
