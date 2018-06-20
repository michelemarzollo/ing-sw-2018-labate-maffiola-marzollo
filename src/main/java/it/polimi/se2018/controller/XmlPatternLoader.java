package it.polimi.se2018.controller;

import it.polimi.se2018.model.Pattern;
import it.polimi.se2018.utils.ResourceManager;
import it.polimi.se2018.utils.XmlLoader;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This class allows to load pattern cards from xml files contained in
 * a directory.
 *
 * <p>All pattern xml descriptions must respect the schema located in the pattern
 * directory in the model package.</p>
 *
 * @author dvdmff
 */
public class XmlPatternLoader extends XmlLoader implements PatternLoader{

    private static final String PATTERNS_DIR = "it/polimi/se2018/model/patterns/";

    /**
     * The list of pattern xml descriptions suitable for loading.
     */
    private final List<String> loadablePatterns = new ArrayList<>();

    /**
     * The directory where patterns are stored.
     */
    private final String basePath;

    /**
     * Creates a new {@code XmlPatternLoader} that can load patterns from
     * xml files.
     * <p>The loaded pattern are the default ones.</p>
     *
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlPatternLoader() throws SAXException {
        this(PATTERNS_DIR, "patterns.list");
    }

    /**
     * Creates a new {@code XmlPatternLoader} that loads patterns from the specified base directory.
     * <p>The files available are listed in the file named {@code listName}.</p>
     *
     * @param basePath The directory where the list and files are stored.
     * @param listName The name of the list file containing the names of the available files to load.
     * @throws SAXException if sax validator or sax parser cannot be used.
     */
    public XmlPatternLoader(String basePath, String listName) throws SAXException {
        super();
        validator = getValidator(ResourceManager.getInstance().getPatternSchema());

        this.basePath = basePath;
        filterFiles(listName);
    }

    /**
     * Populates the list of loadable patterns.
     *
     * @param listName The list containing loadable files.
     * @throws IllegalArgumentException if the list file is unavailable.
     */
    private void filterFiles(String listName) {
        InputStream stream = ResourceManager.getInstance().getStream(basePath, listName);
        if (stream == null)
            throw new IllegalArgumentException("The given list file is unavailable.");
        try (Scanner fileNameScanner = new Scanner(stream)) {
            while (fileNameScanner.hasNext()) {
                String resource = fileNameScanner.nextLine();
                InputStream resourceStream =
                        ResourceManager.getInstance().getXmlStream(basePath, resource);
                if (isValid(resourceStream))
                    loadablePatterns.add(resource);
            }
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
        Collections.shuffle(loadablePatterns);

        return loadablePatterns.stream()
                .limit(n)
                .map(this::loadPattern)
                .toArray(Pattern[]::new);
    }

    /**
     * Loads a pattern from an xml file.
     *
     * @param resource The name of the resource where the pattern is described.
     * @return A pattern matching its description in xml. If {@code null}
     * is returned then some error while loading happened.
     */
    private Pattern loadPattern(String resource) {
        try {

            InputStream inputStream =
                    ResourceManager.getInstance().getXmlStream(basePath, resource);
            SaxPatternBuilder saxPatternBuilder = new SaxPatternBuilder();

            saxParser.parse(inputStream, saxPatternBuilder);
            return saxPatternBuilder.build();

        } catch (SAXException | IOException ignored) {
            return null;
        }

    }
}
