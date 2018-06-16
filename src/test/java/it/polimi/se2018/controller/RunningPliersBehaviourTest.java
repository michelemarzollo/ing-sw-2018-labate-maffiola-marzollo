package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.PlaceDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Unit tests for RunningPliersBehaviour class.
 */
public class RunningPliersBehaviourTest {

    /**
     * Tests if requirements are not met when a player has no second turn
     * in a round.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getRoundFinishedGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");
        RunningPliersBehaviour behaviour = new RunningPliersBehaviour();
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

        RunningPliersBehaviour behaviour = new RunningPliersBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showPlaceDie", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests a case in which the usage of the tool card is successful.
     * <p>This means that no views are selected during the process and that
     * the selected die is correctly placed following all placement restrictions
     * and that the second turn for the player is consumed.</p>
     */
    @Test
    public void testUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        PlaceDie message = new PlaceDie(
                0,
                new Coordinates(1, 1),
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        RunningPliersBehaviour behaviour = new RunningPliersBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        Assert.assertTrue(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());
        Assert.assertTrue(game.getTurnManager().getPlayersToSkip().contains(player));

        List<Die> oldDraftPool = GameUtils.getDice(false);
        Die red2 = oldDraftPool.remove(0);
        Assert.assertTrue(DieUtils.areEqual(red2, player.getPattern().getGrid()[1][1].getDie()));

        List<Die> newDraftPool = game.getDraftPool().getDice();

        for (Die die : oldDraftPool) {
            boolean match = false;
            for (int j = 0; j < newDraftPool.size() && !match; ++j) {
                match = DieUtils.areEqual(die, newDraftPool.get(j));
                if (match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

    }

    /**
     * Helper method that sets up a failure scenario.
     * <p>The kind of failure that happens can be set through the {@code badIndex}
     * parameter.</p>
     *
     * @param mockView The mock view that registers view method calls.
     * @param badIndex {@code true} if the failure has to be caused by a bad index;
     *                 {@code false} if it has to be caused by placement restrictions.
     * @return An instance of Game that is in the state just after the behaviour has
     * been applied.
     */
    private Game getFailure(MockView mockView, boolean badIndex) {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        Coordinates coordinates;
        if (badIndex)
            coordinates = new Coordinates(500, 500);
        else
            coordinates = new Coordinates(0, 0);
        PlaceDie message = new PlaceDie(
                0,
                coordinates,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        RunningPliersBehaviour behaviour = new RunningPliersBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        return game;
    }

    /**
     * Helper method that asserts that the game is in the same state as at
     * the beginning of the test.
     *
     * @param game     The game to be checked.
     * @param mockView The mock view that registered view method calls.
     */
    private void assertUnchanged(Game game, MockView mockView) {
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Assert.assertFalse(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());
        Assert.assertFalse(game.getTurnManager().getPlayersToSkip().contains(player));

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        for (Die die : oldDraftPool) {
            boolean match = false;
            for (int j = 0; j < newDraftPool.size() && !match; ++j) {
                match = DieUtils.areEqual(die, newDraftPool.get(j));
                if (match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful because
     * some placement placement restriction is not satisfied.
     * <p>This means that an error view is selected during the process and that
     * no new die is placed nor the draft pool is altered nor the second turn of
     * the player is consumed.</p>
     */
    @Test
    public void testUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = getFailure(mockView, false);

        assertUnchanged(game, mockView);

        Cell[][] grid = game.getPlayers().get(0).getPattern().getGrid();

        Assert.assertNull(grid[0][0].getDie());
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful because
     * of bad destination coordinates.
     * <p>This means that an error view is selected during the process and that
     * no new die is placed nor the draft pool is altered nor the second turn of
     * the player is consumed.</p>
     */
    @Test
    public void testBadIndexFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = getFailure(mockView, true);

        assertUnchanged(game, mockView);
    }
}
