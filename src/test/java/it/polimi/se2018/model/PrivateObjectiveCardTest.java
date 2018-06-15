package it.polimi.se2018.model;

import org.junit.Assert;
import org.junit.Test;

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
