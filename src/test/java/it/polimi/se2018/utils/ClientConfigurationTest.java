package it.polimi.se2018.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ClientConfiguration}.
 *
 * @author michelemarzollo
 */
public class ClientConfigurationTest {

    /**
     * Resets to null the singleton before every test.
     */
    @Before
    public void resetClientConfiguration() {
        ClientConfiguration.reset();
    }

    /**
     * Tests the method makeInstance when no other instance was created before.
     */
    @Test
    public void testMakeInstance() {
        ClientConfiguration.makeInstance("indirizzo", "nome", 2);
        assertTrue(tryGetConfigurationInstance() != null);
    }

    /**
     * Tests the method makeInstance when there is already a not null instance.
     */
    @Test
    public void testNegativeMakeInstance() {
        ClientConfiguration.makeInstance("indirizzo", "nome", 2);
        assertTrue(tryGetConfigurationInstance() != null);
        ClientConfiguration.makeInstance("indirizzo", "nome", 2);
        try {
            ClientConfiguration clientConfiguration = ClientConfiguration.getInstance();
            assertTrue(clientConfiguration != null);
            assertEquals(2, clientConfiguration.getPortNumber());
            assertEquals("indirizzo", clientConfiguration.getServerAddress());
            assertEquals("nome", clientConfiguration.getServiceName());
            assertEquals("nome", clientConfiguration.getServiceName());
        } catch (MissingConfigurationException e) {
            Assert.fail("The ClientConfiguration class wasn't instantiated: " + e.getMessage());

        }
    }

    /**
     * Tests the method getInstance() and getters.
     */
    @Test
    public void testGetInstance() {
        ClientConfiguration.makeInstance("indirizzo", "nome", 2);
        ClientConfiguration clientConfiguration = tryGetConfigurationInstance();
        assertTrue(clientConfiguration != null);
        assertEquals(2, clientConfiguration.getPortNumber());
        assertEquals("indirizzo", clientConfiguration.getServerAddress());
        assertEquals("nome", clientConfiguration.getServiceName());
    }

    /**
     * Tests the method reset.
     */
    @Test
    public void testReset() {
        ClientConfiguration.makeInstance("indirizzo", "nome", 2);
        assertTrue(tryGetConfigurationInstance() != null);
        ClientConfiguration.reset();
        try {
            ClientConfiguration.getInstance();
            Assert.fail("There was an instance");
        } catch (MissingConfigurationException e) {
            assertEquals("There is no valid configuration for the client!", e.getMessage());
        }
    }


    /**
     * Tries to get the instance of the singleton.
     *
     * @return the instance of the singleton.
     */
    private ClientConfiguration tryGetConfigurationInstance() {
        try {
            return ClientConfiguration.getInstance();
        } catch (MissingConfigurationException e) {
            Logger.getDefaultLogger().log("The ClientConfiguration class wasn't instantiated: " + e.getMessage());
            return null;
        }
    }

}
