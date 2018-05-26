package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.PlacementErrorException;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.events.MoveTwoDice;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;

/**
 * Represents the 'CopperFoilBurnisher' {@link it.polimi.se2018.model.ToolCard}.
 * The effect of this card is to move two dice on the player's {@link it.polimi.se2018.model.Pattern}
 * obeying all placement restrictions.
 * To use this card, the present position and the new position of the two dice on the
 * {@link it.polimi.se2018.model.Pattern} are required({@code message} will contain the data required).
 */
public class LathekinBehaviour implements ToolCardBehaviour {

    /**
     * Always returns true because this tool card has no specific requirements.
     *
     * @param game The game the tool card will be applied to.
     * @return Always {@code true}.
     */
    @Override
    public boolean areRequirementsSatisfied(Game game) {
        return true;
    }

    /**
     * Selects the view to let the user insert the two couple of {@link Coordinates},
     * source and destination, for the movement of the dice.
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showMoveSelection(2);
    }

    /**
     * Applies the dice movement and set the modified {@link it.polimi.se2018.model.Pattern}
     * in the {@link Player}'s attribute if the movement are correct.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by the view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {
        MoveTwoDice msg = (MoveTwoDice) message;
        Coordinates[] sources = msg.getSources();
        Coordinates[] destinations = msg.getDestinations();
        Player player = game.getTurnManager().getCurrentTurn().getPlayer();
        try{
            player.setPattern(player.getPattern().moveDice(sources, destinations));
        }
        catch (PlacementErrorException ex){
            message.getView().showError(ex.getMessage());
            return false;
        }
        catch (IndexOutOfBoundsException ex){
            message.getView().showError("The source or destination coordinates " +
                    "indicated are not valid");
            return false;
        }
        return true;
    }
}
