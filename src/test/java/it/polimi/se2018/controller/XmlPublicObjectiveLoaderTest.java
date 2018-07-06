package it.polimi.se2018.controller;

import it.polimi.se2018.model.PublicObjectiveCard;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Test for {@link XmlPublicObjectiveLoader}.
 *
 * @author michelemarzollo
 */
public class XmlPublicObjectiveLoaderTest {

    /**
     * Expected file name for card list.
     */
    private static final String LIST_NAME = "cards.list";

    /**
     * Tests if an invalid directory causes the constructor to throw an
     * IllegalArgumentException.
     */
    @Test
    public void testBadDirectoryConstructorFailure() {
        String path = "/fake/fake/fake/";
        try {
            new XmlPublicObjectiveLoader(path, LIST_NAME);
            Assert.fail("Should not create object");
        } catch (SAXException e) {
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

    }

    /**
     * Tests if a well-formed xml card description is correctly loaded.
     */
    @Test
    public void testCardLoading() {
        String listPath = "it/polimi/se2018/utils/public_objective_cards/";

        try {
            XmlPublicObjectiveLoader loader = new XmlPublicObjectiveLoader(listPath, LIST_NAME);
            PublicObjectiveElements publicObjectiveElements = loader.load(1);
            PublicObjectiveCard[] cards = publicObjectiveElements.getCards();
            PublicObjectiveScore[] scoreCalculators = publicObjectiveElements.getScoreCalculators();

            Assert.assertEquals(1, cards.length);
            Assert.assertEquals("Color Diagonals", cards[0].getName());
            Assert.assertEquals(1, cards[0].getVictoryPoints());

            Assert.assertEquals(1, scoreCalculators.length);
            Assert.assertTrue(scoreCalculators[0] instanceof DiagonalScore);


        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tests if ill-formed xml card descriptions are ignored during the loading phase.
     */
    @Test
    public void testBadCardLoading() {
        String listPath = "it/polimi/se2018/utils/public_objective_cards/bad/";

        try {
            XmlPatternLoader loader = new XmlPatternLoader(listPath, "cards.list");
            loader.load(1);
        } catch (SAXException e) {
            Assert.assertEquals("", e.getMessage());
        }
    }

    /**
     * Tests that when more cards than available are requested, the actual
     * number of loaded cards is the number of available patterns.
     */
    @Test
    public void testRequestTooManyCards() {
        String listPath = "it/polimi/se2018/controller/public_objective_cards/";

        try {
            XmlPublicObjectiveLoader loader = new XmlPublicObjectiveLoader(listPath, LIST_NAME);
            PublicObjectiveElements publicObjectiveElements = loader.load(12);
            PublicObjectiveCard[] cards = publicObjectiveElements.getCards();
            PublicObjectiveScore[] scoreCalculators = publicObjectiveElements.getScoreCalculators();

            Assert.assertEquals(10, cards.length);
            Assert.assertEquals(10, scoreCalculators.length);

        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }
}
