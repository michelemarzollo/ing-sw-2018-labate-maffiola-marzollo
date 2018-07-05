package it.polimi.se2018.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link ServerConfiguration}.
 *
 * @author michelemarzollo
 */
public class ServerConfigurationTest {

    /**
     * Resets to null the singleton before every test.
     */
    @Before
    public void resetServerConfiguration() {
        ServerConfiguration.reset();
    }

    /**
     * Tests the method makeInstance when no other instance was created before.
     */
    @Test
    public void testMakeInstance() {
        ServerConfiguration.makeInstance(2, "indirizzo", "nome", 2, 3);
        assertTrue(tryGetConfigurationInstance() != null);
    }

    /**
     * Tests the method makeInstance when there is already a not null instance.
     */
    @Test
    public void testNegativeMakeInstance() {
        ServerConfiguration.makeInstance(2, "indirizzo", "nome", 2, 3);
        assertTrue(tryGetConfigurationInstance() != null);
        ServerConfiguration.makeInstance(20, "indirizzo1", "nome1", 20, 30 );
        try {
            ServerConfiguration serverConfiguration = ServerConfiguration.getInstance();
            assertTrue(serverConfiguration != null);
            assertEquals(2, serverConfiguration.getTurnDuration());
            assertEquals(2, serverConfiguration.getPortNumber());
            assertEquals(3, serverConfiguration.getMultiPlayerTimeOut());
            assertEquals("indirizzo", serverConfiguration.getAddress());
            assertEquals("nome", serverConfiguration.getServiceName());
        } catch (MissingConfigurationException e) {
            Assert.fail("The ServerConfiguration class wasn't instantiated: " + e.getMessage());

        }
    }

    /**
     * Tests the method getInstance() and getters.
     */
    @Test
    public void testGetInstance() {
        ServerConfiguration.makeInstance(2, "indirizzo", "nome", 2, 3);
        ServerConfiguration serverConfiguration = tryGetConfigurationInstance();
        assertTrue(serverConfiguration != null);
        assertEquals(2, serverConfiguration.getTurnDuration());
        assertEquals(2, serverConfiguration.getPortNumber());
        assertEquals(3, serverConfiguration.getMultiPlayerTimeOut());
        assertEquals("indirizzo", serverConfiguration.getAddress());
        assertEquals("nome", serverConfiguration.getServiceName());
    }

    /**
     * Tests the method reset.
     */
    @Test
    public void testReset() {
        ServerConfiguration.makeInstance(2, "indirizzo", "nome", 2, 3);
        assertTrue(tryGetConfigurationInstance() != null);
        ServerConfiguration.reset();
        try {
            ServerConfiguration.getInstance();
            Assert.fail("There was an instance");
        } catch (MissingConfigurationException e) {
            assertEquals("There is no valid configuration for the server!", e.getMessage());
        }
    }


    /**
     * Tries to get the instance of the singleton.
     *
     * @return the instance of the singleton.
     */
    private ServerConfiguration tryGetConfigurationInstance() {
        try {
            return ServerConfiguration.getInstance();
        } catch (MissingConfigurationException e) {
            Logger.getDefaultLogger().log("The ServerConfiguration class wasn't instantiated: " + e.getMessage());
            return null;
        }
    }
}
