package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Unit tests for ReRollDieBehaviour class.
 */
public class ReRollDieBehaviourTest {

    /**
     * Tests if the requirements are met in a case where it's the case.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        PullAgainAndPlaceBehaviour behaviour = new PullAgainAndPlaceBehaviour();
        Assert.assertTrue(behaviour.areRequirementsSatisfied(game));
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

        ReRollDieBehaviour behaviour = new ReRollDieBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieSelection", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests a case in which the usage of the tool card is successful.
     * <p>This means that no views are selected during the process and that a
     * die in the draft pool has been re-rolled and set as the only possible placement.
     * Also the other dice must not be altered.</p>
     */
    @Test
    public void testUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        SelectDie message = new SelectDie(
                1,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        ReRollDieBehaviour behaviour = new ReRollDieBehaviour();
        ToolCardBehaviourResponse response = behaviour.useToolCard(game, message);

        Assert.assertEquals(ToolCardBehaviourResponse.SUCCESS, response);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        Die die = expectedDraftPool.remove(1).roll();
        expectedDraftPool.add(die);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());

        for (Die expectedDie : expectedDraftPool) {
            boolean match = false;
            for (int j = 0; j < actualDraftPool.size() && !match; j++) {
                match = DieUtils.areEqual(
                        expectedDie,
                        actualDraftPool.get(j));
            }
            Assert.assertTrue(match);
        }

        int expectedIndex = findIndex(die, actualDraftPool);
        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(expectedIndex, actualIndex);

    }

    /**
     * Helper function to retrieve the index of a die among a list.
     *
     * @param die  The die to look for.
     * @param dice The list of dice where to search.
     * @return In case of success, the index of the die is returned, or -1
     * in the case in which it can't be found.
     */
    private int findIndex(Die die, List<Die> dice) {
        for (int i = 0; i < dice.size(); i++) {
            if (DieUtils.areEqual(die, dice.get(i)))
                return i;
        }
        return -1;
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful because
     * of a bad selection index.
     * <p>This means that an error view is selected during the process and that
     * no die in the draft pool have been re-rolled and that the forced selection
     * is not set.</p>
     */
    @Test
    public void testUsageFailure() {
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

        ReRollDieBehaviour behaviour = new ReRollDieBehaviour();
        ToolCardBehaviourResponse response = behaviour.useToolCard(game, message);

        Assert.assertEquals(ToolCardBehaviourResponse.FAILURE, response);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());

        for (Die expectedDie : expectedDraftPool) {
            boolean match = false;
            for (int j = 0; j < actualDraftPool.size() && !match; j++) {
                match = DieUtils.areEqual(
                        expectedDie,
                        actualDraftPool.get(j));
            }
            Assert.assertTrue(match);
        }

        int expectedIndex = -1;
        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(expectedIndex, actualIndex);

    }
}
