package it.polimi.se2018.networking.server;

import org.junit.Assert;
import org.junit.Test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Unit tests for RmiServer.
 */
public class RmiServerTest {

    /**
     * Tests that the server doesn't start at instantiation.
     */
    @Test
    public void testServerNotStartedAfterInstantiation() {
        RmiServer server = new RmiServer("localhost", "MyServer");
        Assert.assertFalse(server.isRunning());
    }

    /**
     * Tests that the server doesn't start at instantiation when it's a slave server.
     */
    @Test
    public void testRmiServerAsSlaveServer(){
        RmiServer master = new RmiServer("localhost", "master");
        RmiServer slave = new RmiServer(master, "localhost", "master");
        Assert.assertFalse(slave.isRunning());
    }

    /**
     * Tests that the server doesn't start when a malformed url is provided.
     */
    @Test
    public void testMalformedUrl() {
        RmiServer server = new RmiServer("!", "MyServer");
        server.start();
        Assert.assertFalse(server.isRunning());
        server.stop();
        Assert.assertFalse(server.isRunning());
    }

    /**
     * Tests that the stop method has well-defined behaviour when it's called before the
     * start method.
     */
    @Test
    public void testStopWhenNoRegistryExists(){
        RmiServer server = new RmiServer("localhost", "MyServer");
        server.stop();
        Assert.assertFalse(server.isRunning());
    }

    /**
     * Tests if the server starts correctly even when the rmi registry already
     * exists.
     */
    @Test
    public void testAlreadyPresentRegistry() {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException ignore) {
            //Just ensure a registry already exists
        }
        RmiServer server = new RmiServer("localhost", "MyServer");
        server.start();
        Assert.assertTrue(server.isRunning());
    }

    /**
     * Tests the server lifecycle.
     */
    @Test
    public void testServerLifecycle(){
        RmiServer server = new RmiServer("localhost", "MyServer");
        server.start();
        Assert.assertTrue(server.isRunning());
        server.stop();
        Assert.assertFalse(server.isRunning());
    }
}
