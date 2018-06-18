package it.polimi.se2018.controller;


import it.polimi.se2018.model.PublicObjectiveCard;
import it.polimi.se2018.utils.ResourceManager;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * The class to load files from the directory 'resources/public_objective_cards'.
 * It's also a factory for {@link PublicObjectiveCard}s and the classes to calculate
 * the relative scores, since, the method {@code load} returns an array of
 * a given number of cards.
 *
 * @author michelemarzollo
 */

public class XmlPublicObjectiveLoader {

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
     * List of loadable card files.
     */
    private List<String> loadableCards = new ArrayList<>();

    /**
     * The factory used to create sax parsers.
     */
    private final SAXParser saxParser;

    /**
     * The validator of the files.
     */
    private final Validator validator;

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
        this(DEFAULT_PATH, controller);
    }

    /**
     * Creates a new {@code XmlPublicObjectiveLoader} that can load cards from
     * xmls contained in the specified directory.
     * <p>It also loads the xsd file which card definitions are checked against.</p>
     *
     * @param basePath  The directory that contains the xmls.
     * @param controller the controller the loader refers to.
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlPublicObjectiveLoader(String basePath, Controller controller) throws SAXException {

        this.controller = controller;
        this.basePath = basePath;

        validator = getValidator();

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            //fatal
            throw new RuntimeException("ParserConfigurationException: " + e.getMessage());
        }
        filterFiles();
    }

    /**
     * Populates the list of loadable cards.
     *
     * @throws IllegalArgumentException if the list file is unavailable.
     */
    private void filterFiles() {
        InputStream stream = ResourceManager.getInstance().getStream(basePath, LIST_NAME);
        if (stream == null)
            throw new IllegalArgumentException("The given list file is unavailable.");
        try (Scanner fileNameScanner = new Scanner(stream)) {
            while (fileNameScanner.hasNext()) {
                String resource = "xmls/" + fileNameScanner.nextLine();
                InputStream resourceStream =
                        ResourceManager.getInstance().getXmlStream(basePath, resource);
                if (isValid(resourceStream))
                    loadableCards.add(resource);
            }
        }
    }

    /**
     * Validates {@code file} against the xsd for public objective card definitions.
     *
     * @param stream The resource to be validated.
     * @return {@code true} if the file is a valid card description;
     * {@code false} otherwise.
     */
    private boolean isValid(InputStream stream){
        if (stream == null)
            return false;
        try {
            validator.validate(new StreamSource(stream));
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }

    /**
     * Creates a validator object for public objective xml descriptions.
     *
     * @return A validator object for public objective xml descriptions.
     * @throws SAXException if the validator can't be instantiated.
     */
    private Validator getValidator() throws SAXException {
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        InputStream schemaStream = ResourceManager.getInstance().getPublicObjectiveSchema();
        Schema schema = schemaFactory.newSchema(new StreamSource(schemaStream));
        return schema.newValidator();
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
