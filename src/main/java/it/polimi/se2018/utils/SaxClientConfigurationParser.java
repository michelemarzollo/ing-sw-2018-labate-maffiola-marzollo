package it.polimi.se2018.utils;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The sax parser for the file that contains the information to create
 * the {@link ClientConfiguration}.
 *
 * @author michelemarzollo
 */
public class SaxClientConfigurationParser extends DefaultHandler {

    /**
     * The flag to say is the 'server_address' tag is being read.
     */
    private boolean inServerAddress;

    /**
     * The flag to say is the 'service_name' tag is being read.
     */
    private boolean inServiceName;

    /**
     * The flag to say is the 'port' tag is being read.
     */
    private boolean inPort;


    /**
     * The server of the server the client has to connect to.
     */
    private String serverAddress;

    /**
     * The name of the RMI service.
     */
    private String serviceName;

    /**
     * The number of the port to connect to in TCP connection.
     */
    private int portNumber;

    /**
     * The method that sets all flags to false when the document is starting to be read.
     */
    @Override
    public void startDocument() {
        inServerAddress = false;
        inServiceName = false;
        inPort = false;
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
        if (qualifiedName.equals("server_address"))
            inServerAddress = true;
        if (qualifiedName.equals("service_name"))
            inServiceName = true;
        if (qualifiedName.equals("server_port"))
            inPort = true;
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
        if (qualifiedName.equals("server_address"))
            inServerAddress = false;
        if (qualifiedName.equals("service_name"))
            inServiceName = false;
        if (qualifiedName.equals("server_port"))
            inPort = false;
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

        if (inServerAddress)
            serverAddress = string;
        if (inServiceName)
            serviceName = string;
        if (inPort)
            portNumber = Integer.parseInt(string);
    }

    /**
     * The method that, when the xml is finished to be read, creates the
     * {@link ClientConfiguration} with the read information.
     */
    @Override
    public void endDocument() {
        ClientConfiguration.makeInstance(serverAddress, serviceName, portNumber);
    }
}
