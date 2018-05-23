package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;

public class GrindingStoneBehaviour implements ToolCardBehavior {

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
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {

        SelectDie selectMessage = (SelectDie) message;

        //Update of the DraftPool inserting the flipped die
        List<Die> dice = game.getDraftPool().getDice();

        Die die = dice.get(selectMessage.getDieIndex());
        dice.remove(selectMessage.getDieIndex());
        die.flip();
        dice.add(selectMessage.getDieIndex(), die);

        game.getDraftPool().setDice(dice);

        //Setting of the forced selection in the current turn
        game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(
                selectMessage.getDieIndex());

    }
}
