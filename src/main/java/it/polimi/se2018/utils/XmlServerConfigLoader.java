package it.polimi.se2018.utils;

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

/**
 * The class to create a {@link ServerConfiguration}, from the information
 * contained in a file.
 *
 * @author michelemarzollo
 */
public class XmlServerConfigLoader {

    /**
     * Expected file name for the client configuration.
     */
    private static final String FILE_NAME = "server_configuration.xml";

    /**
     * Path where the file is stored.
     */
    private final String basePath;

    /**
     * The factory used to create sax parsers.
     */
    private final SAXParser saxParser;

    /**
     * The validator of the files.
     */
    private final Validator validator;


    /**
     * The constructor of the class.
     *
     * @param basePath the path where to find the xml file.
     * @throws SAXException it there was any sax error in parsing.
     */
    public XmlServerConfigLoader(String basePath) throws SAXException {
        this.basePath = basePath;

        validator = getValidator();

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            //fatal
            throw new RuntimeException("ParserConfigurationException: " + e.getMessage());
        }
    }

    /**
     * Creates a validator object for server configuration xml descriptions.
     *
     * @return A validator object for server configuration xml descriptions.
     * @throws SAXException if the validator can't be instantiated.
     */
    private Validator getValidator() throws SAXException {
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        InputStream schemaStream = ResourceManager.getInstance().getServerConfigurationSchema();
        Schema schema = schemaFactory.newSchema(new StreamSource(schemaStream));
        return schema.newValidator();
    }

    /**
     * Validates {@code file} against the xsd for server configuration definitions.
     *
     * @param stream The file to be validated.
     * @return {@code true} if the file is a valid {@link ServerConfiguration} description;
     * {@code false} otherwise.
     */
    private boolean isValid(InputStream stream) {
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
     * Loads a {@link ServerConfiguration} from an xml file.
     *
     * @return A {@link ServerConfiguration} matching its description in xml. If {@code null}
     * is returned then some error while loading happened.
     */
    public ServerConfiguration loadConfiguration() {
        try {

            InputStream inputStream =
                    ResourceManager.getInstance().getXmlStream(basePath, FILE_NAME);
            SaxServerConfigurationParser serverConfigurationParser = new SaxServerConfigurationParser();

            if (isValid(inputStream)) {
                saxParser.parse(inputStream, serverConfigurationParser);
                return serverConfigurationParser.getServerConfiguration();
            }
            //should never enter here
            else return null;

        } catch (SAXException | IOException ignored) {
            return null;
        }

    }
}
