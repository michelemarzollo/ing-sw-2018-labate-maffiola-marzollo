package it.polimi.se2018.model;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ToolCardFactoryTest {

    /**
     * Tests if the factory throws IllegalArgumentException if too
     * many instances are requested.
     */
    @Test
    public void testRequestTooManyCards() {
        int instances = 20;
        ToolCardFactory factory = new ToolCardFactory();
        try {
            ToolCard[] cards = factory.newInstances(instances);
            Assert.fail();
        } catch (IllegalArgumentException ignored) {

        }

    }

    //verifies that 'a' does not contain duplicates Tool Cards.
    private boolean noDuplications(ToolCard[] a, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if(a[i].getColour().equals(a[j].getColour()) && a[i].getName().equals(a[j].getName())) return false;
            }
        }
        return true;
    }
    @Test
    public void testGetInstances() {
        ToolCardFactory factory = new ToolCardFactory();
        ToolCard[] result = factory.newInstances(5);
        assertEquals(5, result.length);
        assertTrue(noDuplications(result, 5));
        }
    }
