package it.polimi.se2018.controller;

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
 * Unit tests for CorkBackedStraightedgeBehaviour class.
 */
public class CorkBackedStraightedgeBehaviourTest {

    /**
     * Tests that the requirements are not met after a player has placed a die.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().placeDie();
        CorkBackedStraightedgeBehaviour behaviour = new CorkBackedStraightedgeBehaviour();
        Assert.assertFalse(behaviour.areRequirementsSatisfied(game));
    }

    /**
     * Tests that the correct view is selected when asking parameters.
     */
    @Test
    public void testAskParameters() {
        MockView mockView = new MockView("Pippo");

        CorkBackedStraightedgeBehaviour behaviour = new CorkBackedStraightedgeBehaviour();
        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                mockView.getPlayerName());
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showPlaceDie", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests a case in which the usage of the tool card is successful.
     * <p>This means that no views are selected during the process and that the
     * selected die is actually placed in the pattern and removed from the draft pool.</p>
     */
    @Test
    public void testUsageSuccessful() {
        MockView mockView = new MockView("Pippo");

        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        Player player = game.getPlayers().get(0);

        PlaceDie message = new PlaceDie(
                1,
                new Coordinates(3, 3),
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        CorkBackedStraightedgeBehaviour behaviour = new CorkBackedStraightedgeBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Die expectedDie = GameUtils.getDice(false).get(1);
        Die actualDie = player.getPattern().getGrid()[3][3].getDie();

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        Assert.assertTrue(DieUtils.areEqual(expectedDie, actualDie));

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        expectedDraftPool.remove(1);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());
        for (int i = 0; i < expectedDraftPool.size(); i++) {
            Assert.assertTrue(
                    DieUtils.areEqual(expectedDraftPool.get(i), actualDraftPool.get(i))
            );
        }

        Assert.assertTrue(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful because of
     * placement restrictions.
     * <p>This means that an error view is selected during the process and that the
     * selected die is not placed in the pattern and is still in the draft pool.</p>
     */
    @Test
    public void testUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        Player player = game.getPlayers().get(0);

        PlaceDie message = new PlaceDie(
                1,
                new Coordinates(1, 1),
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        CorkBackedStraightedgeBehaviour behaviour = new CorkBackedStraightedgeBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertTrue(mockView.getCalledMethods().get(0).startsWith("showError: Invalid placement"));

        Assert.assertNull(player.getPattern().getGrid()[1][1].getDie());

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());
        for (int i = 0; i < expectedDraftPool.size(); i++) {
            Assert.assertTrue(DieUtils.areEqual(expectedDraftPool.get(i), actualDraftPool.get(i)));
        }

        Assert.assertFalse(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful because of
     * bad destination coordinates.
     * <p>This means that an error view is selected during the process and that the
     * selected die is not placed in the pattern and is still in the draft pool.</p>
     */
    @Test
    public void testBadIndexUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        PlaceDie message = new PlaceDie(
                1,
                new Coordinates(50, 50),
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        CorkBackedStraightedgeBehaviour behaviour = new CorkBackedStraightedgeBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showError: Bad selection!", mockView.getCalledMethods().get(0));

        Assert.assertNull(player.getPattern().getGrid()[1][1].getDie());

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());
        for (int i = 0; i < expectedDraftPool.size(); i++) {
            Assert.assertTrue(DieUtils.areEqual(expectedDraftPool.get(i), actualDraftPool.get(i)));
        }

        Assert.assertFalse(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());
    }
}
