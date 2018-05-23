package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.PlacementErrorException;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Restriction;
import it.polimi.se2018.model.events.MoveDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;

/**
 * Represents the 'EglomiseBrush' {@link it.polimi.se2018.model.ToolCard}.
 * The effect of this card is to move a die on the player's {@link it.polimi.se2018.model.Pattern}
 * ignoring colour restrictions, but obeying all other placement restrictions.
 * To use this card, the present position and the new position of the die
 * on the {@link it.polimi.se2018.model.Pattern} are required({@code message} will contain the data required).
 */
public class EglomiseBrushBehaviour implements ToolCardBehaviour {

    /**
     * Selects the view to let the user insert the couple of {@link Coordinates},
     * source and destination, for the movement of the die.
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showMoveSelection(1);
    }

    /**
     * Applies the die movement and set the modified {@link it.polimi.se2018.model.Pattern}
     * in the {@link Player}'s attribute if the movement is correct.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by the view.
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {
        MoveDie msg = (MoveDie) message;
        Coordinates source = msg.getSource();
        Coordinates destination = msg.getDestination();
        Player player = game.getTurnManager().getCurrentTurn().getPlayer();
        try{
        player.setPattern(player.getPattern().moveDie(source, destination, Restriction.ONLY_VALUE));
        }
        catch (PlacementErrorException ex){
            message.getView().showError(ex.getMessage());
        }
    }
}
