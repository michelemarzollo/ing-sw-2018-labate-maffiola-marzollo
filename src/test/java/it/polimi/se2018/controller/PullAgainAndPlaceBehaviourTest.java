package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.ChooseValue;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

/**
 * Unit tests for PullAgainAndPlaceBehaviour class.
 */
public class PullAgainAndPlaceBehaviourTest {

    /**
     * Tests if the requirements are not met when a player has already placed a die.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().placeDie();
        PullAgainAndPlaceBehaviour behaviour = new PullAgainAndPlaceBehaviour();
        Assert.assertFalse(behaviour.areRequirementsSatisfied(game));
    }

    /**
     * Tests that the correct view is selected when asking parameters.
     */
    @Test
    public void testAskParameters() {
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        PullAgainAndPlaceBehaviour behaviour = new PullAgainAndPlaceBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieSelection", mockView.getCalledMethods().get(0));
    }

    /**
     * Helper function used to apply the first step of the tool card.
     *
     * @param game     The game the tool card is applied to.
     * @param mockView The mock view used to register method calls.
     * @return A PullAgainAndPlaceBehaviour that is ready for the second stage.
     */
    private PullAgainAndPlaceBehaviour applyFirstStep(Game game, MockView mockView) {
        SelectDie message = new SelectDie(
                1,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        // draft some dice to avoid strange behaviour from dice bag
        game.getDiceBag().draft(73);

        PullAgainAndPlaceBehaviour behaviour = new PullAgainAndPlaceBehaviour();
        ToolCardBehaviourResponse response = behaviour.useToolCard(game, message);

        Assert.assertEquals(ToolCardBehaviourResponse.CONSUME, response);

        return behaviour;
    }

    /**
     * Tests a case in which the application of the first step is successful.
     * <p>This means that a die is put back and re-draft from the dice bag,
     * while all other dice in the draft pool are not altered.</p>
     */
    @Test
    public void testFirstStepSuccessful() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        applyFirstStep(game, mockView);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showValueDestinationSelection",
                mockView.getCalledMethods().get(0));

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());

        int forcedSelectionIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, forcedSelectionIndex);
    }

    /**
     * Tests a case in which the application of the first step is unsuccessful
     * because of a bad selection index.
     * <p>This means that no die in the draft pool is altered.</p>
     */
    @Test
    public void testFirstStepFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        SelectDie message = new SelectDie(
                50,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        // draft some dice to avoid strange behaviour from dice bag
        game.getDiceBag().draft(73);

        PullAgainAndPlaceBehaviour behaviour = new PullAgainAndPlaceBehaviour();
        ToolCardBehaviourResponse response = behaviour.useToolCard(game, message);

        Assert.assertEquals(ToolCardBehaviourResponse.FAILURE, response);
        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());
        for (int i = 0; i < oldDraftPool.size(); ++i)
            Assert.assertTrue(DieUtils.areEqual(oldDraftPool.get(i), newDraftPool.get(i)));

        int forcedSelectionIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, forcedSelectionIndex);
    }

    /**
     * Tests a case in which the application of the second step is successful.
     * <p>This means that the previously selected die is set with the new value and that
     * is correctly placed.</p>
     */
    @Test
    public void testSecondStepSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null) {
            Assert.fail("Error on game initialization");
        }
        Player player = game.getPlayers().get(0);

        PullAgainAndPlaceBehaviour behaviour = applyFirstStep(game, mockView);

        ChooseValue message = new ChooseValue(
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo",
                2,
                new Coordinates(2, 0)
        );


        //reset draft pool, so drafted dice are known
        game.getDraftPool().setDice(GameUtils.getDice(false));
        //force select red die
        game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(0);
        Die red2 = new Die(2, new Random(), Colour.RED);

        ToolCardBehaviourResponse response = behaviour.useToolCard(game, message);

        Assert.assertEquals(ToolCardBehaviourResponse.USE, response);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertFalse(isError);

        Assert.assertTrue(
                DieUtils.areEqual(red2, player.getPattern().getGrid()[2][0].getDie()));

        Assert.assertTrue(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(oldDraftPool.size() - 1, newDraftPool.size());

    }

    /**
     * Tests a case in which the application of the second step is unsuccessful
     * because of bad destination coordinates.
     * <p>This means that the previously selected die is set with the new value and
     * that it isn't placed but left in the draft pool.</p>
     */
    @Test
    public void testSecondStepFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);
        PullAgainAndPlaceBehaviour behaviour = applyFirstStep(game, mockView);


        ChooseValue message = new ChooseValue(
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo",
                2,
                new Coordinates(50, 50)
        );

        //reset draft pool, so drafted dice are known
        game.getDraftPool().setDice(GameUtils.getDice(false));
        //force select red die
        game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(0);
        Die red2 = new Die(2, new Random(), Colour.RED);

        ToolCardBehaviourResponse response = behaviour.useToolCard(game, message);

        Assert.assertEquals(ToolCardBehaviourResponse.USE, response);
        Assert.assertEquals(2, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(1).startsWith("showError");
        Assert.assertTrue(isError);

        Assert.assertNull(player.getPattern().getGrid()[2][0].getDie());

        Assert.assertFalse(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());

    }
}
