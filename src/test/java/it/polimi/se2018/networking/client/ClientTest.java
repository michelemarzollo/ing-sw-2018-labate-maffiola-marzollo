package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.server.DummyServer;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for Client.
 */
public class ClientTest {

    /**
     * Tests if the class is instantiated correctly.
     */
    @Test
    public void testCorrectInstantiation(){
        MockView mockView = new MockView("Pippo");
        DummyServer server = new DummyServer();
        Client client = new Client(mockView, server);

        Assert.assertSame(server, client.getServer());
        Assert.assertEquals(mockView.getPlayerName(), client.getNetInterface().getUsername());
    }
}
