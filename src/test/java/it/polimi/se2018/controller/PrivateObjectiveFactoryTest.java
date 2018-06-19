package it.polimi.se2018.controller;

import it.polimi.se2018.controller.PrivateObjectiveFactory;
import it.polimi.se2018.model.PrivateObjectiveCard;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit tests for PrivateObjectiveFactory class.
 */
public class PrivateObjectiveFactoryTest {

    /**
     * Tests if the factory returns the correct number of instances.
     */
    @Test
    public void testCorrectNumberOfInstances() {
        int instances = 4;
        PrivateObjectiveFactory factory = new PrivateObjectiveFactory();
        PrivateObjectiveCard[] cards = factory.newInstances(instances);

        long actualInstances = cards.length;

        Assert.assertEquals(instances, actualInstances);

    }

    /**
     * Tests if the factory returns a set of unique cards.
     */
    @Test
    public void testNoCardRepetition() {
        int instances = 5;
        PrivateObjectiveFactory factory = new PrivateObjectiveFactory();
        PrivateObjectiveCard[] cards = factory.newInstances(instances);

        long distinctCards = Arrays.stream(cards)
                .map(PrivateObjectiveCard::getName)
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
        PrivateObjectiveFactory factory = new PrivateObjectiveFactory();
        try {
            PrivateObjectiveCard[] cards = factory.newInstances(instances);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }
    }
}
