package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.PlaceDie;
import it.polimi.se2018.model.events.ViewMessage;

/**
 * Represents the "Cork-backed Straightedge" tool card.
 * <p>The effect of this tool card is to place a die in a position that is
 * not adjacent to any other die.</p>
 * <p>An additional effect of this tool card is to consume the possibility to
 * regularly place a die.</p>
 * <p>This tool card requires the index of the die in the draft pool and
 * the destination coordinates on the pattern to be applied.</p>
 */
public class CorkBackedStraightedgeBehaviour implements ToolCardBehaviour {

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
     * Does nothing.
     *
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showDieSelection();
    }

    /**
     * Places the selected die in a position that is not adjacent to any
     * other die.
     * <p>The use of this tool card consumes the possibility to place
     * a die regularly.</p>
     *
     * @param game    The game the effect has to be applied to.
     * @param message The message sent by th view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public boolean useToolCard(Game game, ViewMessage message) {
        PlaceDie placeDie = (PlaceDie) message;
        try {
            Die selected = game.getDraftPool().select(placeDie.getDieIndex());
            Player player = game.getTurnManager().getCurrentTurn().getPlayer();

            Pattern currentPattern = player.getPattern();
            Pattern newPattern = currentPattern.placeDie(
                    selected,
                    placeDie.getDestination(),
                    Restriction.NOT_ADJACENT);
            player.setPattern(newPattern);
            game.getDraftPool().draft(placeDie.getDieIndex());
            // consume placement also
            game.getTurnManager().getCurrentTurn().placeDie();

        } catch (PlacementErrorException e) {
            placeDie.getView().showError("Invalid placement: " + e.getMessage());
            return false;
        } catch (IndexOutOfBoundsException e) {
            placeDie.getView().showError("Bad selection!");
            return false;
        }

        return true;
    }
}