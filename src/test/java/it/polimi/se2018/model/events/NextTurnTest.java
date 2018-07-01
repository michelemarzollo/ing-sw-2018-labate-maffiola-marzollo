package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Turn;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

/**
 * Unit tests for NextTurn.
 */
public class NextTurnTest {

    /**
     * The player instance used by tests.
     */
    private Player player;

    /**
     * Initializes the player attribute.
     */
    @Before
    public void setup(){
        Game game = new Game();
        player = new Player("pippo");
        game.addPlayer(player);
    }

    /**
     * Tests if equals(Object) detects positive cases.
     */
    @Test
    public void testEqualObjects(){
        Turn turn = new Turn(player, true);
        NextTurn nextTurn = new NextTurn(turn);
        boolean equals = nextTurn.equals((Object) nextTurn);
        Assert.assertTrue(equals);
    }

    /**
     * Tests if equals(Object) detects negative cases.
     */
    @Test
    public void testNotEqualObjects(){
        Turn turn = new Turn(player, true);
        NextTurn nextTurn = new NextTurn(turn);
        Integer other = 4;
        boolean equals = nextTurn.equals((Object) other);
        Assert.assertFalse(equals);
    }

    /**
     * Tests if equals(NextTurn) detects positive cases.
     */
    @Test
    public void testEqualNextTurn(){
        Turn turn = new Turn(player, true);
        NextTurn first = new NextTurn(turn);
        NextTurn second = new NextTurn(turn);
        boolean equals = first.equals(second);
        Assert.assertTrue(equals);
    }

    /**
     * Tests if equals(NextTurn) detects negative cases.
     */
    @Test
    public void testNotEqualNextTurn(){
        Turn turn = new Turn(player, true);
        NextTurn first = new NextTurn(turn);
        turn.placeDie();
        NextTurn second = new NextTurn(turn);
        boolean equals = first.equals(second);
        Assert.assertFalse(equals);
    }

    /**
     * Tests if hashCode() is the default one.
     */
    @Test
    public void testHashCode(){
        Turn turn = new Turn(player, true);
        NextTurn nextTurn = new NextTurn(turn);
        Assert.assertEquals(Objects.hashCode(nextTurn), nextTurn.hashCode());
    }

}
