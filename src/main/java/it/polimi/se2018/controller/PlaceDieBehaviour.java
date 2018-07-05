package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.PlaceDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Logger;

/**
 * This class manages the usage of tool cards that directly place a die.
 *
 * @author dvdmff
 */
public class PlaceDieBehaviour implements ToolCardBehaviour {

    /**
     * Flag to indicate if the second turn is to be used.
     */
    private final boolean useSecondTurn;
    /**
     * The restriction level to use when placing.
     */
    private final Restriction restriction;

    /**
     * Creates a new instance.
     *
     * @param useSecondTurn {@code true} if the second turn is to be used; {@code false} otherwise.
     * @param restriction   The restriction level to use when placing.
     */
    public PlaceDieBehaviour(boolean useSecondTurn, Restriction restriction) {
        this.useSecondTurn = useSecondTurn;
        this.restriction = restriction;
    }

    /**
     * Tells if the requirements to use the tool card are met.
     *
     * @param game The game the tool card will be applied to.
     * @return {@code true} if the tool card can be used; {@code false} otherwise.
     */
    @Override
    public boolean areRequirementsSatisfied(Game game) {
        Turn currentTurn = game.getTurnManager().getCurrentTurn();
        if (useSecondTurn)
            return currentTurn.hasAlreadyPlacedDie() && currentTurn.isSecondTurnAvailable();
        else
            return !currentTurn.hasAlreadyPlacedDie();
    }

    /**
     * Selects the view to allow the user to place a die.
     *
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showPlaceDie();
    }

    /**
     * Places a die in the specified position.
     * <p>This method will fail if the given coordinates are out of bound or if the requested
     * placement is illegal.</p>
     * <p>If {@code useSecondTurn} is set the second turn of the player is used to place the die,
     * else the current turn is used.</p>
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     * @return {@code true} on success; {@code false} otherwise.
     */
    @Override
    public ToolCardBehaviourResponse useToolCard(Game game, ViewMessage message) {

        PlaceDie placeDie = (PlaceDie) message;
        Die selectedDie = game.getDraftPool().select(placeDie.getDieIndex());
        Turn currentTurn = game.getTurnManager().getCurrentTurn();
        try {
            Pattern currentPattern = currentTurn.getPlayer().getPattern();
            Pattern newPattern =
                    currentPattern.placeDie(selectedDie, placeDie.getDestination(), restriction);
            currentTurn.getPlayer().setPattern(newPattern);
            game.getDraftPool().draft(placeDie.getDieIndex());

            if (useSecondTurn)
                consumeSecondTurn(game);
            else
                currentTurn.placeDie();

            if(currentTurn.getSacrificeIndex() > placeDie.getDieIndex())
                currentTurn.setSacrificeIndex(currentTurn.getSacrificeIndex() - 1);

            return ToolCardBehaviourResponse.SUCCESS;

        } catch (PlacementErrorException e) {
            placeDie.getView().showError(
                    "Placement doesn't respect restrictions: " + e.getMessage()
            );
        } catch (IndexOutOfBoundsException e) {
            placeDie.getView().showError("Bad selection!");
        }

        return ToolCardBehaviourResponse.FAILURE;
    }

    /**
     * Removes the possibility for the current player to use his second turn this round.
     * @param game The game the tool card has been applied to.
     */
    private void consumeSecondTurn(Game game) {
        try {
            game.getTurnManager().consumeSecondTurn(
                    game.getTurnManager().getCurrentTurn().getPlayer()
            );
        } catch (TurnManager.SecondTurnUnavailableException e) {
            // shouldn't happen
            Logger.getDefaultLogger().log(e.getMessage());
        }
    }
}
