package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import org.junit.Assert;
import org.junit.Test;

/**
 * Adds tests for methods not tested by the controller test classes of the class
 * {@link CardDealer}.
 *
 * @author michelemarzollo
 */
public class CardDealerTest {

    /**
     * Tests that the method {@code dealToolCards}, if there is no instance of the
     * {@link ToolCardFactory}, creates an instance of the singleton.
     */
    @Test
    public void testDealToolCards(){
        ToolCardFactory.reset();
        Game game =  new Game();
        CardDealer cardDealer =  new CardDealer(game);
        cardDealer.deal(2, 3, 5);
        Assert.assertTrue(ToolCardFactory.getInstance()!=null);
    }
}
