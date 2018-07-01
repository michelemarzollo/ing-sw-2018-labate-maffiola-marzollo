package it.polimi.se2018.controller;

import it.polimi.se2018.utils.ResourceManager;
import it.polimi.se2018.utils.XmlLoader;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The class extends the {@link XmlLoader} adding functionalities linked
 * with the loading of a specific list of cards.
 * <p>To use this class for the loading of files, the files must be set
 * in a directory called 'xmls'.</p>
 *
 * @author michelemarzollo
 */
public abstract class XmlCardLoader extends XmlLoader {

    /**
     * List of loadable card files.
     */
    protected List<String> loadableCards = new ArrayList<>();

    /**
     * The constructor of the abstract class.
     *
     * @throws SAXException if sax validator or sax parser cannot be used.
     */
    public XmlCardLoader() throws SAXException {
        super();
    }

    /**
     * Populates the list of loadable cards.
     *
     * @throws IllegalArgumentException if the list file is unavailable.
     */
    protected void filterFiles(String basePath, String listName) {
        InputStream stream = ResourceManager.getInstance().getStream(basePath, listName);
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
}
