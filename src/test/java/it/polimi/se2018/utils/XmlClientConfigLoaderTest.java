package it.polimi.se2018.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


import static org.junit.Assert.*;

/**
 * Unit tests for the class {@link XmlClientConfigLoader}.
 *
 * @author michelemarzollo
 */
public class XmlClientConfigLoaderTest {

    /**
     * Resets to null the singleton before every test.
     */
    @Before
    public void resetClientConfiguration() {
        ClientConfiguration.reset();
    }

    /**
     * Tests if a well-formed xml configuration description is correctly loaded.
     */
    @Test
    public void testConfigurationLoading() {

        // the path has to be "C:\\Users\\Michi\\IdeaProjects\\ing-sw-2018-labate-maffiola-marzollo\\src\\test\\resources\\it\\polimi\\se2018\\utils\\configuration\\client_configuration.xml"
        String path = getClass().getClassLoader().getResource("it/polimi/se2018/utils/configuration/client_configuration.xml").toString();
        path = path.substring(5);

        try {
            XmlClientConfigLoader loader = new XmlClientConfigLoader(path);
            ClientConfiguration configuration = loader.loadConfiguration();

            assertEquals("localhost", configuration.getServerAddress());
            assertEquals("MyServer", configuration.getServiceName());
            assertEquals(7777, configuration.getPortNumber());

        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tests if ill-formed configurations are ignored during the loading phase.
     */
    @Test
    public void testBadConfigurationLoading() {
        String path = getClass().getClassLoader().getResource("it/polimi/se2018/utils/public_objective_cards/bad/bad_card.xml").toString();
        path = path.substring(5);

        try {
            XmlClientConfigLoader loader = new XmlClientConfigLoader(path);
            ClientConfiguration configuration = loader.loadConfiguration();

            assertNull(configuration);
        } catch (SAXException e) {
            Assert.assertEquals("", e.getMessage());
        }
    }

}
