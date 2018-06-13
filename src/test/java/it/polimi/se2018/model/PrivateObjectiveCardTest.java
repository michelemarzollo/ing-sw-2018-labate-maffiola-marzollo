package it.polimi.se2018.model;

import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests for PrivateObjectiveCard class.
 */
public class PrivateObjectiveCardTest {

    /**
     * Tests if the object is correctly instantiated and initialized.
     */
    @Test
    public void testConstructor() {
        String name = "TestCard";
        Colour colour = Colour.BLUE;
        String description = "TestDescription";
        PrivateObjectiveCard card = new PrivateObjectiveCard(name, colour, description);
        Assert.assertEquals(name, card.getName());
        Assert.assertEquals(colour, card.getColour());
        Assert.assertEquals(description, card.getDescription());
    }

}
