package it.polimi.se2018.controller;

import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

/**
 * Tests for {@link ToolCardFactory}.
 */
public class ToolCardFactoryTest {

    @Before
    public void setUp() {
        ToolCardFactory.reset();
        if (ToolCardFactory.getInstance() == null){
            try {
                XmlToolCardLoader xmlToolCardLoader = new XmlToolCardLoader();
                xmlToolCardLoader.createToolCardFactory();
            } catch (SAXException e) {
                Logger.getDefaultLogger().log("USAXException " + e);
            }
        }
    }

    /**
     * Tests if the factory throws IllegalArgumentException if too
     * many instances are requested.
     */
    @Test
    public void testRequestTooManyCards() {
        int instances = 20;
        try {
            if (ToolCardFactory.getInstance() == null)
                Assert.fail("Not instantiated!");
            ToolCardFactory.getInstance().newInstances(instances);
            Assert.fail("Creates too many cards!");

        } catch (IllegalArgumentException ignored) {

        }

    }

    /**
     * Tests a case that should behave as expected.
     */
    @Test
    public void testGetInstances() {
        if (ToolCardFactory.getInstance() == null)
            Assert.fail("Not instantiated!");
        ToolCard[] result = ToolCardFactory.getInstance().newInstances(3);
        assertEquals(3, result.length);
        assertTrue(noDuplications(result));

    }

    /**
     * Verifies that 'a' does not contain duplicates Tool Cards.
     */
    private boolean noDuplications(ToolCard[] a) {
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {
                if (a[i].getColour().equals(a[j].getColour()) && a[i].getName().equals(a[j].getName())) return false;
            }
        }
        return true;
    }
}
