package it.polimi.se2018.networking.server;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Unit tests for TcpServer.
 */
public class TcpServerTest {

    /**
     * Tests that the server doesn't start at instantiation.
     */
    @Test
    public void testServerNotStartedAfterInstantiation() {
        TcpServer server = new TcpServer(7777);
        Assert.assertFalse(server.isRunning());
    }

    /**
     * Tests that the stop method has well-defined behaviour when it's called before the
     * start method.
     */
    @Test
    public void testStopWhenNotStarted() {
        TcpServer server = new TcpServer(7777);
        server.stop();
        Assert.assertFalse(server.isRunning());
    }

    /**
     * Tests that the server doesn't start at instantiation when it's a slave server.
     */
    @Test
    public void testTcpServerAsSlaveServer() {
        TcpServer server = new TcpServer(7777);
        TcpServer slave = new TcpServer(server, 8888);
        Assert.assertFalse(slave.isRunning());
    }

    /**
     * Tests the server lifecycle.
     */
    @Test
    public void testServerLifecycle() {
        TcpServer server = new TcpServer(7777);
        Assert.assertFalse(server.isRunning());
        server.start();
        //Can't know if there is already a service on specified port,
        //so it's impossible to assert if the server started or not
        server.stop();
        Assert.assertFalse(server.isRunning());
    }

    /**
     * Tests that the server doesn't start when it's run on an occupied port.
     */
    @Test
    public void testOccupiedPort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(7777);
        } catch (IOException ignored) {
            //Just ensure port is occupied
        }

        TcpServer server = new TcpServer(7777);

        server.start();
        Assert.assertFalse(server.isRunning());

        try {
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {
            //do nothing
        }

    }
}
