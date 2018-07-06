package it.polimi.se2018.controller;


import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.utils.ResourceManager;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * The class to load files from the directory 'resources/public_objective_cards'.
 * It's also a factory for {@link PublicObjectiveCard}s and the classes to calculate
 * the relative scores, since, the method {@code load} returns an array of
 * a given number of cards.
 *
 * @author michelemarzollo
 */

public class XmlPublicObjectiveLoader extends XmlCardLoader {

    /**
     * Default search path.
     */
    private static final String DEFAULT_PATH = "it/polimi/se2018/controller/public_objective_cards/";

    /**
     * Expected file name for card list.
     */
    private static final String LIST_NAME = "cards.list";

    /**
     * The error message to show if there were problems while parsing the file.
     */
    private static final String PARSING_ERROR_MESSAGE = "The file was validated, " +
            "but the parser couldn't create the PublicObjectiveElement";

    /**
     * Path where files are stored.
     */
    private final String basePath;

    /**
     * Creates a new {@code XmlPublicObjectiveLoader} that can load cards from
     * xmls contained in the specified directory.
     * <p>It also loads the xsd file which card definitions are checked against.</p>
     * <p>The default set of cards will be loaded.</p>
     *
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlPublicObjectiveLoader() throws SAXException {
        this(DEFAULT_PATH, LIST_NAME);
    }

    /**
     * Creates a new {@code XmlPublicObjectiveLoader} that can load cards from
     * xmls contained in the specified directory.
     * <p>It also loads the xsd file which card definitions are checked against.</p>
     *
     * @param basePath The directory that contains the xmls.
     * @param listName The name of the list file containing the names of the available files to load.
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlPublicObjectiveLoader(String basePath, String listName) throws SAXException {
        super();
        this.basePath = basePath;

        validator = getValidator(ResourceManager.getInstance().getPublicObjectiveSchema());

        filterFiles(basePath, listName);
    }

    /**
     * Tries to load {@code n} unique cards from the base directory.
     * <p>If there are not enough valid public objectives, the size of the result array
     * is lower than {@code n}.</p>
     *
     * @param n The number of unique cards to load.
     * @return An array of size at most {@code n} containing a set of unique
     * cards.
     */
    public PublicObjectiveElements load(int n) {

        Collections.shuffle(loadableCards);

        //The two arrays to be returned must have the dimension of the number
        //of instances required if there are enough files, or, otherwise, of the number of available files
        int numberOfCards = n < loadableCards.size() ? n : loadableCards.size();
        PublicObjectiveCard[] arrayOfCards = new PublicObjectiveCard[numberOfCards];
        PublicObjectiveScore[] scoreCalculators = new PublicObjectiveScore[numberOfCards];

        for (int i = 0; i < n && i < loadableCards.size(); i++) {
            PublicObjectiveElements cardAndScoreCalculator = loadCard(loadableCards.get(i));
            if (cardAndScoreCalculator != null) {
                arrayOfCards[i] = cardAndScoreCalculator.getCards()[0];
                scoreCalculators[i] = cardAndScoreCalculator.getScoreCalculators()[0];
            } else throw new CouldNotCreateObjectException(PARSING_ERROR_MESSAGE); //should never enter here
        }

        return new PublicObjectiveElements(arrayOfCards, scoreCalculators);
    }

    /**
     * Loads a {@link PublicObjectiveElements} from an xml file.
     *
     * @param resource The resource where the card is stored.
     * @return A {@link PublicObjectiveElements} matching its description in xml.
     * If {@code null} is returned then some error while loading happened.
     */
    private PublicObjectiveElements loadCard(String resource) {
        try {

            InputStream stream = ResourceManager.getInstance().getXmlStream(basePath, resource);
            SaxPublicObjectiveBuilder saxPublicObjectiveBuilder = new SaxPublicObjectiveBuilder();

            saxParser.parse(stream, saxPublicObjectiveBuilder);
            PublicObjectiveCard[] card = new PublicObjectiveCard[]{saxPublicObjectiveBuilder.buildCard()};
            PublicObjectiveScore[] scoreCalculator = new PublicObjectiveScore[]{saxPublicObjectiveBuilder.buildPublicScore()};

            return new PublicObjectiveElements(card, scoreCalculator);

        } catch (SAXException | IOException ignored) {
            return null;
        }
    }

    /**
     * The {@link RuntimeException} thrown when the file to load was validated,
     * but the parser couldn't create the object to construct from it.
     */
    public class CouldNotCreateObjectException extends RuntimeException {

        /**
         * The constructor.
         *
         * @param msg the message of the exception.
         */
        CouldNotCreateObjectException(String msg) {
            super(msg);
        }
    }
}
