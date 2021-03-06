package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.ChooseValue;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class manages the usage of tool cards that puts a die back in the dice bag
 * and pulls a new one, allowing the player to select the value before placing it.
 *
 * @author michelemarzollo
 */
public class PullAgainAndPlaceBehaviour implements ToolCardBehaviour {

    /**
     * The attribute to indicate if the first part of the procedure linked
     * to the toolCard was already done. It's set to {@code false} if the first
     * step wasn't already done, to {@code true} otherwise.
     */
    private boolean firstStepDone = false;

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
        message.getView().showDieSelection();
        firstStepDone = false;
    }

    /**
     * Applies the effect of the tool card.
     * <p>
     * The action is divided in two steps: in the first one the chosen die of the
     * DraftPool is changed with one die from the DiceBag. In the second one the
     * desired value of the new die is set and the die is placed in the Pattern.</p>
     *
     * @param game    The game the effect has to be applied to.
     * @param message The message sent by th view.
     * @return {@code true} if the tool card has been successfully applied;
     * {@code false} otherwise.
     */
    @Override
    public ToolCardBehaviourResponse useToolCard(Game game, ViewMessage message) {

        if (!firstStepDone) {
            SelectDie selectMessage = (SelectDie) message;
            return firstStep(game, selectMessage);
        } else {
            ChooseValue chooseMessage = (ChooseValue) message;
            return secondStep(game, chooseMessage);
        }

    }

    /**
     * In the first step the chosen die from the DraftPool is put back in
     * the DiceBag and a new die is drafted from the DiceBag and put in the
     * DraftPool.
     * <p>
     * In the next step the die that must be used is the one that was just
     * placed in the DraftPool: the attribute {@code forcedSelection} in the
     * current {@link Turn} is set to the index of the die from the DiceBag.</p>
     *
     * @param game          the reference to the {@link Game}.
     * @param selectMessage the message from the view, that contains the information
     *                      to make the move and the reference to the view.
     * @return always {@code false} since the application is not finished.
     */
    private ToolCardBehaviourResponse firstStep(Game game, SelectDie selectMessage) {
        try {
            List<Die> dice = game.getDraftPool().getDice();

            Die oldDie = dice.remove(selectMessage.getDieIndex());
            game.getDiceBag().pushBack(oldDie);
            //I draft 1 die from the diceBag and put it in the list of dice
            Die newDie = game.getDiceBag().draft(1).get(0);
            dice.add(selectMessage.getDieIndex(), newDie);
            //I set the new DraftPool in the game
            game.getDraftPool().setDice(dice);
            //Setting of the forced selection in the current turn
            game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(
                    selectMessage.getDieIndex());

            firstStepDone = true;
            selectMessage.getView().showValueDestinationSelection();
            return ToolCardBehaviourResponse.CONSUME;
        } catch (IndexOutOfBoundsException e) {
            selectMessage.getView().showError("Bad index!");
        }
        return ToolCardBehaviourResponse.FAILURE;
    }

    /**
     * In the second step is given to the die the value chosen by the player, and the
     * die is placed in the grid in the position chosen by the player.
     *
     * @param game          the reference to the {@link Game}.
     * @param chooseMessage the message from the view, that contains the information
     *                      to make the move and the reference to the view.
     * @return {@code true} if this step has been successfully applied;
     * {@code false} otherwise.
     */
    private ToolCardBehaviourResponse secondStep(Game game, ChooseValue chooseMessage) {

        int forcedSelection = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Die oldDie = game.getDraftPool().getDice().get(forcedSelection);

        //I create a new die with the same colour and the specified value
        Die newDie = new Die(chooseMessage.getValue(), new Random(), oldDie.getColour());
        insertNewDie(game, newDie, forcedSelection);
        try {
            Turn currentTurn = game.getTurnManager().getCurrentTurn();

            // place die
            Pattern currentPattern = currentTurn.getPlayer().getPattern();
            Pattern newPattern = currentPattern.placeDie(newDie, chooseMessage.getDestination());
            currentTurn.getPlayer().setPattern(newPattern);
            game.getDraftPool().draft(forcedSelection);

            currentTurn.placeDie();

            if(currentTurn.getSacrificeIndex() > currentTurn.getForcedSelectionIndex())
                currentTurn.setSacrificeIndex(currentTurn.getSacrificeIndex() - 1);
            currentTurn.setForcedSelectionIndex(-1);
        } catch (PlacementErrorException e) {
            chooseMessage.getView().showError(
                    "Placement doesn't respect restrictions!\n" + e.getMessage()
            );
        }
        firstStepDone = false;
        return ToolCardBehaviourResponse.USE;
    }

    /**
     * Inserts the given die at the given position.
     * @param game The game owning the die.
     * @param newDie The die to insert.
     * @param index The index of the die.
     */
    private void insertNewDie(Game game, Die newDie, int index) {
        List<Die> draftPool = new ArrayList<>(game.getDraftPool().getDice());
        draftPool.set(index, newDie);
        game.getDraftPool().setDice(draftPool);
    }
}
