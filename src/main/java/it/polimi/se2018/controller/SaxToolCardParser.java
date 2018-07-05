package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The class that parses an xml file that describes a {@link it.polimi.se2018.model.ToolCard}.
 *
 * @author michelemarzollo
 */
public class SaxToolCardParser extends DefaultHandler {

    /**
     * Says if the content of the tag 'name' is being read.
     */
    private boolean inName;

    /**
     * Says if the content of the tag 'description' is being read.
     */
    private boolean inDescription;

    /**
     * Says if the content of the tag 'colour' is being read.
     */
    private boolean inColour;

    /**
     * The name of the card to create.
     */
    private String name;

    /**
     * The description of the card to create.
     */
    private String description;

    /**
     * The colour of the ToolCard (for single player mode)
     *
     * @see it.polimi.se2018.model.ToolCard#colour
     */
    private Colour colour;

    /**
     * The loader of all tool cards, that contains the lists that must be filled
     * with the elements read from the xml file.
     */
    private XmlToolCardLoader xmlLoader;

    /**
     * The constructor of the class.
     *
     * @param xmlLoader The loader of all tool cards, that contains the lists that must be filled
     *                  with the elements read from the xml file.
     */
    SaxToolCardParser(XmlToolCardLoader xmlLoader) {
        this.xmlLoader = xmlLoader;
    }

    /**
     * The method the set the attributes of the class when the file is starting
     * to be parsed.
     */
    @Override
    public void startDocument() {
        inName = false;
        inDescription = false;
        inColour = false;
    }

    /**
     * The method that receives notification of the start of an element and
     * sets the corresponding attribute to {@code true}.
     *
     * @param uri           The Namespace URI, or the empty string if the element has no
     *                      Namespace URI or if Namespace processing is not being performed.
     * @param localName     The local name (without prefix), or the
     *                      empty string if Namespace processing is not being performed.
     * @param qualifiedName The qualified name (with prefix), or the empty string
     *                      if qualified names are not available.
     * @param attributes    The attributes attached to the element.
     *                      If there are no attributes, it shall be an empty Attributes object.
     */
    @Override
    public void startElement(String uri, String localName,
                             String qualifiedName, Attributes attributes) {
        if (qualifiedName.equals("name"))
            inName = true;
        if (qualifiedName.equals("description"))
            inDescription = true;
        if (qualifiedName.equals("colour"))
            inColour = true;
    }

    /**
     * The method that receives notification of the end of an element and
     * sets the corresponding attribute to {@code false}.
     *
     * @param uri           The Namespace URI, or the empty string if the element has no
     *                      Namespace URI or if Namespace processing is not being performed.
     * @param localName     The local name (without prefix), or the
     *                      empty string if Namespace processing is not being performed.
     * @param qualifiedName The qualified name (with prefix), or the empty string
     *                      if qualified names are not available.
     */
    @Override
    public void endElement(String uri, String localName,
                           String qualifiedName) {
        if (qualifiedName.equals("name"))
            inName = false;
        if (qualifiedName.equals("description"))
            inDescription = false;
        if (qualifiedName.equals("colour"))
            inColour = false;
    }

    /**
     * The method that saves the data inside the elements of the xml file,
     * in different ways depending on which is the content of the element.
     *
     * @param chars  The characters that codify the data.
     * @param start  The start position in the character array.
     * @param length The number of characters to use from the character array.
     */
    @Override
    public void characters(char[] chars, int start, int length) {
        String string = new String(chars, start, length);
        if (inName)
            name = string;
        if (inDescription)
            description = string;
        if (inColour)
            colour = Colour.valueOf(string);
    }

    /**
     * The actions to do when the document finishes: the lists of the {@code xmlLoader}
     * must be updated with the information of the new card.
     */
    @Override
    public void endDocument() {
        xmlLoader.addListElements(name, description, colour);
    }

}
