package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.DummyClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for DelegateNetInterface.
 */
public class DelegateNetInterfaceTest {

    /**
     * Master server used in tests.
     */
    private RmiServer master;
    /**
     * Slave server that provides the object instance.
     */
    private RmiServer slave;

    /**
     * Sets up a master-slave system of two servers without starting them.
     */
    @Before
    public void setup(){
        master = new RmiServer("localhost", "master");
        slave = new RmiServer(master, "localhost", "salve");
    }

    /**
     * Tests if a client is correctly added.
     * <p>The slave server mustn't keep trace of connections, but the master server
     * has to.</p>
     */
    @Test
    public void testAddClientSuccessful(){
        String name = "Pippo";
        DummyClient client = new DummyClient(name);

        slave.getServerNetInterface().addClient(client, false);

        Assert.assertEquals(0, slave.getClients().size());

        Assert.assertSame(client, master.getClientFor(name));
        Assert.assertEquals(name, master.getClientFor(name).getUsername());
    }

    /**
     * Tests if a client connection with duplicate username is rejected.
     * <p>The slave server mustn't keep trace of connections, but the master server
     * has to.</p>
     */
    @Test
    public void testAddClientUnsuccessful(){
        String name = "Pippo";
        DummyClient firstClient = new DummyClient(name);
        DummyClient secondClient = new DummyClient(name);
        slave.getServerNetInterface().addClient(firstClient, false);
        slave.getServerNetInterface().addClient(secondClient, false);

        Assert.assertEquals(0, slave.getClients().size());

        Assert.assertEquals(1, master.getClients().size());
        Assert.assertSame(firstClient, master.getClientFor(name));
        Assert.assertEquals(name, master.getClientFor(name).getUsername());
    }

    /**
     * Tests if a client can be removed.
     * <p>The slave server mustn't keep trace of connections, but the master server
     * has to.</p>
     */
    @Test
    public void testRemoveClient(){
        String name = "Pippo";
        DummyClient client = new DummyClient(name);
        slave.getServerNetInterface().addClient(client, false);
        slave.getServerNetInterface().removeClient(client);

        Assert.assertEquals(0, slave.getClients().size());

        Assert.assertEquals(0, master.getClients().size());
        Assert.assertNull(master.getClientFor(name));
    }

}
