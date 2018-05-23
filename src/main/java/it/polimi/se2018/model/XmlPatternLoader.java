package it.polimi.se2018.model;

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
 * This class allows to load pattern cards from xml files contained in
 * a directory.
 *
 * @author dvdmff
 */
public class XmlPatternLoader implements PatternLoader {

    /**
     * The directory where pattern xmls are stored.
     */
    private final File directory;

    /**
     * The factory used to create sax parsers.
     */
    private final SAXParser saxParser;

    private final Validator validator;

    /**
     * Creates a new {@code XmlPatternLoader} that can load patterns from
     * xmls contained in the specified directory.
     * <p>It also loads the xsd file which pattern definitions are checked against.</p>
     *
     * @param directory The directory that contains the xmls.
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlPatternLoader(File directory) throws SAXException {
        if (!directory.isDirectory())
            throw new IllegalArgumentException("Directory required");
        if (directory.listFiles() == null)
            throw new IllegalArgumentException("Cannot list files");
        this.directory = directory;

        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(getClass().getResource("pattern.xsd"));
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
     * Tries to load {@code n} unique patterns from the pattern directory.
     * <p>If there are not enough valid patterns, the size of the result array
     * is lower tha {@code n}.</p>
     *
     * @param n The number of unique patterns to load.
     * @return An array of size at most {@code n} containing a set of unique
     * pattern cards.
     */
    @Override
    public Pattern[] load(int n) {

        File[] listedFiles = directory.listFiles();
        if(listedFiles == null)
            throw new NullPointerException("The list of files is null");

        List<File> files = Arrays.stream(listedFiles)
                .filter(File::isFile)
                .collect(Collectors.toList());
        Collections.shuffle(files);


        List<Pattern> patterns = new ArrayList<>();

        for (int i = 0;
             i < files.size() && patterns.size() != n;
             ++i) {
            File file = files.get(i);
            if (isValid(file))
                patterns.add(loadPattern(file));
        }
        return patterns.toArray(new Pattern[0]);
    }

    /**
     * Validates {@code file} against the xsd for pattern definitions.
     *
     * @param file The file to be validated.
     * @return {@code true} if the file is a valid pattern description;
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
     * Loads a pattern from an xml file.
     *
     * @param file The file where the card is stored.
     * @return A pattern matching its description in xml. If {@code null}
     * is returned then some error while loading happened.
     */
    private Pattern loadPattern(File file) {
        try {
            SaxPatternBuilder saxPatternBuilder = new SaxPatternBuilder();

            saxParser.parse(file, saxPatternBuilder);
            return saxPatternBuilder.build();

        } catch (SAXException | IOException ignored) {
            return null;
        }

    }
}
