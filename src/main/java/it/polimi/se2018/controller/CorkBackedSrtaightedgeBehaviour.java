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
public class CorkBackedSrtaightedgeBehaviour implements ToolCardBehaviour {

    /**
     * Does nothing.
     *
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        //do nothing
    }

    /**
     * Places the selected die in a position that is not adjacent to any
     * other die.
     * <p>The use of this tool card consumes the possibility to place
     * a die regularly.</p>
     *
     * @param game    The game the effect has to be applied to.
     * @param message The message sent by th view.
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {
        PlaceDie placeDie = (PlaceDie) message;
        try {
            Die selected = game.getDraftPool().select(placeDie.getDieIndex());
            String playerName = placeDie.getPlayerName();
            Player player = game.getTurnManager().getCurrentTurn().getPlayer();
            if (player.getName().equals(playerName)) {
                Pattern currentPattern = player.getPattern();
                Pattern newPattern = currentPattern.placeDie(
                        selected,
                        placeDie.getDestination(),
                        Restriction.NOT_ADJACENT);
                player.setPattern(newPattern);
                game.getDraftPool().draft(placeDie.getDieIndex());
                // consume placement also
                game.getTurnManager().getCurrentTurn().placeDie();
            } else {
                placeDie.getView().showError("Not your turn!");
            }
        } catch (PlacementErrorException e) {
            placeDie.getView().showError("Invalid placement: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            placeDie.getView().showError("Bad selection!");
        }
    }
}
