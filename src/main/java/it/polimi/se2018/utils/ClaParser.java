package it.polimi.se2018.utils;

/**
 * This class is a command line arguments parser.
 * <p>It is used to parse the
 * command line input and find what commands have been inserted: it saves the
 * commands that have been selected and is able to say if some mistake have
 * been made when inserting the input.</p>
 * <p>Only one among {@code isServer()}, {@code isCli()} and {@code isGui()} can be
 * true at any given time.</p>
 */
public class ClaParser {

    /**
     * Tells if the starting server command is present.
     */
    private boolean isServer;

    /**
     * Tells if the command to run the client on CLI is present.
     */
    private boolean isCli;

    /**
     * Tells if the command to run the client on GUI is present.
     */
    private boolean isGui;

    /**
     * Tells if the command to ask for help on possible instructions is present.
     */
    private boolean isHelp;

    /**
     * Tells if an invalid argument is present.
     */
    private boolean isError;

    /**
     * Contains the path where the configuration file passed as an
     * argument should be found.
     */
    private String configLocation;

    /**
     * Parse all the strings passed when launching the program and stores what
     * commands have been inserted in the class' attributes. If an invalid command is
     * present stops its execution.
     *
     * @param args The array of strings that represents the configuration info
     *             passed when the program is launched.
     */
    public void parse(String[] args) {

        boolean expectPath = false;
        try {
            for (String arg : args) {
                if ("--server".equals(arg.trim()))
                    setServer();
                else if ("--gui".equals(arg.trim()))
                    setGui();
                else if ("--cli".equals(arg.trim()))
                    setCli();
                else if ("--help".equals(arg.trim()))
                    setHelp();
                else if ("--config".equals(arg.trim()))
                    expectPath = true;
                else if (expectPath) {
                    setConfigLocation(arg);
                    expectPath = false;
                }
                //A not valid argument has been passed.
                else
                    throw new ConfigurationError();
            }
        } catch (ConfigurationError e) {
            Logger.getDefaultLogger().log("exc");
            setError();
        }
    }

    /**
     * Getter for the isServer attribute.
     *
     * @return {@code true} if the '--server' command has been inserted,
     * {@code false otherwise}.
     */
    public boolean isServer() {
        return isServer;
    }

    /**
     * Setter for the isServer attribute: sets isServer to {@code true} if the
     * '--server' command has been inserted.
     *
     * @throws ConfigurationError if at least one among {@code isServer()}, {@code isCli()}
     *                            and {@code isGui()} is already true.
     */
    private void setServer() throws ConfigurationError {
        if (isServer() || isCli() || isGui())
            throw new ConfigurationError();
        isServer = true;
    }

    /**
     * Getter for the isCli attribute.
     *
     * @return {@code true} if the '--cli' command has been inserted,
     * {@code false otherwise}.
     */
    public boolean isCli() {
        return isCli;
    }

    /**
     * Setter for the isCli attribute: sets isCli to {@code true} if the
     * '--cli' command has been inserted.
     *
     * @throws ConfigurationError if at least one among {@code isServer()}, {@code isCli()}
     *                            and {@code isGui()} is already true.
     */
    private void setCli() throws ConfigurationError {
        if (isServer() || isCli() || isGui())
            throw new ConfigurationError();
        isCli = true;
    }

    /**
     * Getter for the isGui attribute.
     *
     * @return {@code true} if the '--gui' command has been inserted,
     * {@code false otherwise}.
     */
    public boolean isGui() {
        return isGui;
    }

    /**
     * Setter for the isGui attribute: sets isGui to {@code true} if the
     * '--gui' command has been inserted.
     *
     * @throws ConfigurationError if at least one among {@code isServer()}, {@code isCli()}
     *                            and {@code isGui()} is already true.
     */
    private void setGui() throws ConfigurationError {
        if (isServer() || isCli() || isGui())
            throw new ConfigurationError();
        isGui = true;
    }

    /**
     * Getter for the isHelp attribute.
     *
     * @return {@code true} if the '--help' command has been inserted,
     * {@code false otherwise}.
     */
    public boolean isHelp() {
        return isHelp;
    }

    /**
     * Setter for the isHelp attribute: sets isHelp to {@code true} if the
     * '--help' command has been inserted.
     */
    private void setHelp() {
        isHelp = true;
    }

    /**
     * Getter for the isError attribute.
     *
     * @return {@code true} if at least one of the inserted commands {@code args}
     * have a non correct syntax, {@code false otherwise}.
     */
    public boolean isError() {
        return isError || getConfigLocation() == null || !(isServer() || isCli() || isGui());
    }

    /**
     * Setter for the isError attribute: forces isError to {@code true} if {@code parse}
     * find an argument with a incorrect syntax.
     */
    private void setError() {
        isError = true;
    }

    /**
     * Getter for the configLocation
     *
     * @return The path where to find the configuration file.
     */
    public String getConfigLocation() {
        return configLocation;
    }

    /**
     * Setter for the configLocation
     *
     * @param configLocation The string that represents the path where to find the
     *                       configuration file.
     */
    private void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * Internal exception to signal error during parsing.
     */
    private static class ConfigurationError extends Exception {
    }
}
