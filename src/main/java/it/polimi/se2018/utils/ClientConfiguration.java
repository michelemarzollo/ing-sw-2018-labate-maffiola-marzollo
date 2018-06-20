package it.polimi.se2018.utils;

/**
 * The class that contains the information to configure the client.
 * It is a particular singleton, with a method to set the parameters for the
 * first time.
 *
 * @author michelemarzollo
 */
public class ClientConfiguration {

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
     * The instance of the singleton.
     */
    private static ClientConfiguration instance = null;

    /**
     * The private constructor.
     *
     * @param serverAddress The server of the server the client has to connect to.
     * @param serviceName   The name of the RMI service.
     * @param port          The number of the port to connect to in TCP connection.
     */
    private ClientConfiguration(String serverAddress, String serviceName, int port) {
        this.serverAddress = serverAddress;
        this.serviceName = serviceName;
        this.portNumber = port;
    }

    /**
     * The method that instantiates the class. If it is called when {@code instance}
     * is not null it does nothing and ignores the new parameters.
     *
     * @param serverAddress The server of the server the client has to connect to.
     * @param serviceName   The name of the RMI service.
     * @param port          The number of the port to connect to in TCP connection.
     */
    public static void makeInstance(String serverAddress, String serviceName, int port) {
        if (instance == null)
            instance = new ClientConfiguration(serverAddress, serviceName, port);
    }

    /**
     * The getter of the instance. It shouldn't be called if {@code makeInstance()}
     * was never called.
     *
     * @return The instance of the singleton.
     * @throws MissingConfigurationException if the class parameters aren't already set.
     */
    public static ClientConfiguration getInstance() throws MissingConfigurationException {
        if (instance != null)
            return instance;
        else throw new MissingConfigurationException("There is no valid configuration for the client!");
    }

    /**
     * Resets the instance to null;
     */
    public static void reset(){
        instance = null;
    }

    /**
     * The getter for {@code serverAddress}.
     *
     * @return The server of the server the client has to connect to.
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * The getter for {@code serviceName}.
     *
     * @return The name of the RMI service.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * The getter for {@code portNumber}.
     *
     * @return The number of the port to connect to in TCP connection.
     */
    public int getPortNumber() {
        return portNumber;
    }
}
