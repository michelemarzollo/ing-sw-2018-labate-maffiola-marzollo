package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.DieValueException;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.IncrementDieValue;
import it.polimi.se2018.model.events.ViewMessage;

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
     * Selects the view to let the user select a die from the draft pool.
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showDieSelection();
    }

    /**
     * Increment or decrement the chosen die's value and forces the user to only use
     * that die for the rest of his turn.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by the view.
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {
        IncrementDieValue msg = (IncrementDieValue) message;
        int dieIndex = msg.getDieIndex();
        List<Die> dice = game.getDraftPool().getDice();
        try {
            Die selectedDie = dice.remove(dieIndex);
            if (msg.isIncrement()) {
                dice.add(selectedDie.increase());
                //The draftPool is actually modified only if the increment has been successful.
                game.getDraftPool().setDice(dice);
            } else {
                dice.add(selectedDie.decrease());
                //The draftPool is actually modified only if the decrement has been successful.
                game.getDraftPool().setDice(dice);
            }
            int index = dice.indexOf(selectedDie);
            game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(index);
        }
        catch(DieValueException ex){
            msg.getView().showError(ex.getMessage());
        }
        catch(IndexOutOfBoundsException ex){
            msg.getView().showError("The index of the selected die is not valid");
        }
    }
}
