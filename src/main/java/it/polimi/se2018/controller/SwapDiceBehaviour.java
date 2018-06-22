package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.DiceSwap;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;

/**
 * This class manages the usage of tool cards that swap a die from the draft pool with
 * one from the round track.
 *
 * @author michelemarzollo
 */
public class SwapDiceBehaviour implements ToolCardBehaviour {

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
        message.getView().showLensCutterSelection();
    }

    /**
     * Swaps the selected dice.
     * <p>
     * The chosen die of the {@link it.polimi.se2018.model.DraftPool} is
     * swapped with the chosen die of the {@link it.polimi.se2018.model.RoundTrack}
     * (the information for the index and the Coordinates is in the message). It's set
     * the forced selection of the drafted die for the next placement in the Pattern.</p>
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {

        DiceSwap swapMessage = (DiceSwap) message;

        Die fromDraftPool = game.getDraftPool().select(swapMessage.getSourceIndex());

        try {
            Die fromRoundTrack = game.getRoundTrack().swap(swapMessage.getDestination(),
                    fromDraftPool);
            if (fromRoundTrack != null) {
                //Update of the DraftPool inserting the die from the RoundTrack
                List<Die> dice = game.getDraftPool().getDice();
                dice.remove(swapMessage.getSourceIndex());
                dice.add(swapMessage.getSourceIndex(), fromRoundTrack);
                game.getDraftPool().setDice(dice);
                //Setting of the forced selection in the current turn
                game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(
                        swapMessage.getSourceIndex());
                return true;
            } else {
                swapMessage.getView().showError(
                        "There is no die in that position in the Round Track!"
                );
            }
        } catch (IndexOutOfBoundsException e) {
            swapMessage.getView().showError("There is no die in that position in the Draft Pool!");
        }
        return false;
    }
}
