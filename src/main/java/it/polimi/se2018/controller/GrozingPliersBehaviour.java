package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.DieValueException;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.IncrementDieValue;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Logger;

import java.util.List;

/**
 * Represents the 'GrozingPliers' {@link it.polimi.se2018.model.ToolCard}.
 * The effect of this card is to increase or decrease the value of the drafted
 * die. Using this card the player is forced to place the chosen die if he wishes
 * to place a die.
 * To use this card, the index of the chosen die in the {@link it.polimi.se2018.model.DraftPool}
 * is required ({@code message} will contain the data required).
 */
public class GrozingPliersBehaviour implements ToolCardBehaviour {

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
     * Selects the view to let the user select a die from the draft pool.
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showDieIncrementSelection();
    }

    /**
     * Increment or decrement the chosen die's value and forces the user to only use
     * that die for the rest of his turn.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by the view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {
        IncrementDieValue msg = (IncrementDieValue) message;
        int dieIndex = msg.getDieIndex();
        List<Die> dice = game.getDraftPool().getDice();
        try {
            Die selectedDie = dice.remove(dieIndex);
            if (msg.isIncrement()) {
                selectedDie = selectedDie.increase();
            } else {
                selectedDie = selectedDie.decrease();
            }
            dice.add(selectedDie);
            //The draftPool is actually modified only if the increment has been successful.
            game.getDraftPool().setDice(dice);
            int index = dice.indexOf(selectedDie);
            game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(index);
        }
        catch(DieValueException ex){
            Logger.getDefaultLogger().log(ex.getMessage());
            msg.getView().showError(ex.getMessage());
            return false;
        }
        catch(IndexOutOfBoundsException ex){
            Logger.getDefaultLogger().log(ex.getMessage());
            msg.getView().showError("The index of the selected die is not valid");
            return false;
        }

        return true;
    }
}
