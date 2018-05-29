package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The class to test the methods of {@link Player}.
 *
 * @author michelemarzollo
 */
public class TestPlayer {

    /**
     * The player that will be used for tests.
     */
    private Player player;

    /**
     * Creates a {@link Player} (common for all test methods).
     */
    @Before
    public void setUp() {
        player = new Player("Michele");
        player.setGame(new Game());
    }

    /**
     * Tests getter and setter for {@code score}.
     */
    @Test
    public void testScore() {
        player.setScore(12);
        assertEquals(12, player.getScore());
    }

    /**
     * Tests getter and setter for the candidate pattern cards.
     */
    @Test
    public void testPatternCandidates() {

        Pattern[] p = new Pattern[2];

        //instantiation of two different patterns

        //first grid
        Cell[][] grid = new Cell[4][5];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell();
            }
        }

        grid[0][1] = new Cell(Colour.BLUE);
        grid[0][2] = new Cell(2);
        grid[0][4] = new Cell(Colour.YELLOW);
        grid[1][1] = new Cell(4);
        grid[1][3] = new Cell(Colour.RED);

        //second grid
        Cell[][] grid1 = new Cell[4][5];

        for (int i = 0; i < grid1.length; i++) {
            for (int j = 0; j < grid1[i].length; j++) {
                grid1[i][j] = new Cell();
            }
        }

        grid1[2][2] = new Cell(5);
        grid1[2][3] = new Cell(Colour.YELLOW);
        grid1[3][0] = new Cell(Colour.GREEN);
        grid1[3][1] = new Cell(3);
        grid1[3][4] = new Cell(Colour.PURPLE);

        p[0] = new Pattern("Pluto", 5, grid);
        p[1] = new Pattern("Pippo", 3, grid1);

        //tests the methods setCandidates and getCandidates

        //at the beginning candidates must be set to null
        assertNull(player.getCandidates());
        //setting of candidates
        player.setCandidates(p);
        assertTrue(p[0].equals(player.getCandidates()[0]) &&
                p[1].equals(player.getCandidates()[1]));

    }

    /**
     * Tests the getter and setter for {@code cards} in single player configuration.
     */
    @Test
    public void testCardsSinglePlayer() {

        PrivateObjectiveFactory p = new PrivateObjectiveFactory();
        PrivateObjectiveCard[] cards = p.newInstances(2);
        player.setCards(cards);
        assertTrue(cards[0] == player.getCards()[0] &&
                cards[1] == player.getCards()[1]);

    }

    /**
     * Tests the getter and setter for {@code cards} in single player configuration.
     */
    @Test
    public void testCardsMultiPlayer() {

        PrivateObjectiveFactory p = new PrivateObjectiveFactory();
        PrivateObjectiveCard[] cards = p.newInstances(1);
        player.setCards(cards);
        assertTrue(cards[0] == player.getCards()[0]);

    }

    /**
     * Tests that the player is by default connected. When the connection is set,
     * it must assume the new value.
     */
    @Test
    public void testSetConnection() {

        assertTrue(player.isConnected());
        player.setConnected(true);
        assertTrue(player.isConnected());

    }

    /**
     * Tests the correct setting of a pattern.
     */
    @Test
    public void testSetPattern() {

        Cell[][] grid = new Cell[4][5];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell();
            }
        }

        Pattern p = new Pattern("SunCatcher", 4, grid);
        player.setPattern(p);
        assertEquals("SunCatcher", player.getPattern().getName());
    }

    /**
     * Tests the method {@code consumeTokens()} when the tokens required
     * are more the the ones available. It must catch an exception.
     */
    @Test
    public void testConsumeTokensFail() {

        player.setTokens(4);
        try {
            player.consumeTokens(5);
            fail();
        } catch (NotEnoughTokensException e) {
            //The tokens, if not enough, mustn't be used
            assertEquals(4, player.getTokens());
        }
    }

    /**
     * Tests the method {@code consumeTokens()} when the tokens required
     * are less the the ones available. It must catch decrease the attribute {@code tokens}.
     */
    @Test
    public void testConsumeTokensSuccessful() {

        player.setTokens(4);
        try {
            player.consumeTokens(3);
            assertEquals(1, player.getTokens());
        } catch (NotEnoughTokensException e) {
            fail();
        }

    }

    /**
     * Tests the method {@code consumeTokens()} in the limit case when 0 tokens are requested
     */
    @Test
    public void testConsumeTokensLimit() {

        player.setTokens(4);
        try {
            player.consumeTokens(0);
            assertEquals(4, player.getTokens());
        } catch (NotEnoughTokensException e) {
            fail();
        }

    }

}