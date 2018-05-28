package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.MoveTwoDice;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Unit tests for TapWheelBehaviour class.
 */
public class TapWheelBehaviourTest {

    /**
     * Tests if requirements are met.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        TapWheelBehaviour behaviour = new TapWheelBehaviour();
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

        TapWheelBehaviour behaviour = new TapWheelBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showMoveSelection2", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests a case in which the usage of the tool card is successful.
     * <p>This means that no views are selected during the process and that
     * the selected dice are correctly moved (following all placement restrictions).</p>
     */
    @Test
    public void testUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = setupSuccessScenario(mockView);
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);

        Cell[][] grid = player.getPattern().getGrid();
        Assert.assertTrue(DieUtils.areEqual(yellow3, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[2][2].getDie()));

    }

    private Game setupSuccessScenario(MockView mockView) {
        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[]{
                        new Coordinates(1, 0),
                        new Coordinates(2, 2)
                },
                new Coordinates[]{
                        new Coordinates(2, 2),
                        new Coordinates(1, 0)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        return setupScenario(message, true, true);
    }

    /**
     * Helper method that places 2 dice on the pattern owned by the player.
     * <p>These dice are:
     * <ul>
     * <li>a red 2 in position (1, 1)</li>
     * <li>a yellow 3 in position (2, 2)</li>
     * </ul></p>
     *
     * @param player The player owning the pattern to manipulate.
     * @return {@code true} if the placement is successful; {@code false}
     * otherwise.
     */
    private boolean placeSomeDice(Player player) {
        Pattern pattern = player.getPattern();
        Die red2 = new Die(2, new Random(0), Colour.RED);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);
        try {
            pattern = pattern.placeDie(red2, new Coordinates(1, 1));
            pattern = pattern.placeDie(yellow3, new Coordinates(2, 2));
        } catch (PlacementErrorException e) {
            return false;
        }

        player.setPattern(pattern);
        return true;
    }

    /**
     * Returns a well-defined list of dice.
     * <p>The dice are, in order:
     * <ul>
     * <li>purple 2</li>
     * <li>purple 5</li>
     * <li>yellow 6 (if insertYellow flag is true)</li>
     * </ul>
     * </p>
     *
     * @param insertYellow Flag to indicate if a yellow die has to be inserted.
     * @return A list of well-defined dice.
     */
    private List<Die> getLeftovers(boolean insertYellow) {
        List<Die> dice = new ArrayList<>();
        dice.add(new Die(2, new Random(0), Colour.PURPLE));
        dice.add(new Die(5, new Random(0), Colour.PURPLE));
        if (insertYellow)
            dice.add(new Die(6, new Random(0), Colour.YELLOW));
        return dice;
    }

    /**
     * Fills the round track of the specified game with some well-defined dice.
     *
     * @param game         The game to be manipulated.
     * @param insertYellow Flag to indicate if a yellow die has to be inserted.
     * @see TapWheelBehaviourTest#getLeftovers(boolean)
     */
    private void fillRoundTrack(Game game, boolean insertYellow) {
        game.getRoundTrack().addAllForRound(1, getLeftovers(insertYellow));
    }

    /**
     * Helper method that sets up a custom scenario fot this test suite.
     *
     * @param message      The message to be used to apply the behaviour.
     * @param insertYellow Flag to indicate whether a yellow die has to be inserted
     *                     in the round track.
     * @param isSuccess    Flag to indicate if the application of the behaviour has to
     *                     be successful.
     * @return An instance of Game just after the behaviour has been applied.
     */
    private Game setupScenario(MoveTwoDice message, boolean insertYellow, boolean isSuccess) {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        fillRoundTrack(game, insertYellow);
        boolean control = placeSomeDice(player);
        Assert.assertTrue("Error on placement", control);

        TapWheelBehaviour behaviour = new TapWheelBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertEquals(isSuccess, success);

        return game;
    }

    /**
     * Helper method that sets up a failure scenario where the failure is caused
     * by bad colour selection.
     *
     * @param mockView The mock view that registers view method calls.
     * @return An instance of Game just after the behaviour has been applied.
     */
    private Game setupBadColourScenario(MockView mockView) {
        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[]{
                        new Coordinates(1, 0),
                        new Coordinates(2, 2)
                },
                new Coordinates[]{
                        new Coordinates(2, 2),
                        new Coordinates(1, 0)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        return setupScenario(message, false, false);
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful because
     * the colour of the selected dice is not in the round track.
     * <p>This means that an error view is selected during the process and that
     * no die is moved.</p>
     */
    @Test
    public void testBadColourUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = setupBadColourScenario(mockView);
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);

        Cell[][] grid = player.getPattern().getGrid();
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(yellow3, grid[2][2].getDie()));
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful because
     * the number of selected dice doesn't correspond to the number of destinations.
     * <p>This means that an error view is selected during the process and that
     * no die is moved.</p>
     */
    @Test
    public void testBadAmountsUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = setupBadAmountScenario(mockView);
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);

        Cell[][] grid = player.getPattern().getGrid();
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(yellow3, grid[2][2].getDie()));
    }

    private Game setupBadAmountScenario(MockView mockView) {
        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[]{
                        new Coordinates(1, 0),
                        new Coordinates(2, 2)
                },
                new Coordinates[]{
                        new Coordinates(2, 2)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );
        return setupScenario(message, true, false);
    }
}
