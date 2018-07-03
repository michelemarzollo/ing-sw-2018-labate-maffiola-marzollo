package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.utils.GameUtils;
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
        Game game = new Game();
        Controller controller = new MultiPlayerController(game, 100, 100);
        String path = "/fake/fake/fake/";
        try {
            new XmlPublicObjectiveLoader(path, LIST_NAME, controller);
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
            Game game = new Game();
            Controller controller = new SinglePlayerController(game, 100);
            GameUtils.getCompleteSinglePlayerGame(game, controller);
            XmlPublicObjectiveLoader loader = new XmlPublicObjectiveLoader(listPath, LIST_NAME, controller);
            PublicObjectiveCard[] cards = loader.load(1);

            Assert.assertEquals(1, cards.length);
            Assert.assertEquals("Color Diagonals", cards[0].getName());
            Assert.assertEquals(1, cards[0].getVictoryPoints());

            //The first 3 classes, set in getCompleteSinglePlayerGame, are already in the controller.
            //the new one will be the third
            Assert.assertEquals(4, controller.getPublicScoreCalculators().size());
            Assert.assertTrue(controller.getPublicScoreCalculators().get(3) instanceof DiagonalScore);


        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * Tests if ill-formed patterns are ignored during the loading phase.
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
            Game game = new Game();
            Controller controller = new SinglePlayerController(game, 100);
            GameUtils.getCompleteSinglePlayerGame(game,controller);
            XmlPublicObjectiveLoader loader = new XmlPublicObjectiveLoader(listPath, LIST_NAME, controller);
            PublicObjectiveCard[] cards = loader.load(12);

            Assert.assertEquals(10, cards.length);

        } catch (SAXException e) {
            Assert.fail(e.getMessage());
        }
    }
}
