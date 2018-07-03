package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.DieValueException;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.IncrementDieValue;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;

/**
 * This class manges the usage of the tool cards that alter the die value.
 *
 * @author dvdmff
 */
public class AlterDieValueBehaviour implements ToolCardBehaviour {

    /**
     * Flag to indicate if the die haas to be incremented/decremented or flipped.
     */
    private final boolean isIncrement;

    /**
     * Creates a new instance that will flip or increment/decrement the die,
     * depending on the parameter.
     *
     * @param isIncrement {@code true} if the die has to be incremented/decremented;
     *                    {@code false} if it has to be flipped.
     */
    public AlterDieValueBehaviour(boolean isIncrement) {
        this.isIncrement = isIncrement;
    }

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
     * Selects the view to allow the user to select a die.
     * <p>In the case an increment/decrement operation is required, a more specific view
     * is selected.</p>
     *
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        if (isIncrement)
            message.getView().showDieIncrementSelection();
        else
            message.getView().showDieSelection();
    }

    /**
     * Alters the value of the die according to the configuration.
     * <p>If {@code isIncrement} flag is set the action to perform is read from the message,
     * else the die is flipped.</p>
     *
     * @param die     The die to alter.
     * @param message The message sent by the view.
     * @return The altered die.
     */
    private Die alterDie(Die die, SelectDie message) {
        if (isIncrement) {
            IncrementDieValue incrementDieValue = (IncrementDieValue) message;
            if (incrementDieValue.isIncrement())
                return die.increase();
            else
                return die.decrease();
        } else
            return die.flip();
    }

    /**
     * Alters the value of the selected die and puts it in the draft pool.
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     * @return {@code true} on success; {@code false} otherwise.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {
        SelectDie selectMessage = (SelectDie) message;
        try {
            List<Die> dice = game.getDraftPool().getDice();

            Die die = dice.get(selectMessage.getDieIndex());
            die = alterDie(die, selectMessage);
            dice.set(selectMessage.getDieIndex(), die);

            game.getDraftPool().setDice(dice);

            //Setting of the forced selection in the current turn
            game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(
                    selectMessage.getDieIndex());
            return true;
        } catch (DieValueException ex) {
            selectMessage.getView().showError(ex.getMessage());
        } catch (IndexOutOfBoundsException e) {
            selectMessage.getView().showError("Bad index!");
        }
        return false;
    }
}
