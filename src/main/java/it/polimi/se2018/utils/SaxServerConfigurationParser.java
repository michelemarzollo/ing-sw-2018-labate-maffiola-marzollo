package it.polimi.se2018.utils;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The sax parser for the file that contains the information to create
 * the {@link ServerConfiguration}.
 *
 * @author michelemarzollo
 */
public class SaxServerConfigurationParser extends DefaultHandler {

    /**
     * The flag to say is the 'port' tag is being read.
     */
    private boolean inPort;

    /**
     * The flag to say is the 'address' tag is being read.
     */
    private boolean inAddress;

    /**
     * The flag to say is the 'service_name' tag is being read.
     */
    private boolean inServiceName;

    /**
     * The flag to say is the 'turn_duration' tag is being read.
     */
    private boolean inTurnDuration;

    /**
     * The flag to say is the 'multi_player_timeout' tag is being read.
     */
    private boolean inMultiPlayerTo;

    /**
     * The flag to say is the 'single_player_timeout' tag is being read.
     */
    private boolean inSinglePlayerTo;


    /**
     * The number of the port of the server.
     */
    private int portNumber;

    /**
     * The address of the server.
     */
    private String address;

    /**
     * The name of the RMI service.
     */
    private String serviceName;

    /**
     * The duration of a turn of the match.
     */
    private int turnDuration;

    /**
     * The timeout for multi-player mode.
     */
    private int multiPlayerTimeOut;

    /**
     * The timeout for single-player mode.
     */
    private int singlePlayerTimeOut;

    private ServerConfiguration serverConfiguration;

    /**
     * The method that sets all flags to false when the document is starting to be read.
     */
    @Override
    public void startDocument() {
        inPort = false;
        inAddress = false;
        inServiceName = false;
        inTurnDuration = false;
        inMultiPlayerTo = false;
        inSinglePlayerTo = false;
    }

    /**
     * The method that receives notification of the start of an element in the file and
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
        if (qualifiedName.equals("port"))
            inPort = true;
        if (qualifiedName.equals("address"))
            inAddress = true;
        if (qualifiedName.equals("service_name"))
            inServiceName = true;
        if (qualifiedName.equals("turn_duration"))
            inTurnDuration = true;
        if (qualifiedName.equals("multi_player_timeout"))
            inMultiPlayerTo = true;
        if (qualifiedName.equals("single_player_timeout"))
            inSinglePlayerTo = true;
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
        if (qualifiedName.equals("port"))
            inPort = false;
        if (qualifiedName.equals("address"))
            inAddress = true;
        if (qualifiedName.equals("service_name"))
            inServiceName = false;
        if (qualifiedName.equals("turn_duration"))
            inTurnDuration = false;
        if (qualifiedName.equals("multi_player_timeout"))
            inMultiPlayerTo = false;
        if (qualifiedName.equals("single_player_timeout"))
            inSinglePlayerTo = false;
    }

    /**
     * The method that collects the information of the file, when the
     * parameters of the tags are read.
     *
     * @param chars  The characters that codify the data.
     * @param start  The start position in the character array.
     * @param length The number of characters to use from the character array.
     */
    @Override
    public void characters(char[] chars, int start, int length) {

        String string = new String(chars, start, length);

        if (inPort)
            portNumber = Integer.parseInt(string);
        if (inAddress)
            address = string;
        if (inServiceName)
            serviceName = string;
        if (inTurnDuration)
            turnDuration = Integer.parseInt(string);
        if (inMultiPlayerTo)
            multiPlayerTimeOut = Integer.parseInt(string);
        if (inSinglePlayerTo)
            singlePlayerTimeOut = Integer.parseInt(string);
    }

    /**
     * The method that, when the xml is finished to be read, creates the
     * {@link ServerConfiguration} with the read information.
     */
    @Override
    public void endDocument() {
        serverConfiguration = ServerConfiguration.makeInstance(portNumber, address,
                serviceName, turnDuration, multiPlayerTimeOut, singlePlayerTimeOut);
    }

    /**
     * The getter for the {@link ServerConfiguration}
     *
     * @return The {@link ServerConfiguration} created from the file.
     */
    public ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }
}
