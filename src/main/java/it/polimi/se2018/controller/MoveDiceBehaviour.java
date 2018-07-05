package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.PlacementErrorException;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Restriction;
import it.polimi.se2018.model.events.MoveDice;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;

/**
 * This class manages the usage of tool cards that move exactly n dice.
 *
 * @author dvdmff
 */
public class MoveDiceBehaviour implements ToolCardBehaviour {

    /**
     * The amount of dice to move.
     */
    private final int amount;

    /**
     * The placement restriction to use when moving dice.
     */
    private final Restriction restriction;

    /**
     * Error string for bad indexes.
     */
    private static final String BAD_INDEX_ERROR = "The source or destination coordinates " +
            "indicated are not valid";

    /**
     * Error string for bad source and destination sizes.
     */
    private static final String BAD_SIZES_ERROR = "The amount of sources is not equal to " +
            "the amount of destinations!";

    /**
     * Creates a new instance.
     *
     * @param amount      The amount of dice to be moved.
     * @param restriction The restriction to use when moving dice.
     */
    public MoveDiceBehaviour(int amount, Restriction restriction) {
        this.amount = amount;
        this.restriction = restriction;
    }

    /**
     * Returns always true because this kind of tool cards can always be used.
     *
     * @param game The game the tool card will be applied to.
     * @return Always {@code true}.
     */
    @Override
    public boolean areRequirementsSatisfied(Game game) {
        return true;
    }

    /**
     * Selects the view to allow the user to provide the dice to move.
     *
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showMoveSelection(amount);
    }

    /**
     * Moves the selected dice from the specified source positions to their
     * destination position.
     * <p>This method will fail when an invalid placement is requested, the amount
     * of source positions and destination position are not equal to the amount of dice
     * to move or if the requested positions are out of bound.</p>
     *
     * @param game    the game the effect has to be applied to.
     * @param message the message sent by the view.
     * @return {@code true} on success; {@code false} otherwise.
     */
    @Override
    public ToolCardBehaviourResponse useToolCard(Game game, ViewMessage message) {
        MoveDice moveDice = (MoveDice) message;
        Coordinates[] sources = moveDice.getSources();
        Coordinates[] destinations = moveDice.getDestinations();

        if (sources.length != amount || destinations.length != amount) {
            message.getView().showError(BAD_SIZES_ERROR);
            return ToolCardBehaviourResponse.FAILURE;
        }

        Player player = game.getTurnManager().getCurrentTurn().getPlayer();
        try {
            player.setPattern(player.getPattern().moveDice(sources, destinations, restriction));
            return ToolCardBehaviourResponse.SUCCESS;
        } catch (PlacementErrorException ex) {
            message.getView().showError(ex.getMessage());
        } catch (IndexOutOfBoundsException ex) {
            message.getView().showError(BAD_INDEX_ERROR);
        }
        return ToolCardBehaviourResponse.FAILURE;
    }
}
