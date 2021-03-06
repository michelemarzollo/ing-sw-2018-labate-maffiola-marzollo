package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ToolCardTest {

    @Test
    public void testGetName() {
        ToolCard card = new ToolCard("Trial", "Description", Colour.PURPLE);
        assertEquals("Trial", card.getName());
    }

    @Test
    public void testGetColour() {
        ToolCard card = new ToolCard("Trial", "Description", Colour.PURPLE);
        assertEquals(Colour.PURPLE, card.getColour());
    }


    @Test
    public void testUse() {
        ToolCard card = new ToolCard("Trial", "Description", Colour.PURPLE);
        card.use();
        assertEquals(true, card.isUsed());
    }

    @Test
    public void testGetDescription(){
        ToolCard card = new ToolCard("Trial", "Description", Colour.PURPLE);
        assertEquals("Description", card.getDescription());
    }
}