package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.ToolCard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link XmlToolCardLoader}.
 *
 * @author michelemarzollo
 */
public class XmlToolCardLoaderTest {

    @Before
    public void setUp() {
        ToolCardFactory.reset();
    }

    /**
     * Tests the correct creation of the {@link ToolCardFactory}.
     */
    @Test
    public void testCreateToolCardFactory() {
        try {
            XmlToolCardLoader xmlToolCardLoader = new XmlToolCardLoader();
            xmlToolCardLoader.createToolCardFactory();
            assertTrue(ToolCardFactory.getInstance() != null);
        } catch (SAXException e) {
            Assert.fail();
        }
    }

    /**
     * Tests that the information read from the file is correct.
     */
    @Test
    public void testAddListElements() {
        try {
            String basePath = "it/polimi/se2018/utils/tool_cards/";
            String listName = "cards.list";

            XmlToolCardLoader xmlToolCardLoader = new XmlToolCardLoader(basePath, listName);
            xmlToolCardLoader.createToolCardFactory();
            assertTrue(ToolCardFactory.getInstance() != null);
            ToolCard[] toolCards = ToolCardFactory.getInstance().newInstances(1);
            assertEquals("Fake Card", toolCards[0].getName());
            assertEquals("NO DESCRIPTION", toolCards[0].getDescription());
            assertEquals(Colour.YELLOW, toolCards[0].getColour());
        } catch (SAXException e) {
            Assert.fail();
        }
    }

    /**
     * Tests the case in which the xml tried to be loaded doesn't respect the xsd specification.
     */
    @Test
    public void testNegativeLoading() {
        try {
            String listPath = "it/polimi/se2018/utils/public_objective_cards/bad/";
            String listName = "cards.list";

            XmlToolCardLoader xmlToolCardLoader = new XmlToolCardLoader(listPath, listName);
            xmlToolCardLoader.createToolCardFactory();
            assertTrue(ToolCardFactory.getInstance() != null);
            try {
                ToolCard[] toolCards = ToolCardFactory.getInstance().newInstances(1);
                Assert.fail();
            } catch (IllegalArgumentException ignored) {

            }
        } catch (SAXException e) {
            Assert.fail();
        }
    }
}
