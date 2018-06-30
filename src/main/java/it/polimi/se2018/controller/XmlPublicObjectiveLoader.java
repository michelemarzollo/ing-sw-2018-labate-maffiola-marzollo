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
     * Path where files are stored.
     */
    private final String basePath;

    /**
     * The controller the loader refers to.
     */
    private Controller controller;

    /**
     * Creates a new {@code XmlPublicObjectiveLoader} that can load cards from
     * xmls contained in the specified directory.
     * <p>It also loads the xsd file which card definitions are checked against.</p>
     * <p>The default set of cards will be loaded.</p>
     *
     * @param controller the controller the loader refers to.
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlPublicObjectiveLoader(Controller controller) throws SAXException {
        this(DEFAULT_PATH, LIST_NAME, controller);
    }

    /**
     * Creates a new {@code XmlPublicObjectiveLoader} that can load cards from
     * xmls contained in the specified directory.
     * <p>It also loads the xsd file which card definitions are checked against.</p>
     *
     * @param basePath   The directory that contains the xmls.
     * @param listName   The name of the list file containing the names of the available files to load.
     * @param controller The controller the loader refers to.
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlPublicObjectiveLoader(String basePath, String listName, Controller controller) throws SAXException {
        super();
        this.controller = controller;
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
    public PublicObjectiveCard[] load(int n) {
        Collections.shuffle(loadableCards);

        return loadableCards.stream()
                .limit(n)
                .map(this::loadCard)
                .toArray(PublicObjectiveCard[]::new);
    }

    /**
     * Loads a {@link PublicObjectiveCard} from an xml file.
     *
     * @param resource The resource where the card is stored.
     * @return A {@link PublicObjectiveCard} matching its description in xml.
     * If {@code null} is returned then some error while loading happened.
     */
    private PublicObjectiveCard loadCard(String resource) {
        try {

            InputStream stream = ResourceManager.getInstance().getXmlStream(basePath, resource);
            SaxPublicObjectiveBuilder saxPublicObjectiveBuilder = new SaxPublicObjectiveBuilder(controller);

            saxParser.parse(stream, saxPublicObjectiveBuilder);
            return saxPublicObjectiveBuilder.build();

        } catch (SAXException | IOException ignored) {
            return null;
        }
    }
}
