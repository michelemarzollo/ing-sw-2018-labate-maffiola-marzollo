package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToolCardFactoryTest {

    //verifies that 'a' does not contain duplicates Tool Cards.
    private boolean noDups(ToolCard[] a, int n) {
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
        ToolCard[] result = factory.getInstances(5);
        assertEquals(5, result.length);
        assertTrue(noDups(result, 5));
        }
    }
