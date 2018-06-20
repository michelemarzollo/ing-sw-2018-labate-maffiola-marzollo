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
 * The abstract class that contains the common methods for the classes
 * that load xml files.
 *
 * @author michelamarzollo
 */
public abstract class XmlLoader {

    /**
     * The factory used to create sax parsers.
     */
    protected final SAXParser saxParser;

    /**
     * The validator of the files.
     */
    protected Validator validator;

    /**
     * The constructor of th abstract class.
     *
     * @throws SAXException if sax validator or sax parser cannot be used.
     */
    public XmlLoader() throws SAXException {

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            //fatal
            throw new ParserException("ParserConfigurationException: " + e.getMessage());
        }
    }

    /**
     * Creates a validator object for xml descriptions.
     *
     * @param schemaStream the stream of the xsd file used to validate the xml files.
     * @return A validator object for xml descriptions.
     * @throws SAXException if the validator can't be instantiated.
     */
    protected Validator getValidator(InputStream schemaStream) throws SAXException {
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = schemaFactory.newSchema(new StreamSource(schemaStream));
        return schema.newValidator();
    }

    /**
     * Validates {@code file} against the xsd given in {@code getValidator()}.
     *
     * @param stream The file to be validated.
     * @return {@code true} if the file a valid against the xsd description;
     * {@code false} otherwise.
     */
    protected boolean isValid(InputStream stream) {
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
     * The {@link RuntimeException} thrown when a {@link SAXParser} can't
     * be instantiated.
     */
    public class ParserException extends RuntimeException {

        /**
         * The constructor.
         *
         * @param msg the message of the exception.
         */
        ParserException(String msg) {
            super(msg);
        }
    }
}
