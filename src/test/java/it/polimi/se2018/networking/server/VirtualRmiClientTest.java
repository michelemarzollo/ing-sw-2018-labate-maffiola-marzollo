package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.DummyClient;
import it.polimi.se2018.networking.client.RmiClientImplementation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for VirtualRmiClient.
 */
public class VirtualRmiClientTest {

    /**
     * Tests if the correct username is returned.
     */
    @Test
    public void testGetUsername(){
        String name = "Pippo";
        RmiClientImplementation clientImplementation = new RmiClientImplementation(
                new DummyClient(name));
        VirtualRmiClient client = new VirtualRmiClient(
                new DummyServer(),
                clientImplementation);

        Assert.assertEquals(name, client.getUsername());
    }
}
