package it.polimi.se2018.model;

import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Unit tests for XmlPatternLoader.
 */
public class XmlPatternLoaderTest {

    /**
     * Tests if an invalid directory causes the constructor to throw an
     * IllegalArgumentException.
     */
    @Test
    public void testBadDirectoryConstructorFailure() {
        File directory = new File("/fake/fake/fake/");
        try {
            new XmlPatternLoader(directory);
            Assert.fail("Should not create object");
        } catch (SAXException e) {
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

    }

    /**
     * Tests if a well-formed xml pattern description is correctly loaded.
     */
    @Test
    public void testPatternLoading() {
        File directory = null;
        try {
            directory = new File(getClass()
                    .getResource("XmlPatternLoaderTest/sun_catcher")
                    .toURI()
            );
        } catch (URISyntaxException e) {
            Assert.fail(e.getMessage());
        }

        Cell[][] expectedGrid =
                GridUtils.getEmptyUnrestrictedGrid(Pattern.ROWS, Pattern.COLS);

        expectedGrid[2][0] = new Cell(2);
        expectedGrid[0][0] = new Cell(Colour.BLUE);

        try {
            XmlPatternLoader loader = new XmlPatternLoader(directory);
            Pattern pattern = loader.load(1)[0];

            Assert.assertEquals("Duomo", pattern.getName());
            Assert.assertEquals(4, pattern.getDifficulty());

            Cell[][] realGrid = pattern.getGrid();
            Assert.assertTrue(GridUtils.haveSameRestriction(expectedGrid, realGrid));
            Assert.assertTrue(GridUtils.isEmpty(realGrid));

        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tests if ill-formed patterns are ignored during the loading phase.
     */
    @Test
    public void testBadPatternLoading() {
        File directory = null;
        try {
            directory = new File(
                    getClass()
                            .getResource("XmlPatternLoaderTest/bad")
                            .toURI()
            );
        } catch (URISyntaxException e) {
            Assert.fail(e.getMessage());
        }

        try {
            XmlPatternLoader loader = new XmlPatternLoader(directory);
            loader.load(1);
        } catch (SAXException e) {
            Assert.assertEquals("", e.getMessage());
        }
    }

    /**
     * Tests that when more patterns than available are requested, the actual
     * number of loaded patterns is the number of available patterns.
     */
    @Test
    public void testRequestTooManyPattern(){
        File directory = null;
        try {
            directory = new File(getClass()
                    .getResource("XmlPatternLoaderTest/sun_catcher")
                    .toURI()
            );
        } catch (URISyntaxException e) {
            Assert.fail(e.getMessage());
        }

        try {
            XmlPatternLoader loader = new XmlPatternLoader(directory);
            Pattern[] patterns = loader.load(5);

            Assert.assertEquals(1, patterns.length);

        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }
}
