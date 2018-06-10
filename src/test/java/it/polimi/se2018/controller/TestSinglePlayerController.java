package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.*;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Class to test the class {@link SinglePlayerController} in the package
 * 'controller'.
 *
 * @author michelemarzollo
 */
public class TestSinglePlayerController {

    private Game game;
    private SinglePlayerController controller;
    private MockView view;
    private String playerName = "Pippo";

    @Before
    public void setUp() {
        view = new MockView(playerName);
    }

    /**
     * The same as {@code testRegisterPlayers()}, but passing form the method
     * {@code update(ViewMessage m)}.
     *
     * @see Controller#update(ViewMessage)
     */
    @Test
    public void testUpdate() {
        game = new Game();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.REGISTER_PLAYER, playerName);
        controller.update(msg);
        assertEquals(playerName, game.getPlayers().get(0).getName());
        assertEquals(1, game.getPlayers().size());
        assertTrue(view.getCalledMethods().contains("showDifficultySelection"));
        assertEquals(1, view.getCalledMethods().size());

        msg = new ViewMessage(view, null, playerName);
        controller.update(msg);
        assertTrue(view.getCalledMethods().size() == 1);
        assertTrue(view.getCalledMethods().contains("showDifficultySelection"));
    }

    /**
     * Test the method {@code registerAction}: when a certain message is received,
     * the corresponding method must call the proper method in the view.
     *
     * @see SinglePlayerController#registerActions(Map)
     */
    @Test
    public void testRegisterAction() {
        game = GameUtils.getStartedGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");
        controller = new SinglePlayerController(game, 100, 100);
        game = GameUtils.getHalfwayGame();
        PlaceDie msg = new PlaceDie(2, new Coordinates(2, 7), view, Action.PLACE_DIE, playerName);
        controller.performAction(msg);
        assertTrue(view.getCalledMethods().contains("showError: Invalid selection!"));
        assertEquals(1, view.getCalledMethods().size());
    }

    /**
     * Tests the method {@code registerPlayers()} when the first player is added.
     *
     * @see SinglePlayerController#registerPlayer(ViewMessage)
     */
    @Test
    public void testRegisterPlayers() {
        game = new Game();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.REGISTER_PLAYER, playerName);
        controller.performAction(msg);
        assertEquals(playerName, game.getPlayers().get(0).getName());
        assertEquals(1, game.getPlayers().size());
        assertTrue(view.getCalledMethods().contains("showDifficultySelection"));
        assertEquals(1, view.getCalledMethods().size());
    }

    /**
     * Tests the behavior of the controller when a {@code REGISTER_PLAYER} action
     * is received and the game was already started.
     *
     * @see SinglePlayerController#registerPlayer(ViewMessage)
     */
    @Test
    public void testNegativeRegisterPlayer() {
        game = GameUtils.getStartedGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.REGISTER_PLAYER, "Paperino");
        controller.performAction(msg);
        assertEquals(playerName, game.getPlayers().get(0).getName());
        assertTrue(view.getCalledMethods().contains("showError: There is already one registered player!"));
        assertEquals(1, game.getPlayers().size());
    }

    /**
     * Tests the method {@code setUpGame()} when the difficulty selected is valid.
     *
     * @see SinglePlayerController#setUpGame(ViewMessage)
     */
    @Test
    public void testCorrectSetUpGame() {
        game = new Game();
        controller = new SinglePlayerController(game, 100, 100);
        //registers a player to the game
        ViewMessage msg1 = new ViewMessage(view, Action.REGISTER_PLAYER, playerName);
        controller.performAction(msg1);

        SelectDifficulty msg2 = new SelectDifficulty(4, view, playerName);
        controller.performAction(msg2);
        //the number of cards must be the correct one
        assertEquals(3, game.getPublicObjectiveCards().length);
        assertEquals(2, game.getPlayers().get(0).getCards().length);
        assertTrue(game.isSetupComplete());
    }

    /**
     * Tests the behaviour of {@code setUpGame()} if the player wasn't already registered.
     * (if {@code update()} in {@link Controller} receives a {@link SelectDifficulty}
     * message before a player was registered.
     *
     * @see SinglePlayerController#setUpGame(ViewMessage)
     */
    @Test
    public void testSetUpGameBeforeRegisterPlayer() {
        game = new Game();
        controller = new SinglePlayerController(game, 100, 100);
        SelectDifficulty msg = new SelectDifficulty(4, view, playerName);
        controller.performAction(msg);
        assertTrue(view.getCalledMethods().contains("showError: There is no registered player!"));
        //cards shouldn't have been added
        assertNull(game.getPublicObjectiveCards());
        //there can't be players in the game
        assertTrue(game.getPlayers().isEmpty());
        assertFalse(game.isSetupComplete());
    }

    /**
     * Tests the method {@code setUpGame()} when the difficulty selected is valid.
     * <p>
     * Nothing should be done to the game and  an error should be shown.</p>
     * <p>
     * When the next {@link SelectDifficulty} message is receive, if the difficulty
     * is allowed, the set up must be completed.</p>
     * <p>(First branch of the condition)</p>
     *
     * @see SinglePlayerController#setUpGame(ViewMessage)
     */
    @Test
    public void testFirstWrongSetUpGame() {
        game = new Game();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg1 = new ViewMessage(view, Action.REGISTER_PLAYER, playerName);
        controller.performAction(msg1);
        SelectDifficulty msg2 = new SelectDifficulty(0, view, playerName);
        controller.performAction(msg2);
        assertTrue(view.getCalledMethods().contains(
                "showError: The level of difficulty must be in 1-5 range: choose another one"));
        assertFalse(game.isSetupComplete());
        //cards shouldn't have been distributed
        assertNull(game.getPublicObjectiveCards());
        assertNull(game.getPlayers().get(0).getCards());
        //a second message to select the difficulty is sent
        SelectDifficulty msg3 = new SelectDifficulty(3, view, playerName);
        controller.performAction(msg3);
        assertEquals(3, game.getPublicObjectiveCards().length);
        assertEquals(2, game.getPlayers().get(0).getCards().length);
        assertTrue(game.isSetupComplete());
    }

    /**
     * Tests the method {@code setUpGame()} when the difficulty selected is valid.
     * <p>
     * Nothing should be done to the game and  an error should be shown.</p>
     * <p>(Second branch of the condition)</p>
     *
     * @see SinglePlayerController#setUpGame(ViewMessage)
     */
    @Test
    public void testSecondWrongSetUpGame() {
        game = new Game();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg1 = new ViewMessage(view, Action.REGISTER_PLAYER, playerName);
        controller.performAction(msg1);
        SelectDifficulty msg2 = new SelectDifficulty(7, view, playerName);
        controller.performAction(msg2);
        assertTrue(view.getCalledMethods().contains(
                "showError: The level of difficulty must be in 1-5 range: choose another one"));
        assertFalse(game.isSetupComplete());
        //cards shouldn't have been distributed
        assertNull(game.getPublicObjectiveCards());
        assertNull(game.getPlayers().get(0).getCards());
    }

    /**
     * Tests the call of {@code endGame}.
     *
     * @see SinglePlayerController#endGame(ViewMessage)
     */
    @Test
    public void testEndGame() {
        game = GameUtils.getCompleteSinglePlayerGame();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.END_TURN, playerName);

        controller.endGame(msg);

        assertTrue(game.getDraftPool().getDice().isEmpty());
        assertEquals("showPrivateObjectiveSelection", view.getCalledMethods().get(0));
    }

    /**
     * Tests the method {@code selectPrivateObjective(ViewMessage message)} when th card
     * chosen is not present in the game.
     *
     * @see SinglePlayerController#selectPrivateObjective(ViewMessage)
     */
    @Test
    public void testNegativeSelectPrivateObjective() {
        game = GameUtils.getCompleteSinglePlayerGame();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.END_TURN, playerName);

        controller.endGame(msg);

        //the colour is initialized to avoid compilation errors, but i know that
        //it will be substituted by the for loop
        String name = "ShadesOfYellow";
        //chooses a colour that is not the colour of one of the Private Cards of the game
        for (Colour col : Colour.values()) {
            if (col != game.getPlayers().get(0).getCards()[0].getColour() &&
                    col != game.getPlayers().get(0).getCards()[1].getColour())
                name = "ShadesOf" + col.toString();
        }
        //the message to send to choose the card
        ViewMessage msg2 = new SelectCard(
                name, view, Action.SELECT_PRIVATE_OBJECTIVE, playerName);
        controller.performAction(msg2);

        assertEquals("showError: The chosen private objective card is not available!",
                view.getCalledMethods().get(view.getCalledMethods().size() - 1));
    }

    /**
     * Tests the method to calculate the scores.
     *
     * @see SinglePlayerController#calculateScores()
     */
    @Test
    public void testCalculateScores() {
        game = GameUtils.getCompleteSinglePlayerGame();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.END_TURN, playerName);

        controller.endGame(msg);

        //gets the colour of the first Private Objective of the game
        String name = game.getPlayers().get(0).getCards()[0].getName();
        //the message to send to choose the card
        ViewMessage msg2 = new SelectCard(
                name, view, Action.SELECT_PRIVATE_OBJECTIVE, playerName);
        controller.performAction(msg2);
        assertTrue(game.getDraftPool().getDice().isEmpty());
        assertTrue(game.getPlayers().get(1).getName().equals("RoundTrack"));
        assertEquals(-21, game.getPlayers().get(0).getScore());
        assertEquals(37, game.getPlayers().get(1).getScore());

    }

    /**
     * Checks the filling of the ScoreBoard.
     * Also checks that in the limit case where the {@link it.polimi.se2018.model.DraftPool}
     * is empty, the method should behave normally and give a s result the values of the dice
     * of the {@link it.polimi.se2018.model.RoundTrack}.
     *
     * @see SinglePlayerController#fillScoreBoard()
     */
    @Test
    public void testFillScoreBoard() {
        game = GameUtils.getCompleteSinglePlayerGame();

        game.getDraftPool().setDice(new ArrayList<>());

        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.END_TURN, playerName);

        controller.endGame(msg);

        //gets the colour of the first Private Objective of the game
        String name = game.getPlayers().get(0).getCards()[0].getName();
        //the message to send to choose the card
        ViewMessage msg2 = new SelectCard(
                name, view, Action.SELECT_PRIVATE_OBJECTIVE, playerName);
        controller.performAction(msg2);
        assertTrue(game.getDraftPool().getDice().isEmpty());
        assertEquals(-21, game.getPlayers().get(0).getScore());
        assertEquals(19, game.getPlayers().get(1).getScore());

        //te scoreboard must be filled in decreasing order of scores
        List<Player> scoreBoard = game.getScoreBoard();
        assertEquals(scoreBoard.get(0), game.getPlayers().get(1));
        assertEquals(scoreBoard.get(1), game.getPlayers().get(0));
        assertEquals("showScoreBoard", view.getCalledMethods().get(view.getCalledMethods().size() - 1));
    }

    /**
     * Tests {@code canUseToolCard()} when it can be used.
     *
     * @see SinglePlayerController#canUseToolCard(ViewMessage, ToolCard)
     */
    @Test
    public void testPositiveCanUseToolCard() {
        Game game = GameUtils.getStartedGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");

        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", Colour.PURPLE),
                new ToolCard("Eglomise Brush", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", Colour.RED)};
        game.setToolCards(toolCards);

        List<Die> dice = new ArrayList<>(Arrays.asList(
                new Die(4, new Random(), Colour.YELLOW),
                new Die(5, new Random(), Colour.RED),
                new Die(3, new Random(), Colour.BLUE),
                new Die(6, new Random(), Colour.PURPLE)));

        game.getDraftPool().setDice(dice);
        Controller controller = new SinglePlayerController(game, 100, 100);
        SelectCardSP msg = new SelectCardSP("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo", 3);
        boolean canUse = controller.canUseToolCard(msg, toolCards[0]);
        assertTrue(canUse);
        assertTrue(view.getCalledMethods().isEmpty());
        assertEquals(3, game.getTurnManager().getCurrentTurn().getSacrificeIndex());
    }

    /**
     * Tests {@code canUseToolCard()} when it can be used because the {@link ToolCard}
     * was already used.
     *
     * @see SinglePlayerController#canUseToolCard(ViewMessage, ToolCard)
     */
    @Test
    public void testNegativeCanUseToolCard() {
        Game game = GameUtils.getStartedGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");

        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", Colour.PURPLE),
                new ToolCard("Eglomise Brush", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", Colour.RED)};
        game.setToolCards(toolCards);

        List<Die> dice = new ArrayList<>(Arrays.asList(
                new Die(4, new Random(), Colour.YELLOW),
                new Die(5, new Random(), Colour.RED),
                new Die(3, new Random(), Colour.BLUE),
                new Die(6, new Random(), Colour.PURPLE)));

        game.getDraftPool().setDice(dice);
        toolCards[0].use();
        Controller controller = new SinglePlayerController(game, 100, 100);
        SelectCardSP msg = new SelectCardSP("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo", 3);
        boolean canUse = controller.canUseToolCard(msg, toolCards[0]);
        assertFalse(canUse);
        assertEquals("showError: The Tool Card has already been used.",
                view.getCalledMethods().get(view.getCalledMethods().size() - 1));
        assertEquals(-1, game.getTurnManager().getCurrentTurn().getSacrificeIndex());
    }

    /**
     * Tests {@code canUseToolCard()} when it can be used because the {@link ToolCard}
     * die selected doesn't match the colour of the ToolCard.
     *
     * @see SinglePlayerController#canUseToolCard(ViewMessage, ToolCard)
     */
    @Test
    public void testWrongDieCanUseToolCard() {
        Game game = GameUtils.getStartedGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");

        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", Colour.PURPLE),
                new ToolCard("Eglomise Brush", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", Colour.RED)};
        game.setToolCards(toolCards);

        List<Die> dice = new ArrayList<>(Arrays.asList(
                new Die(4, new Random(), Colour.YELLOW),
                new Die(5, new Random(), Colour.RED),
                new Die(3, new Random(), Colour.BLUE),
                new Die(6, new Random(), Colour.PURPLE)));

        game.getDraftPool().setDice(dice);

        Controller controller = new SinglePlayerController(game, 100, 100);
        SelectCardSP msg = new SelectCardSP("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo", 2);
        boolean canUse = controller.canUseToolCard(msg, toolCards[0]);
        assertFalse(canUse);
        assertEquals("showError: The requested die doesn't match the Tool Card's colour.",
                view.getCalledMethods().get(view.getCalledMethods().size() - 1));
        assertEquals(-1, game.getTurnManager().getCurrentTurn().getSacrificeIndex());
    }

    /**
     * Tests {@code displayGame()}.
     *
     * @see SinglePlayerController#displayGame(ViewMessage)
     */
    @Test
    public void testDisplayGame() {
        game = GameUtils.getCompleteSinglePlayerGame();
        controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.END_TURN, "Pippo"); //generic action, just to have a ViewMessage
        controller.displayGame(msg);
        assertEquals("showSinglePlayerGame", view.getCalledMethods().get(view.getCalledMethods().size() - 1));

    }

    /**
     * Tests {@code getDraftAmount()}.
     *
     * @see SinglePlayerController#getDraftAmount()
     */
    @Test
    public void testGetDraftAmount() {
        game = GameUtils.getCompleteSinglePlayerGame();
        controller = new SinglePlayerController(game, 100, 100);
        assertEquals(4, controller.getDraftAmount());
    }

    /**
     * Tests the behaviour of {@code consumeResources()} when a {@link ToolCard} is used.
     *
     * @see SinglePlayerController#consumeResources(ViewMessage)
     */
    @Test
    public void testConsumeResources() {
        Game game = GameUtils.getStartedGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");

        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", Colour.PURPLE),
                new ToolCard("Eglomise Brush", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", Colour.RED)};
        game.setToolCards(toolCards);

        List<Die> dice = new ArrayList<>(Arrays.asList(
                new Die(4, new Random(), Colour.YELLOW),
                new Die(5, new Random(), Colour.RED),
                new Die(3, new Random(), Colour.BLUE),
                new Die(6, new Random(), Colour.PURPLE)));
        game.getDraftPool().setDice(dice);
        game.getTurnManager().getCurrentTurn().setSacrificeIndex(3);

        Controller controller = new SinglePlayerController(game, 100, 100);
        game.getTurnManager().getCurrentTurn().setSelectedToolCard(toolCards[0]);
        IncrementDieValue msg = new IncrementDieValue(2, true, view, Action.APPLY_TOOL_CARD, playerName);
        controller.consumeResources(msg);

        Die removedDie = new Die(6, new Random(), Colour.PURPLE);
        List<Die> dice2 = new ArrayList<>(Arrays.asList(
                new Die(4, new Random(), Colour.YELLOW),
                new Die(5, new Random(), Colour.RED),
                new Die(3, new Random(), Colour.BLUE)));

        assertEquals(3, game.getDraftPool().getDice().size());
        assertFalse(game.getDraftPool().getDice().contains(removedDie));
        for (int i = 0; i < 3; i++) {
            assertTrue(dice2.get(i).equals(game.getDraftPool().getDice().get(i)));
        }
    }

    /**
     * Tests the disconnection of the player, when it is properly done.
     *
     * @see SinglePlayerController#disconnectPlayer(ViewMessage)
     */
    @Test
    public void testDisconnectPlayerSetUpCompleted() {
        Game game = GameUtils.getSetUpGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.DISCONNECT_PLAYER, playerName);
        controller.disconnectPlayer(msg);
        assertFalse(game.getPlayers().get(0).isConnected());
        assertEquals(1, game.getPlayers().size());
    }

    /**
     * Tests the disconnection of the player when the message contains a
     * wrong name.
     *
     * @see SinglePlayerController#disconnectPlayer(ViewMessage)
     */
    @Test
    public void testNegativeDisconnectPlayerSetUpCompleted() {
        Game game = GameUtils.getSetUpGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new SinglePlayerController(game, 100, 100);
        ViewMessage msg = new ViewMessage(view, Action.DISCONNECT_PLAYER, "Pluto");
        controller.disconnectPlayer(msg);
        assertTrue(game.getPlayers().get(0).isConnected());
        assertEquals(1, game.getPlayers().size());
    }

    /**
     * Tests the reconnection of the player when the player can reconnect.
     *
     * @see SinglePlayerController#reconnectPlayer(ViewMessage)
     */
    @Test
    public void testValidReconnectPlayer() {
        Game game = GameUtils.getSetUpGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new SinglePlayerController(game, 100, 100);
        game.getPlayers().get(0).setConnected(false);
        ViewMessage msg = new ViewMessage(view, Action.RECONNECT_PLAYER, playerName);
        controller.reconnectPlayer(msg);
        assertTrue(game.getPlayers().get(0).isConnected());
        assertEquals(1, game.getPlayers().size());
    }

    /**
     * Tests the reconnection of the player when the player that wants to reconnect can't
     * reconnect, because it has a wrong name.
     *
     * @see SinglePlayerController#reconnectPlayer(ViewMessage)
     */
    @Test
    public void testInvalidReconnectPlayer() {
        Game game = GameUtils.getSetUpGame(false);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new SinglePlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        game.getPlayers().get(0).setConnected(false);
        ViewMessage msg = new ViewMessage(view1, Action.RECONNECT_PLAYER, "Paperino");
        controller.reconnectPlayer(msg);
        assertFalse(game.getPlayers().get(0).isConnected());
        assertEquals(1, game.getPlayers().size());
    }

}
