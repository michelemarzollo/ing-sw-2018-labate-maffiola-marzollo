package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * Input manager used to allow the player to login.
 *
 * @author giorgiolabate
 */
public class LoginManager extends InputEventManager {

    /**
     * The chosen type of connection.
     */
    private int connection;

    /**
     * The chosen game mode.
     */
    private int gameMode;

    /**
     * The name inserted by the player.
     */
    private String userName;

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public LoginManager(ClientView view, CliPrinter output) {
        super(view, output);
    }


    /**
     * Handles the input entered by the user.
     * <p>After collecting the wished network technology, the game mode and the user name,
     * logs the player in though a proper handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {

        if (connection != 1 && connection != 2) {
            handleConnection(input);
        } else if (gameMode != 1 && gameMode != 2) {
            handleGameMode(input);
        } else if (userName == null) {
            handleUserName(input);
            if (userName != null) {
                boolean multiPlayer = (gameMode == 2);
                boolean rmi = (connection == 2);
                getView().handleLogin(userName, multiPlayer, rmi);
            }
        }
    }

    /**
     * Handles the first user's input in this phase: the choice for the type
     * of connection.
     *
     * @param input The user's choice (represented by an int).
     */
    private void handleConnection(String input) {
        try {
            connection = Integer.parseInt(input);
            if (connection != 1 && connection != 2) {
                showError();
            }
        } catch (NumberFormatException ex) {
            showError();
        }

    }

    /**
     * Handles the second user's input in this phase: the choice for the game
     * mode.
     *
     * @param input The user's choice (represented by an int).
     */
    private void handleGameMode(String input) {
        try {
            gameMode = Integer.parseInt(input);
            if (gameMode != 1 && gameMode != 2) {
                showError();
            }
        } catch (NumberFormatException ex) {
            showError();
        }
    }

    /**
     * Handles the third user's input in this phase: the choice for the
     * username.
     *
     * @param input The name inserted by the user.
     */
    private void handleUserName(String input) {
        if (input.equals("")) {
            showError();
        } else {
            userName = input;
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (connection != 1 && connection != 2) {
            getOutput().println("Choose the type of connection, enter: \n" +
                    "1 for SOCKET\n" +
                    "2 for RMI");
        } else if (gameMode != 1 && gameMode != 2) {
            getOutput().println("Choose the game mode, enter: \n" +
                    "1 for SinglePlayer\n" +
                    "2 for MultiPlayer");
        } else if (userName == null) {
            getOutput().println("Enter your username:");
        }
    }

    /**
     * Resets the input manager.
     */
    @Override
    public void reset() {
        connection = 0;
        gameMode = 0;
        userName = null;
    }
}
