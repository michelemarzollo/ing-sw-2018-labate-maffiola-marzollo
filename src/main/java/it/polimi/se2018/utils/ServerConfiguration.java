package it.polimi.se2018.utils;

/**
 * The class that contains the information to configure the server.
 * It is a particular singleton, with a method to set the parameters for the
 * first time.
 *
 * @author michelemarzollo
 */
public class ServerConfiguration {

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
     *
     * @see it.polimi.se2018.controller.MultiPlayerController#timeOut
     */
    private int multiPlayerTimeOut;

    /**
     * The timeout for single-player mode.
     *
     * @see it.polimi.se2018.controller.SinglePlayerController#timeOut
     */
    private int singlePlayerTimeOut;

    /**
     * The instance of the singleton.
     */
    private static ServerConfiguration instance = null;

    /**
     * The private constructor.
     *
     * @param portNumber          The number of the port of the server.
     * @param address             The address of the server.
     * @param serviceName         The name of the RMI service.
     * @param turnDuration        The duration of a turn of the match.
     * @param multiPlayerTimeOut  The timeout for multi-player mode.
     * @param singlePlayerTimeOut The timeout for single-player mode.
     */
    private ServerConfiguration(int portNumber, String address, String serviceName, int turnDuration,
                                int multiPlayerTimeOut, int singlePlayerTimeOut) {
        this.portNumber = portNumber;
        this.address = address;
        this.serviceName = serviceName;
        this.turnDuration = turnDuration;
        this.multiPlayerTimeOut = multiPlayerTimeOut;
        this.singlePlayerTimeOut = singlePlayerTimeOut;
    }

    /**
     * The method that instantiates the class. If it is called when {@code instance}
     * is not null it returns the instance that is already there.
     *
     * @param portNumber          The number of the port of the server.
     * @param address             The address of the server.
     * @param serviceName         The name of the RMI service.
     * @param turnDuration        The duration of a turn of the match.
     * @param multiPlayerTimeOut  The timeout for multi-player mode.
     * @param singlePlayerTimeOut The timeout for single-player mode.
     * @return The instance of the singleton.
     */
    public static ServerConfiguration makeInstance(
            int portNumber, String address, String serviceName,
            int turnDuration, int multiPlayerTimeOut, int singlePlayerTimeOut) {

        if (instance == null)
            instance = new ServerConfiguration(portNumber, address, serviceName,
                    turnDuration, multiPlayerTimeOut, singlePlayerTimeOut);
        return instance;
    }

    /**
     * The getter of the instance. It shouldn't be called if {@code makeInstance()}
     * was never called.
     *
     * @return The instance.
     * @throws MissingConfigurationException if the class parameters aren't already set.
     */
    public static ServerConfiguration getInstance() throws MissingConfigurationException {
        if (instance != null)
            return instance;
        else throw new MissingConfigurationException("There is no valid configuration for the server!");
    }

    /**
     * The getter for {@code portNumber}.
     *
     * @return The number of the port of the server.
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * The getter for {@code address}.
     *
     * @return The address of the server.
     */
    public String getAddress() {
        return address;
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
     * The getter for {@code turnDuration}.
     *
     * @return The duration of a turn of the match.
     */
    public int getTurnDuration() {
        return turnDuration;
    }

    /**
     * The getter for {@code multiPlayerTimeOut}.
     *
     * @return The timeout for multi-player mode.
     */
    public int getMultiPlayerTimeOut() {
        return multiPlayerTimeOut;
    }

    /**
     * The getter for {@code singlePlayerTimeOut}.
     *
     * @return The timeout for single-player mode.
     */
    public int getSinglePlayerTimeOut() {
        return singlePlayerTimeOut;
    }

}
