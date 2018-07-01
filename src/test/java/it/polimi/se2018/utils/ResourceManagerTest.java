package it.polimi.se2018.utils;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;

/**
 * Unit tests for ResourceManager.
 */
public class ResourceManagerTest {

    /**
     * Tests if existing card images are loaded correctly.
     */
    @Test
    public void testGetCardImageUrlSuccess(){
        String url = ResourceManager.getInstance().getCardImageUrl("Color Diagonals");
        URL expectedUrl = getClass().getClassLoader()
                .getResource("it/polimi/se2018/view/gui/images/cards/ColorDiagonals.jpg");
        if(expectedUrl == null)
            Assert.fail("File not found");
        Assert.assertEquals(expectedUrl.toString(), url);
    }

    /**
     * Tests if unexisting card images are not loaded.
     */
    @Test
    public void testGetCardImageUrlFailure(){
        String url = ResourceManager.getInstance().getCardImageUrl("_AAAAAAAA");
        Assert.assertTrue(url.isEmpty());
    }

    /**
     * Tests if die images are loaded correctly.
     */
    @Test
    public void testGetDieImageUrl(){
        Die die = new Die(1, new Random(), Colour.BLUE);
        String url = ResourceManager.getInstance().getDieImageUrl(die);
        URL expectedUrl = getClass().getClassLoader()
                .getResource("it/polimi/se2018/view/gui/images/dice/Blue1.jpg");
        if(expectedUrl == null)
            Assert.fail("File not found");
        Assert.assertEquals(expectedUrl.toString(), url);
    }

    /**
     * Tests if cell images with only value restriction are loaded correctly.
     */
    @Test
    public void testGetCellImageUrlOnlyValue(){
        Cell cell = new Cell(1);
        String url = ResourceManager.getInstance().getCellImageUrl(cell);
        URL expectedUrl = getClass().getClassLoader()
                .getResource("it/polimi/se2018/view/gui/images/dice/Gray1.jpg");
        if(expectedUrl == null)
            Assert.fail("File not found");
        Assert.assertEquals(expectedUrl.toString(), url);
    }

    /**
     * Tests if cell images with only colour restriction are loaded correctly.
     */
    @Test
    public void testGetCellImageUrlOnlyColour(){
        Cell cell = new Cell(Colour.BLUE);
        String url = ResourceManager.getInstance().getCellImageUrl(cell);
        URL expectedUrl = getClass().getClassLoader()
                .getResource("it/polimi/se2018/view/gui/images/dice/BlueRestriction.jpg");
        if(expectedUrl == null)
            Assert.fail("File not found");
        Assert.assertEquals(expectedUrl.toString(), url);
    }

    /**
     * Tests if cell images with no restrictions are loaded correctly.
     */
    @Test
    public void testGetCellImageUrlNoRestrictions(){
        Cell cell = new Cell();
        String url = ResourceManager.getInstance().getCellImageUrl(cell);
        Assert.assertTrue(url.isEmpty());
    }

    /**
     * Tests if pattern xsd file is located and opened correctly.
     */
    @Test
    public void testGetPatternSchema(){
        InputStream schema = ResourceManager.getInstance().getPatternSchema();
        Assert.assertNotNull(schema);
    }

    /**
     * Tests if public objective xsd file is located and opened correctly.
     */
    @Test
    public void testGetPublicObjectiveSchema(){
        InputStream schema = ResourceManager.getInstance().getPublicObjectiveSchema();
        Assert.assertNotNull(schema);
    }

    /**
     * Tests if tool card xsd file is located and opened correctly.
     */
    @Test
    public void testGetToolCardSchema(){
        InputStream schema = ResourceManager.getInstance().getToolCardSchema();
        Assert.assertNotNull(schema);
    }

    /**
     * Tests if client configuration xsd file is located and opened correctly.
     */
    @Test
    public void testGetClientConfigurationSchema(){
        InputStream schema = ResourceManager.getInstance().getClientConfigurationSchema();
        Assert.assertNotNull(schema);
    }

    /**
     * Tests if server configuration xsd file is located and opened correctly.
     */
    @Test
    public void testGetServerConfigurationSchema(){
        InputStream schema = ResourceManager.getInstance().getServerConfigurationSchema();
        Assert.assertNotNull(schema);
    }

    /**
     * Tests if an existing file is located and opened correctly.
     */
    @Test
    public void testGetStreamSuccess(){
        InputStream schema = ResourceManager.getInstance()
                .getStream("it/polimi/se2018/controller/public_objective_cards/",
                        "cards.list");
        Assert.assertNotNull(schema);
    }

    /**
     * Tests if an unexisting file is not opened.
     */
    @Test
    public void testGetStreamFailure(){
        InputStream schema = ResourceManager.getInstance()
                .getStream("it/polimi/se2018/",
                        "_AAAAAAAAAA");
        Assert.assertNull(schema);
    }

    /**
     * Tests if an existing xml file is located and opened correctly.
     */
    @Test
    public void testGetXmlStreamSuccess(){
        InputStream schema = ResourceManager.getInstance()
                .getXmlStream("it/polimi/se2018/model/patterns/xmls/",
                        "aurora_sagradis.xml");
        Assert.assertNotNull(schema);
    }

}
