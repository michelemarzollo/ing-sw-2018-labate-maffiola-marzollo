package it.polimi.se2018.networking.messages;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.ToolCard;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link Message}.
 *
 * @author michelemarzollo
 */
public class MessageTest {

    /**
     * Tests the getter for {@code body} and the constructor.
     */
    @Test
    public void testGetBody(){
        ToolCard toolCard =  new ToolCard("Name", "Description", Colour.RED);
        Message message = new Message(Command.ACK, toolCard);
        Assert.assertEquals(toolCard, message.getBody());
    }

    /**
     * Tests the getter for {@code command} and the constructor.
     */
    @Test
    public void testGetCommand(){
        ToolCard toolCard =  new ToolCard("Name", "Description", Colour.RED);
        Message message = new Message(Command.ACK, toolCard);
        Assert.assertEquals(Command.ACK, message.getCommand());
    }
}
