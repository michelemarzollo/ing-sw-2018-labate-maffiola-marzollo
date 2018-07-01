package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.ResourceManager;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that loads all {@link ToolCard}s and creates the {@link ToolCardFactory}.
 *
 * @author michelemarzollo
 */
public class XmlToolCardLoader extends XmlCardLoader {

    /**
     * Default search path.
     */
    private static final String DEFAULT_PATH = "it/polimi/se2018/controller/tool_cards/";

    /**
     * Expected file name for card list.
     */
    private static final String LIST_NAME = "cards.list";

    /**
     * The default path where to find the
     */
    private final String basePath;

    /**
     * The list that contains the names of the tool cards.
     */
    private List<String> names = new ArrayList<>();

    /**
     * The list that contains the descriptions of the tool cards.
     */
    private List<String> descriptions = new ArrayList<>();

    /**
     * The list that contains the colours of the tool cards.
     */
    private List<Colour> colours = new ArrayList<>();

    /**
     * Creates a new {@code XmlToolCardLoader} that can load card information from
     * xmls contained in the specified directory.
     * <p>It also loads the xsd file which card definitions are checked against.</p>
     *
     * @throws IllegalArgumentException if the specified directory isn't actually
     *                                  a directory or it isn't readable.
     * @throws SAXException             if sax validator or sax parser cannot be used.
     */
    public XmlToolCardLoader() throws SAXException {
        this(DEFAULT_PATH, LIST_NAME);
    }

    /**
     * The constructor of the class.
     *
     * @param basePath The directory that contains the xmls.
     * @param listName The name of the list file containing the names of the available files to load.
     * @throws SAXException if sax validator or sax parser cannot be used.
     */
    public XmlToolCardLoader(String basePath, String listName) throws SAXException {
        super();
        this.basePath = basePath;

        validator = getValidator(ResourceManager.getInstance().getToolCardSchema());

        filterFiles(basePath, listName);
    }

    /**
     * The method to set the lists that will be used to create the {@link ToolCardFactory}.
     * <p>It loads the information of tool cards that are in in the file 'cards.list'.</p>
     */
    public void createToolCardFactory() {
        for (String fileName : loadableCards) {
            loadCard(fileName);
        }
        ToolCardFactory.makeInstance(names, descriptions, colours);
    }

    /**
     * Loads the information of a {@link ToolCard} from an xml file.
     *
     * @param resource The resource where the card is stored.
     */
    private void loadCard(String resource) {
        try {

            InputStream stream = ResourceManager.getInstance().getXmlStream(basePath, resource);
            SaxToolCardParser saxToolCardParser = new SaxToolCardParser(this);

            saxParser.parse(stream, saxToolCardParser);

        } catch (SAXException | IOException ignored) {
            Logger.getDefaultLogger().log(ignored.getMessage());
        }
    }

    /**
     * The method that is invoked be the {@link SaxToolCardParser} to add the information
     * of a card to the three lists.
     *
     * @param name        the name of the card.
     * @param description the description of the card.
     * @param colour      the colour of the card.
     */
    public void addListElements(String name, String description, Colour colour) {
        names.add(name);
        descriptions.add(description);
        colours.add(colour);
    }
}
