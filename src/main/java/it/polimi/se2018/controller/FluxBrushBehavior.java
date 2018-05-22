package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;

/**
 * Represents the "Flux Brush" tool card.
 * <p>The effect of this tool card is to re-roll a die in the draft pool.
 * After the roll the user is forced to only use that die for the rest of
 * his turn.</p>
 * <p>This tool card requires the index of the die in the draft pool
 * to be applied.</p>
 */
public class FluxBrushBehavior implements ToolCardBehavior {

    /**
     * Selects the view to let the user select a die from the draft pool.
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showDieSelection();
    }

    /**
     * Re-rolls a die and forces the user to only use that die for the rest
     * of his turn.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by th view.
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {
        SelectDie selectDie = (SelectDie) message;
        try{
            List<Die> draftPool = game.getDraftPool().getDice();
            Die rerolledDie = draftPool.remove(selectDie.getDieIndex()).roll();
            draftPool.add(rerolledDie);
            game.getDraftPool().setDice(draftPool);

            // force next selection
            game.getDraftPool().setForcedSelection(draftPool.indexOf(rerolledDie));

        } catch (IndexOutOfBoundsException e){
            selectDie.getView().showError("Invalid selection: index too big!");
        }
    }
}
