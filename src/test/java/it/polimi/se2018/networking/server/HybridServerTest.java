package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.client.DummyClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Unit tests for HybridServer.
 */
public class HybridServerTest {

    /**
     * Tests if the server lifecycle is correct.
     */
    @Test
    public void testLifecycle(){
        HybridServer server = new HybridServer("localhost", "MyService", 7777);
        Assert.assertFalse(server.isRunning());
        server.start();
        //At least the rmi server is running
        Assert.assertTrue(server.isRunning());
        server.stop();
        Assert.assertFalse(server.isRunning());
    }

    /**
     * Tests if clients can be added.
     */
    @Test
    public void testAddClientSuccessful(){
        HybridServer server = new HybridServer("localhost", "MyService", 7777);
        DummyClient firstClient = new DummyClient("pippo");
        DummyClient secondClient = new DummyClient("pluto");
        boolean success = server.addClient(firstClient);
        Assert.assertTrue(success);
        success = server.addClient(secondClient);
        Assert.assertTrue(success);
    }

    /**
     * Tests if a client with a username already registered is rejected.
     */
    @Test
    public void testAddClientDuplicateNamesFailure(){
        HybridServer server = new HybridServer("localhost", "MyService", 7777);
        String name = "pippo";
        DummyClient firstClient = new DummyClient(name);
        DummyClient secondClient = new DummyClient(name);
        boolean success = server.addClient(firstClient);
        Assert.assertTrue(success);
        success = server.addClient(secondClient);
        Assert.assertFalse(success);
    }

    /**
     * Tests if null is returned when the connection bound to an unregistered username
     * is retrieved.
     */
    @Test
    public void testRetrieveUnexistingClient(){
        HybridServer server = new HybridServer("localhost", "MyService", 7777);
        ClientNetInterface client = server.getClientFor("pippo");
        Assert.assertNull(client);
    }

    /**
     * Tests if the correct object is returned when the connection bound to a registered username
     * is retrieved.
     */
    @Test
    public void testRetrieveClient(){
        HybridServer server = new HybridServer("localhost", "MyService", 7777);

        String name = "pippo";
        DummyClient expectedClient = new DummyClient(name);
        boolean success = server.addClient(expectedClient);
        Assert.assertTrue(success);

        ClientNetInterface actualClient = server.getClientFor(name);

        Assert.assertEquals(expectedClient.getUsername(), actualClient.getUsername());
    }

    /**
     * Tests if the list of clients returned by the server is correct.
     */
    @Test
    public void testRetrieveAllClients(){
        HybridServer server = new HybridServer("localhost", "MyService", 7777);

        String name = "pippo";
        DummyClient expectedClient = new DummyClient(name);
        boolean success = server.addClient(expectedClient);
        Assert.assertTrue(success);

        List<ClientNetInterface> clients = server.getClients();

        Assert.assertEquals(1, clients.size());
        Assert.assertEquals(expectedClient.getUsername(), clients.get(0).getUsername());
    }
}
