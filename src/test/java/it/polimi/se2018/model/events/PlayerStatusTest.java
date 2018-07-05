package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Pattern;
import it.polimi.se2018.utils.GridUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests fo {@link PlayerStatus}.
 *
 * @author michelemarzollo
 */
public class PlayerStatusTest {

    /**
     * Tests the constructor for the mock PlayerStatus.
     */
    @Test
    public void testSecondConstructor(){

        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);
        Pattern pattern = new Pattern("Name", 5, grid);
        PlayerStatus playerStatus = new PlayerStatus("Pippo", 3, pattern);

        assertEquals("Pippo", playerStatus.getPlayerName());
        assertEquals(3, playerStatus.getTokens());
        Pattern messagePattern = playerStatus.getPattern();
        assertEquals(pattern.getName(), playerStatus.getPattern().getName());
        assertEquals(pattern.getDifficulty(), playerStatus.getPattern().getDifficulty());
    }

    /**
     * Tests the getters of the class.
     */
    @Test
    public void testGetters(){
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);
        Pattern pattern = new Pattern("Name", 5, grid);
        PlayerStatus playerStatus = new PlayerStatus("Pippo", 3, pattern);

        String playerName = playerStatus.getPlayerName();
        int tokens = playerStatus.getTokens();
        Pattern pattern1 = playerStatus.getPattern();

        assertEquals("Pippo", playerName);
        assertEquals(3, tokens);
        assertEquals(pattern, pattern1);
    }

}
