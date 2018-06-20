package it.polimi.se2018.utils;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;

import java.io.InputStream;
import java.net.URL;

/**
 * This class is used to retrieve information from application resources.
 * <p>ResourceManager is a singleton.</p>
 * @author dvdmff
 */
public class ResourceManager {

    /**
     * The only instance of the class.
     */
    private static ResourceManager instance;

    /**
     * Base url.
     */
    private static final String BASE = "it/polimi/se2018/";

    /**
     * Returns an instance to the only instantiable object.
     * @return A reference to the class instance.
     */
    public static ResourceManager getInstance() {
        if (instance == null)
            instance = new ResourceManager();
        return instance;
    }

    /**
     * Retrieves the url of a card image.
     * @param cardName The name of the card to load.
     * @return The url of a card image.
     */
    public String getCardImageUrl(String cardName) {
        String fileName = cardName.replaceAll("\\s+", "") + ".jpg";
        URL url = getClass().getClassLoader().getResource(BASE + "view/gui/images/cards/" + fileName);
        if (url == null)
            return "";
        return url.toString();
    }

    /**
     * Retrieves the url of a die representation.
     * @param die The die to represent.
     * @return The url of the die.
     */
    public String getDieImageUrl(Die die) {
        String fileName = die.getColour().toString() + die.getValue() + ".jpg";
        URL url = getClass().getClassLoader().getResource(BASE + "view/gui/images/dice/" + fileName);
        if (url == null)
            return "";
        return url.toString();
    }

    /**
     * Retrieves the url of a cell representation.
     * @param cell The cell to represent.
     * @return The url of a cell, or empty string if there is none.
     */
    public String getCellImageUrl(Cell cell) {
        String fileName;
        if (cell.getColour() == null && cell.getValue() != 0)
            fileName = "Gray" + cell.getValue() + ".jpg";
        else if (cell.getValue() == 0 && cell.getColour() != null)
            fileName = cell.getColour() + "Restriction.jpg";
        else
            return "";

        URL url = getClass().getClassLoader().getResource(BASE + "view/gui/images/dice/" + fileName);
        if (url == null)
            return "";
        return url.toString();
    }

    /**
     * Retrieves the {@link InputStream} of the pattern xsd.
     * @return The {@link InputStream} of the pattern xsd.
     */
    public InputStream getPatternSchema() {
        return getClass().getClassLoader().getResourceAsStream(BASE + "model/patterns/pattern.xsd");
    }

    /**
     * Retrieves the {@link InputStream} of the public objective cards xsd.
     * @return The {@link InputStream} of the public objective cards xsd.
     */
    public InputStream getPublicObjectiveSchema() {
        return getClass().getClassLoader().getResourceAsStream(BASE + "controller/public_objective_cards/public_objective.xsd");
    }

    /**
     * Retrieves the {@link InputStream} of the client configuration xsd.
     * @return The {@link InputStream} of the client configuration xsd.
     */
    public InputStream getClientConfigurationSchema() {
        return getClass().getClassLoader().getResourceAsStream(BASE + "utils/client_specification.xsd");
    }

    /**
     * Retrieves the {@link InputStream} of the client configuration xsd.
     * @return The {@link InputStream} of the client configuration xsd.
     */
    public InputStream getServerConfigurationSchema() {
        return getClass().getClassLoader().getResourceAsStream(BASE + "utils/server_specification.xsd");
    }

    /**
     * Returns a stream for the given file.
     * @param base The directory where the file is located.
     * @param fileName The name of the file.
     * @return The stream for the given file.
     */
    public InputStream getStream(String base, String fileName){
        return getClass().getClassLoader().getResourceAsStream(base + fileName);
    }

    /**
     * Returns a stream for the given xml file.
     * @param base The directory where the file is located.
     * @param name The name of the file.
     * @return The stream for the given file.
     */
    public InputStream getXmlStream(String base, String name){
        String fileName = name;
        if (!fileName.endsWith(".xml"))
            fileName = name + ".xml";

        return getStream(base, fileName);
    }

}
