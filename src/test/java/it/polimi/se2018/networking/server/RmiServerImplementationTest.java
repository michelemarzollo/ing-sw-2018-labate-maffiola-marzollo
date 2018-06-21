package it.polimi.se2018.networking.server;

import it.polimi.se2018.networking.client.DummyClient;
import it.polimi.se2018.networking.client.RmiClientImplementation;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.RemoteException;

/**
 * Unit tests for RmiServerImplementation.
 */
public class RmiServerImplementationTest {

    /**
     * Tests if a client is correctly added.
     */
    @Test
    public void testAddClient(){
        DummyServer server = new DummyServer();
        RmiServerInterface serverImplementation = null;
        try {
            serverImplementation = new RmiServerImplementation(server);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }
        String name = "Pippo";
        try {
            serverImplementation.addClient(new RmiClientImplementation(new DummyClient(name)), false);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(1, server.getClients().size());
        Assert.assertEquals(name, server.getClients().get(0).getUsername());
    }

    /**
     * Tests if a client is correctly removed.
     */
    @Test
    public void testRemoveClient(){
        DummyServer server = new DummyServer();
        RmiServerInterface serverImplementation = null;
        try {
            serverImplementation = new RmiServerImplementation(server);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }
        String name = "Pippo";
        try {
            serverImplementation.addClient(new RmiClientImplementation(new DummyClient(name)), false);
            serverImplementation.removeClient(new RmiClientImplementation(new DummyClient(name)));
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(0, server.getClients().size());
    }

    /**
     * Negative test for equals method.
     */
    @Test
    public void testNotEquals(){
        DummyServer server = new DummyServer();
        RmiServerImplementation first = null;
        RmiServerImplementation second = null;
        try {
            first = new RmiServerImplementation(server);
            second = new RmiServerImplementation(server);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNotEquals(first, second);
    }

    /**
     * Positive test for equals method.
     */
    @Test
    public void testEquals(){
        DummyServer server = new DummyServer();
        RmiServerImplementation serverImplementation = null;
        try {
            serverImplementation = new RmiServerImplementation(server);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(serverImplementation, serverImplementation);
    }

    /**
     * Tests if two equal objects have the same hash code.
     */
    @Test
    public void testHashCode(){
        DummyServer server = new DummyServer();
        RmiServerImplementation serverImplementation = null;
        try {
            serverImplementation = new RmiServerImplementation(server);
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(serverImplementation.hashCode(), serverImplementation.hashCode());
    }
}
