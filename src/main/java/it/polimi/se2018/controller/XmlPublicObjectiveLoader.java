package it.polimi.se2018.controller;


import it.polimi.se2018.model.PublicObjectiveCard;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * The directory where public-objective-card xmls are stored.
     */
    private final File directory;

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
     *
     * @param directory  The directory that contains the xmls.
     * @param controller the controller the loader refers to.
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    XmlPublicObjectiveLoader(File directory, Controller controller) throws SAXException {

        this.controller = controller;

        if (!directory.isDirectory())
            throw new IllegalArgumentException("Directory required");
        if (directory.listFiles() == null)
            throw new IllegalArgumentException("Cannot list files");
        this.directory = directory;

        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(getClass().getResource("public_objective_cards/public_objective.xsd"));
        validator = schema.newValidator();

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            //fatal
            throw new RuntimeException("ParserConfigurationException: " + e.getMessage());
        }
    }

    /**
     * Tries to load {@code n} unique cards from the pattern directory.
     * <p>If there are not enough valid patterns, the size of the result array
     * is lower than {@code n}.</p>
     *
     * @param n The number of unique cards to load.
     * @return An array of size at most {@code n} containing a set of unique
     * cards.
     */
    public PublicObjectiveCard[] load(int n) {

        File[] listedFiles = directory.listFiles();
        if (listedFiles == null)
            throw new NullPointerException("The list of files is null");

        List<File> files = Arrays.stream(listedFiles)
                .filter(File::isFile)
                .collect(Collectors.toList());
        Collections.shuffle(files);


        List<PublicObjectiveCard> publicObjectiveCards = new ArrayList<>();

        for (int i = 0; i < files.size() && publicObjectiveCards.size() != n; ++i) {
            File file = files.get(i);
            if (isValid(file))
                publicObjectiveCards.add(loadCard(file));
        }
        return publicObjectiveCards.toArray(new PublicObjectiveCard[0]);
    }

    /**
     * Validates {@code file} against the xsd for public objective card definitions.
     *
     * @param file The file to be validated.
     * @return {@code true} if the file is a valid card description;
     * {@code false} otherwise.
     */
    private boolean isValid(File file) {
        try {
            validator.validate(new StreamSource(file));
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }

    /**
     * Loads a {@link PublicObjectiveCard} from an xml file.
     *
     * @param file The file where the card is stored.
     * @return A {@link PublicObjectiveCard} matching its description in xml.
     * If {@code null} is returned then some error while loading happened.
     */
    private PublicObjectiveCard loadCard(File file) {
        try {
            SaxPublicObjectiveBuilder saxPublicObjectiveBuilder = new SaxPublicObjectiveBuilder(controller);

            saxParser.parse(file, saxPublicObjectiveBuilder);
            return saxPublicObjectiveBuilder.build();

        } catch (SAXException | IOException ignored) {
            return null;
        }

    }
}
