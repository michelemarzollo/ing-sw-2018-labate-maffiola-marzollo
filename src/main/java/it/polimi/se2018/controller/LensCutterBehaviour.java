package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.DiceSwap;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.List;

/**
 * The class to describe the behaviour of the Lens Cutter ToolCard
 * {@link it.polimi.se2018.model.ToolCard}. (Tool Card 5)
 * <p>
 * The ToolCard allows to change a die from the {@link it.polimi.se2018.model.DraftPool}
 * with a die from the {@link it.polimi.se2018.model.RoundTrack}</p>
 */
public class LensCutterBehaviour implements ToolCardBehaviour {

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
     * The action associated to the Lens Cutter ToolCard.
     * <p>
     * The chosen die of the {@link it.polimi.se2018.model.DraftPool} is
     * swapped with the chosen die of the {@link it.polimi.se2018.model.RoundTrack}
     * (the information for the index and the Coordinates is in the message). It's set
     * the forced selection of the drafted die for the next placement in the Pattern.</p>
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {

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
            } else {
                swapMessage.getView().showError(
                        "There is no die in that position in the Round Track!"
                );
            }
        } catch (IndexOutOfBoundsException e) {
            swapMessage.getView().showError("There is no die in that position in the Draft Pool!");
        }

    }
}
