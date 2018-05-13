package it.polimi.se2018.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit tests for PublicObjectiveFactory class.
 */
public class PublicObjectiveFactoryTest {

    /**
     * Tests if the factory returns the correct number of instances.
     */
    @Test
    public void testCorrectNumberOfInstances() {
        int instances = 5;
        PublicObjectiveFactory factory = new PublicObjectiveFactory();
        PublicObjectiveCard[] cards = factory.newInstances(instances);

        long actualInstances = cards.length;

        Assert.assertEquals(instances, actualInstances);

    }

    /**
     * Tests if the factory returns a set of unique cards.
     */
    @Test
    public void testNoCardRepetition() {
        int instances = 10;
        PublicObjectiveFactory factory = new PublicObjectiveFactory();
        PublicObjectiveCard[] cards = factory.newInstances(instances);

        long distinctCards = Arrays.stream(cards)
                .distinct()
                .count();

        Assert.assertEquals(instances, distinctCards);
    }

    /**
     * Tests if the factory throws IllegalArgumentException if too
     * many instances are requested.
     */
    @Test
    public void testRequestTooManyCards() {
        int instances = 20;
        PublicObjectiveFactory factory = new PublicObjectiveFactory();
        try {
            PublicObjectiveCard[] cards = factory.newInstances(instances);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }

    }
}
