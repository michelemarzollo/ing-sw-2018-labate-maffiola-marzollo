package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.IncrementDieValue;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Unit tests for AlterDieValueBehaviour.
 */
public class AlterDieValueBehaviourTest {

    /**
     * Tests if requirements are not met when a player has already placed a die.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().placeDie();
        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(false);
        Assert.assertFalse(behaviour.areRequirementsSatisfied(game));
    }

    /**
     * Tests that the correct view is selected when asking parameters when the behaviour is
     * set not to allow increment/decrement.
     */
    @Test
    public void testAskParametersNoIncrement() {
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(false);
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieSelection", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests that the correct view is selected when asking parameters when the behaviour is
     * set to allow increment/decrement.
     */
    @Test
    public void testAskParametersCanIncrement() {
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(true);
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieIncrementSelection", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests a case in which the usage of the tool card is successful when the behaviour is
     * set not to allow increment/decrement.
     * <p>This means that no views are selected during the process and that the
     * selected die is correctly flipped and set as the only option for drafting,
     * while the other dice in the draft pool are not altered.</p>
     */
    @Test
    public void testUsageSuccessFlipDie() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        SelectDie message = new SelectDie(
                0,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(false);
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        Die expectedDie = oldDraftPool.remove(0).flip();
        Assert.assertTrue(DieUtils.areEqual(expectedDie, newDraftPool.get(actualIndex)));

        Assert.assertTrue(draftPoolEquals(oldDraftPool, newDraftPool));

    }

    /**
     * Tests a case in which the usage of the tool card in increase mode is successful when
     * the behaviour is set to allow increment/decrement.
     * <p>This means that no views are selected during the process and that the
     * selected die is correctly increased and set as the only option for drafting,
     * while the other dice in the draft pool are not altered.</p>
     */
    @Test
    public void testIncreaseUsageSuccessIncrement() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                0,
                true,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(true);
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Die expectedDie = oldDraftPool.remove(0).increase();
        Assert.assertTrue(DieUtils.areEqual(expectedDie, newDraftPool.get(actualIndex)));
        Assert.assertTrue(draftPoolEquals(oldDraftPool, newDraftPool));
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful
     * because of a bad selection index.
     * <p>This means that an error view is selected during the process and that
     * no die in the draft pool is altered nor set as the only option for drafting.</p>
     */
    @Test
    public void testBadIndexFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        SelectDie message = new SelectDie(
                500,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(false);
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());

        Assert.assertTrue(draftPoolEquals(expectedDraftPool, actualDraftPool));

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, actualIndex);

    }

    /**
     * Tests a case in which the usage of the tool card in decrease mode is successful when
     * the behaviour is set not to allow increment/decrement.
     * <p>This means that no views are selected during the process and that the
     * selected die is correctly decreased and set as the only option for drafting,
     * while the other dice in the draft pool are not altered.</p>
     */
    @Test
    public void testUsageSuccessDecrement() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                1,
                false,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(true);
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Die expectedDie = oldDraftPool.remove(1).decrease();
        Assert.assertTrue(DieUtils.areEqual(expectedDie, newDraftPool.get(actualIndex)));
        Assert.assertTrue(draftPoolEquals(oldDraftPool, newDraftPool));
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful
     * because the value can't be decreased (it's a 1).
     * <p>This means that an error view is selected during the process and that
     * no die in the draft pool is altered nor set as the only option for
     * drafting.</p>
     */
    @Test
    public void testUsageFailureWithDecrement() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                0,
                false,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        ToolCardBehaviour behaviour = new AlterDieValueBehaviour(true);
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertTrue(draftPoolEquals(oldDraftPool, newDraftPool));
    }

    /**
     * Checks if two draft pool are equals.
     *
     * @param firstDraftPool  The first draft pool to compare.
     * @param secondDraftPool The second draft pool to compare.
     * @return {@code true} if the two draft pools contains the same dice, even in different
     * order; {@code false} otherwise.
     */
    private boolean draftPoolEquals(List<Die> firstDraftPool, List<Die> secondDraftPool) {
        for (Die die : firstDraftPool) {
            boolean match = false;
            for (int j = 0; j < secondDraftPool.size() && !match; ++j) {
                match = DieUtils.areEqual(die, secondDraftPool.get(j));
                if (match)
                    secondDraftPool.remove(j);
            }
            if (!match)
                return false;
        }
        return true;
    }
}
