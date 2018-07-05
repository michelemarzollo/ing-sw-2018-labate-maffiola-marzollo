package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Pattern;
import it.polimi.se2018.utils.GameUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link GameSetup}.
 *
 * @author michelemarzollo
 */
public class GameSetupTest {

    /**
     * The game used by all tests.
     */
    private Game game;

    /**
     * Sets the game for all tests.
     */
    @Before
    public void setUp(){
        game = GameUtils.getSetUpGame(true);
    }

    /**
     * Tests the getter of the player, and that the constructor
     * has set it correctly.
     */
    @Test
    public void testGetPlayers(){
        GameSetup gameSetup = new GameSetup(game);

        assertEquals("Pippo", gameSetup.getPlayers()[0]);
        assertEquals("Pluto", gameSetup.getPlayers()[1]);
        assertEquals(2, gameSetup.getPlayers().length);
    }

    /**
     * Tests the getters for the cards, and that the constructor
     * has set them correctly.
     */
    @Test
    public void testCardGetters(){

        GameSetup gameSetup = new GameSetup(game);

        assertEquals(Colour.BLUE, gameSetup.getPrivateObjectives()[0][0].getColour());
        assertEquals(Colour.RED, gameSetup.getPrivateObjectives()[1][0].getColour());

        assertEquals(12, gameSetup.getToolCards().length);

        assertEquals(0, gameSetup.getPublicObjectives().length);
    }

    /**
     * Tests the getter for the candidate patterns, and that the constructor
     * has set them correctly.
     */
    @Test
    public void testGetCandidates(){
        GameSetup gameSetup = new GameSetup(game);

        Pattern[][] patterns = gameSetup.getCandidates();

        assertEquals(2, patterns[0].length);
        assertEquals(2, patterns[1].length);
        assertEquals(2, patterns.length);

        assertEquals("Duomo", patterns[0][0].getName());
    }
}
