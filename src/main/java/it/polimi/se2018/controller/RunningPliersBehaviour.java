package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.PlaceDie;
import it.polimi.se2018.model.events.ViewMessage;

/**
 * The class to describe the behaviour of the Running Pliers ToolCard
 * {@link it.polimi.se2018.model.ToolCard}. (Tool Card 8)
 * <p>
 * The ToolCard allows to draft and place consecutively two dices form
 * the DraftPool.</p>
 * <p>
 * If used, the ToolCard makes the player loose his second turn.</p>
 */
public class RunningPliersBehaviour implements ToolCardBehaviour {

    /**
     * Tells if the tool card can be applied.
     * <p>This tool card can only be applied if the player has a second
     * turn this round.</p>
     *
     * @param game The game the tool card will be applied to.
     * @return {@code true} if the player has a second turn this round.
     */
    @Override
    public boolean areRequirementsSatisfied(Game game) {
        return game.getTurnManager().isSecondTurnAvailable();
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
     * The action associated to the Running Pliers ToolCard.
     * <p>
     * After the first part of the turn the method allows the player
     * to draft another die from the {@link DraftPool} and place it in the grid.</p>
     * <p>
     * The player is not allowed to make the second turn during the same round
     * </p>
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {

        PlaceDie placeMessage = (PlaceDie) message;

        try {

            Die die = game.getDraftPool().select(placeMessage.getDieIndex());

            Turn currentTurn = game.getTurnManager().getCurrentTurn();

            // place die
            Pattern currentPattern = currentTurn.getPlayer().getPattern();
            Pattern newPattern = currentPattern.placeDie(die, placeMessage.getDestination());
            currentTurn.getPlayer().setPattern(newPattern);

            game.getDraftPool().draft(placeMessage.getDieIndex());
            currentTurn.placeDie();

            blockSecondTurn(game);
            return true;

        } catch (IndexOutOfBoundsException e) {
            placeMessage.getView().showError("Invalid selection!");

        } catch (PlacementErrorException e) {
            placeMessage.getView().showError(
                    "Placement doesn't respect restrictions!\n" + e.getMessage()
            );
        }
        return false;
    }

    private void blockSecondTurn(Game game) {
        try {
            game.getTurnManager().consumeSecondTurn(
                    game.getTurnManager().getCurrentTurn().getPlayer()
            );
        } catch (TurnManager.SecondTurnUnavailableException ignored) {
            // cannot happen
        }
    }
}
