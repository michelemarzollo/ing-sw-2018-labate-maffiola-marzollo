package it.polimi.se2018.utils;

import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * The class to create a {@link ClientConfiguration}, from the information
 * contained in a file.
 *
 * @author michelemarzollo
 */
public class XmlClientConfigLoader extends XmlLoader {

    /**
     * Path where the file is stored.
     */
    private final String path;

    /**
     * The constructor of the class.
     *
     * @param path the path where to find the xml file.
     * @throws SAXException it there was any sax error in parsing.
     */
    public XmlClientConfigLoader(String path) throws SAXException {
        super();
        validator = getValidator(ResourceManager.getInstance().getClientConfigurationSchema());
        this.path = path;
    }

    /**
     * Loads a {@link ClientConfiguration} from an xml file.
     *
     * @return A client configuration matching its description in xml. If {@code null}
     * is returned then some error while loading happened.
     */
    public ClientConfiguration loadConfiguration() {
        try {

            try (FileInputStream inputStream = new FileInputStream(path)) {
                SaxClientConfigurationParser clientConfigurationParser = new SaxClientConfigurationParser();

                if (isValid(inputStream)) {
                    //I create again the strem because it was destroyed
                    try (FileInputStream stream = new FileInputStream(path)) {
                        saxParser.parse(stream, clientConfigurationParser);
                        return tryGetConfigurationInstance();
                    }
                }
                //should never enter here
                else return null;
            }

        } catch (SAXException | IOException ignored) {
            return null;
        }
    }

    /**
     * Helper method for {@code loadConfiguration()}: it tries to get the instance of
     * {@link ClientConfiguration}. It should never enter in the catch branch, beacause
     * at this point the ClientConfiguration should have been instantiated.
     *
     * @return the instance of {@link ClientConfiguration}.
     */
    private ClientConfiguration tryGetConfigurationInstance() {
        try {
            return ClientConfiguration.getInstance();
        } catch (MissingConfigurationException e) {
            Logger.getDefaultLogger().log("The ClientConfiguration class wasn't instantiated: " + e.getMessage());
            return null;
        }
    }
}
