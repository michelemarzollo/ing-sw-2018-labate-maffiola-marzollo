package it.polimi.se2018.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link PublicObjectiveCard}.
 */
public class PublicObjectiveCardTest {

    /**
     * The name.
     */
    private String name = "Card";

    /**
     * The description.
     */
    private String description = "Description";

    /**
     * The victory points.
     */
    private int victoryPoints = 3;

    /**
     * The card.
     */
    private PublicObjectiveCard publicObjectiveCard;

    /**
     * Creates the object.
     */
    @Before
    public void callConstructor(){
        publicObjectiveCard = new PublicObjectiveCard(name, description, victoryPoints);
    }

    /**
     * Tests getName().
     */
    @Test
    public void testGetName() {
        Assert.assertEquals(name, publicObjectiveCard.getName());
    }

    /**
     * Tests getDescription().
     */
    @Test
    public void testGetDescription() {
        Assert.assertEquals(description, publicObjectiveCard.getDescription());
    }

    /**
     * Tests getVictoryPoints().
     */
    @Test
    public void testGetVictoryPoints() {
        Assert.assertEquals(victoryPoints, publicObjectiveCard.getVictoryPoints());
    }
}
