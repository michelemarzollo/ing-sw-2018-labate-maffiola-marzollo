package it.polimi.se2018;

import it.polimi.se2018.networking.server.HybridServer;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.Displayer;
import it.polimi.se2018.view.DisplayerFactory;
import org.xml.sax.SAXException;

import java.util.Scanner;

/**
 * This class is the start utility of the application.
 */
public class App {
    /**
     * Usage string.
     */
    private static final String USAGE =
            "usage sagrada [--server | --gui | --cli] [--config CONFIG_FILE]\n\n" +
                    "Option\t\t\t\tMeaning\n\n" +
                    "--server\t\t\tRun as server\n" +
                    "--gui\t\t\t\tRun as client in gui-mode\n" +
                    "--cli\t\t\t\tRun as client in cli-mode\n" +
                    "--config CONFIG\t\tUse the file CONFIG as configuration";

    /**
     * Starts the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        new App().start(args);
    }

    /**
     * Parses the command line arguments and launches the application.
     *
     * @param args The command line arguments.
     */
    private void start(String[] args) {
        ClaParser parser = new ClaParser();
        parser.parse(args);

        if (parser.isError() || parser.isHelp())
            printUsage();
        else if (parser.isServer())
            launchServer(parser);
        else
            launchClient(parser);
    }

    /**
     * Prints usage.
     */
    private static void printUsage() {
        System.out.println(USAGE);
    }

    /**
     * Launches the server.
     *
     * @param parser The parser used for command line arguments.
     */
    private static void launchServer(ClaParser parser) {
        try {
            XmlServerConfigLoader serverConfigLoader = new XmlServerConfigLoader(parser.getConfigLocation());
            ServerConfiguration configuration = serverConfigLoader.loadConfiguration();

            HybridServer server = new HybridServer(configuration.getAddress(),
                    configuration.getServiceName(), configuration.getPortNumber());
            server.start();
            Scanner in = new Scanner(System.in);
            String cmd = in.nextLine();
            while (!cmd.equalsIgnoreCase("quit")) {
                cmd = in.nextLine();
            }
            server.stop();
        } catch (SAXException e) {
            Logger.getDefaultLogger().log("SAXException: " + e.getMessage());
        }
    }

    /**
     * Launches the client.
     *
     * @param parser The parser used for command line arguments.
     */
    private static void launchClient(ClaParser parser) {
        try {
            XmlClientConfigLoader clientConfigLoader = new XmlClientConfigLoader(parser.getConfigLocation());
            clientConfigLoader.loadConfiguration();
            Displayer displayer;
            if (parser.isCli())
                displayer = DisplayerFactory.getInstance().newCliDisplayer();
            else if (parser.isGui())
                displayer = DisplayerFactory.getInstance().newGuiDisplayer();
            else {
                printUsage();
                return;
            }
            new ClientView(displayer);
        } catch (SAXException e) {
            Logger.getDefaultLogger().log("SAXException: " + e.getMessage());
        }
    }
}
