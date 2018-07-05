package it.polimi.se2018.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the class {@link XmlServerConfigLoader}.
 *
 * @author michelemarzollo
 */
public class XmlServerConfigLoaderTest {

    /**
     * Resets to null the singleton before every test.
     */
    @Before
    public void resetServerConfiguration() {
        ServerConfiguration.reset();
    }

    /**
     * Tests if a well-formed xml configuration description is correctly loaded.
     */
    @Test
    public void testConfigurationLoading() {

        String path = getClass().getClassLoader().getResource("it/polimi/se2018/utils/configuration/server_configuration.xml").toString();
        path = path.substring(5);

        try {
            XmlServerConfigLoader loader = new XmlServerConfigLoader(path);
            ServerConfiguration configuration = loader.loadConfiguration();

            assertEquals(7777, configuration.getPortNumber());
            assertEquals("localhost", configuration.getAddress());
            assertEquals("MyServer", configuration.getServiceName());
            assertEquals(300, configuration.getTurnDuration());
            assertEquals(20, configuration.getMultiPlayerTimeOut());

        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tests if ill-formed configurations are ignored during the loading phase.
     */
    @Test
    public void testBadConfigurationLoading() {
        String path = getClass().getClassLoader().getResource("it/polimi/se2018/utils/public_objective_cards/bad/xmls/bad_card.xml").toString();
        path = path.substring(5);

        try {
            XmlServerConfigLoader loader = new XmlServerConfigLoader(path);
            ServerConfiguration configuration = loader.loadConfiguration();

            assertNull(configuration);
        } catch (SAXException e) {
            Assert.assertEquals("", e.getMessage());
        }
    }
}
