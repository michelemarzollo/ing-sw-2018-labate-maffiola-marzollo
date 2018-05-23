package it.polimi.se2018.model;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is a sax handler that parses a pattern from its xml description.
 * @author dvdmff
 */
public class SaxPatternBuilder extends DefaultHandler {

    private boolean inName;
    private boolean inDifficulty;
    private boolean inGrid;
    private boolean inCell;
    private boolean inColourRestriction;
    private boolean inValueRestriction;

    private int row;
    private int col;

    private Cell[][] grid;
    private String name;
    private int difficulty;

    /**
     * Creates a new pattern using the parsed data.
     * @return The pattern described in the xml.
     */
    public Pattern build() {
        return new Pattern(name, difficulty, grid);
    }

    /**
     * Initializes all attributes to their default value to correctly parse
     * the xml file.
     */
    @Override
    public void startDocument() {
        inName = false;
        inColourRestriction = false;
        inDifficulty = false;
        inValueRestriction = false;
        inGrid = false;
        inCell = false;

        row = -1;
        col = -1;

        name = "";
        difficulty = 0;
        grid = new Cell[4][5];
    }

    /**
     * Handles the event that occurs when a new element is opened.
     *
     * @param uri           The namespace uri of the element.
     * @param localName     The local name of the element.
     * @param qualifiedName The qualified name of the element.
     * @param attributes    The set of attributes of the element.
     */
    @Override
    public void startElement(String uri, String localName,
                             String qualifiedName, Attributes attributes) {

        if (qualifiedName.equals("name"))
            inName = true;
        if (qualifiedName.equals("difficulty"))
            inDifficulty = true;
        if (qualifiedName.equals("grid"))
            inGrid = true;
        if (qualifiedName.equals("cell")) {
            inCell = true;
            row = Integer.parseInt(attributes.getValue("row")) - 1;
            col = Integer.parseInt(attributes.getValue("col")) - 1;
        }
        if (qualifiedName.equals("valueRestriction"))
            inValueRestriction = true;

        if (qualifiedName.equals("colourRestriction"))
            inColourRestriction = true;
    }

    /**
     * Handles the event that occurs when a new element is closed.
     *
     * @param uri           The namespace uri of the element.
     * @param localName     The local name of the element.
     * @param qualifiedName The qualified name of the element.
     */
    @Override
    public void endElement(String uri, String localName, String qualifiedName) {

        if (qualifiedName.equals("name"))
            inName = false;
        if (qualifiedName.equals("difficulty"))
            inDifficulty = false;
        if (qualifiedName.equals("grid"))
            inGrid = false;
        if (qualifiedName.equals("cell")) {
            inCell = false;
            row = -1;
            col = -1;
        }
        if (qualifiedName.equals("valueRestriction"))
            inValueRestriction = false;

        if (qualifiedName.equals("colourRestriction"))
            inColourRestriction = false;
    }

    /**
     * Handles the event that occurs when a text is encountered.
     * <p>Depending on the current position in the file, sets the name,
     * difficulty and cell restrictions of the pattern.</p>
     */
    @Override
    public void characters(char[] chars, int start, int length) {
        String string = new String(chars, start, length);
        if (inName)
            name = string;
        if (inDifficulty)
            difficulty = Integer.parseInt(string);
        if (inGrid && inCell) {
            if (inColourRestriction)
                grid[row][col] = new Cell(Colour.valueOf(string));
            else if (inValueRestriction)
                grid[row][col] = new Cell(Integer.parseInt(string));

        }
    }

    /**
     * Fills the grid with unrestricted cells when the parsing is finished.
     */
    @Override
    public void endDocument() {
        for (int i = 0; i < grid.length; ++i)
            for (int j = 0; j < grid[0].length; ++j)
                if (grid[i][j] == null)
                    grid[i][j] = new Cell();
    }
}
