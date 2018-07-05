package it.polimi.se2018.networking.client;

import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.utils.ClientConfiguration;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.MissingConfigurationException;

import java.io.IOException;

/**
 * Static class to decouple network handler instantiation from its usage.
 *
 * @author dvdmff
 */
public class NetworkHandlerFactory {

    /**
     * Private constructor to avoid instantiation.
     */
    private NetworkHandlerFactory() {
    }

    /**
     * Creates a new {@link RmiNetworkHandler} according to the current {@link ClientConfiguration}.
     *
     * @return An instance of {@link RmiNetworkHandler}, or {@code null} if configuration is missing.
     */
    public static ServerNetInterface newRmiNetHandler() {
        try {
            ClientConfiguration configuration = ClientConfiguration.getInstance();
            return new RmiNetworkHandler(
                    configuration.getServerAddress(),
                    configuration.getServiceName());
        } catch (MissingConfigurationException e) {
            Logger.getDefaultLogger().log(e.getMessage());
            return null;
        }
    }

    /**
     * Creates a new {@link TcpNetworkHandler} according to the current {@link ClientConfiguration}.
     *
     * @return An instance of {@link TcpNetworkHandler}, or {@code null} if configuration is missing or
     * the port is occupied.
     */
    public static ServerNetInterface newTcpNetHandler() {
        try {
            ClientConfiguration configuration = ClientConfiguration.getInstance();
            return new TcpNetworkHandler(
                    configuration.getServerAddress(),
                    configuration.getPortNumber());
        } catch (MissingConfigurationException | IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
            return null;
        }
    }
}
