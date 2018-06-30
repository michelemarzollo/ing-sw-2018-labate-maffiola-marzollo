package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.PublicObjectiveCard;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * The sax parser for the xml files that describe public objective cards.
 *
 * @author michelemarzollo
 */
public class SaxPublicObjectiveBuilder extends DefaultHandler {

    /**
     * The controller that is handling the loading of the files.
     */
    private Controller controller;

    /**
     * Says if the content of the tag 'name' is being read.
     */
    private boolean inName;

    /**
     * Says if the content of the tag 'description' is being read.
     */
    private boolean inDescription;

    /**
     * Says if the content of the tag 'victoryPoints' is being read.
     */
    private boolean inVictoryPoints;

    /**
     * Says if the content of the tag 'worksOn' is being read.
     */
    private boolean inWorksOn;

    /**
     * Says if the content of the tag 'strategy' is being read.
     */
    private boolean inStrategy;

    /**
     * Says if the content of the tag 'colourArray' is being read.
     */
    private boolean inColourArray;

    /**
     * Says if the content of the tag 'valueArray' is being read.
     */
    private boolean inValueArray;

    /**
     * The name of the card to create.
     */
    private String name;

    /**
     * The description of the card to create.
     */
    private String description;

    /**
     * The description of the card to create.
     */
    private int victoryPoints;

    /**
     * The property (works on colours or on values) of the card to create.
     */
    private boolean propertyIsColour;

    /**
     * The string that describes which one will be the
     * class that will calculate the score of the card.
     */
    private String strategy;

    /**
     * The eventual array of colours to calculate the score.
     */
    private Colour[] colourArray;

    /**
     * The eventual array of values to calculate the score.
     */
    private Integer[] valueArray;

    /**
     * A map to connect the string {@code strategy} to the class that
     * will calculate the score of the card.
     */
    private static final Map<String, PublicObjectiveScore> scoreStrategies = new HashMap<>();

    /**
     * The constructor of the class.
     *
     * @param controller the controller that is handling the loading of the files.
     */
    SaxPublicObjectiveBuilder(Controller controller) {
        this.controller = controller;
    }

    /**
     * The method to set the map {@code scoreStrategies}.
     */
    private void registerScoreStrategy() {
        scoreStrategies.put("Row", new RowVarietyScore(victoryPoints, propertyIsColour));
        scoreStrategies.put("Column", new ColumnVarietyScore(victoryPoints, propertyIsColour));
        scoreStrategies.put("Diagonals", new DiagonalScore(victoryPoints, propertyIsColour));
        scoreStrategies.put("Grid", new GridVarietyScore(victoryPoints, propertyIsColour,
                propertyIsColour ? colourArray : valueArray));
    }

    /**
     * The method the set the attributes of the class when the file is starting
     * to be parsed.
     */
    @Override
    public void startDocument() {
        inName = false;
        inDescription = false;
        inVictoryPoints = false;
        inWorksOn = false;
        inStrategy = false;
        inColourArray = false;
        inValueArray = false;
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
        if (qualifiedName.equals("victoryPoints"))
            inVictoryPoints = true;
        if (qualifiedName.equals("worksOn"))
            inWorksOn = true;
        if (qualifiedName.equals("strategy"))
            inStrategy = true;
        if (qualifiedName.equals("colourArray"))
            inColourArray = true;
        if (qualifiedName.equals("valueArray"))
            inValueArray = true;
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
        if (qualifiedName.equals("victoryPoints"))
            inVictoryPoints = false;
        if (qualifiedName.equals("worksOn"))
            inWorksOn = false;
        if (qualifiedName.equals("strategy"))
            inStrategy = false;
        if (qualifiedName.equals("colourArray"))
            inColourArray = false;
        if (qualifiedName.equals("valueArray"))
            inValueArray = false;
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
        if (inVictoryPoints)
            victoryPoints = Integer.parseInt(string);
        if (inWorksOn)
            propertyIsColour = string.equals("colour");

        if (inStrategy)
            strategy = string;
        if (inColourArray) {
            String[] colourStrings = string.split(" ");
            colourArray = new Colour[colourStrings.length];
            for (int i = 0; i < colourStrings.length; i++) {
                colourArray[i] = Colour.valueOf(colourStrings[i]);
            }
        }
        if (inValueArray) {
            String[] integerStrings = string.split(" ");
            valueArray = new Integer[integerStrings.length];
            for (int i = 0; i < integerStrings.length; i++) {
                valueArray[i] = Integer.parseInt(integerStrings[i]);
            }
        }
    }

    /**
     * The actions to do when the document finishes: the class that is
     * created must be assigned to the controller.
     */
    @Override
    public void endDocument() {
        registerScoreStrategy();
        controller.addPublicScoreStrategy(newPublicScore());
    }

    /**
     * The method to create the {@link PublicObjectiveScore}, depending on the
     * attributes that were contained in the file.
     *
     * @return the {@link PublicObjectiveScore} of the card described in the file.
     */
    private PublicObjectiveScore newPublicScore() {
        PublicObjectiveScore scoreStrategy = scoreStrategies.get(strategy);
        if (scoreStrategy == null)
            throw new IllegalArgumentException();
        return scoreStrategy;
    }

    /**
     * The method that returns the {@link PublicObjectiveCard} described by the file.
     *
     * @return the {@link PublicObjectiveCard}.
     */
    public PublicObjectiveCard build() {
        return new PublicObjectiveCard(name, description, victoryPoints);
    }

}
