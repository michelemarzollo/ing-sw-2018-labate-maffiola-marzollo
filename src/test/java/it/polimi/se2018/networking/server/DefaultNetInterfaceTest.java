package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.DummyClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for DefaultNetInterface.
 */
public class DefaultNetInterfaceTest {

    /**
     * Server used to test DefaultNetInterface.
     */
    private RmiServer server;

    /**
     * Prepares the server without starting it.
     */
    @Before
    public void setup(){
        server = new RmiServer("localhost", "MyServer");
    }

    /**
     * Tests if a client connection is correctly added.
     */
    @Test
    public void testAddClientSuccessful(){
        String name = "Pippo";
        DummyClient client = new DummyClient(name);
        server.getServerNetInterface().addClient(client, false);

        Assert.assertSame(client, server.getClientFor(name));
        Assert.assertEquals(name, server.getClientFor(name).getUsername());
    }

    /**
     * Tests if a connection with a duplicate username is rejected.
     */
    @Test
    public void testAddClientUnsuccessful(){
        String name = "Pippo";
        DummyClient firstClient = new DummyClient(name);
        DummyClient secondClient = new DummyClient(name);
        server.getServerNetInterface().addClient(firstClient, false);
        server.getServerNetInterface().addClient(secondClient, false);

        Assert.assertEquals(1, server.getClients().size());
        Assert.assertSame(firstClient,server.getClientFor(name));
        Assert.assertEquals(name, server.getClientFor(name).getUsername());
    }

    /**
     * Tests if a client can be removed.
     */
    @Test
    public void testRemoveClient(){
        String name = "Pippo";
        DummyClient client = new DummyClient(name);
        server.getServerNetInterface().addClient(client, false);
        server.getServerNetInterface().removeClient(client);

        Assert.assertEquals(0, server.getClients().size());
        Assert.assertNull(server.getClientFor(name));
    }

}
