package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 * This manager handles the beginning phase of the game when the user
 * has to choose the type of connection, the game mode and the username.
 * It is set as manager for the input by the {@link CliDisplayer} when
 * the displayLoginView is invoked.
 */
public class LoginManager extends  InputEventManager{

    /**
     * Indicates the type of connection chosen.
     */
    private int connection;

    /**
     * Indicates the gameMode chosen.
     */
    private int gameMode;

    /**
     * Contains the name inserted by the player.
     */
    private String userName;


    /**
     * Constructor of the class
     * @param view The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public LoginManager(ClientView view, CliImagePrinter output) {
        super(view, output);
    }

    /**
     * This method is the one delegated for handling the input entered by the user
     * in a correct way. When all the data have been gathered the handling is delegated
     * to the {@link ClientView} that will create the correct message for sending it
     * on the network.
     * @param input The String inserted by the user that represents his choice.
     */
    @Override
    public void handle(String input) {

        if (connection != 1 && connection != 2) {
            handleConnection(input);
        }

        else if (gameMode != 1 && gameMode != 2) {
            handleGameMode(input);
        }

        else if(userName == null){
            handleUserName(input);
            if(userName != null) {
                boolean multiPlayer = (gameMode == 2);
                boolean rmi = (connection == 2);
                view.handleLogin(userName, multiPlayer, rmi);
            }
        }
    }

    /**
     * Handles the first user's input in this phase: the choice for the type
     * of connection
     * @param input The user's choice (represented by an int).
     */
    private void handleConnection(String input){
        try {
            connection = Integer.parseInt(input);
            if(connection != 1 && connection != 2){
                showError();
            }
        }
        catch (NumberFormatException ex){
            showError();
        }

    }

    /**
     * Handles the second user's input in this phase: the choice for the game
     * mode.
     * @param input The user's choice (represented by an int).
     */
    private void handleGameMode(String input){
        try {
            gameMode = Integer.parseInt(input);
            if(gameMode != 1 && gameMode != 2){
                showError();
            }
        }
        catch (NumberFormatException ex){
            showError();
        }
    }

    /**
     * Handles the third user's input in this phase: the choice for the
     * username.
     * @param input The name inserted by the user.
     */
    private void handleUserName(String input){
        if(input.equals("")){
            showError();
        }
        else{
            userName = input;
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can and what he has to insert.
     */
    @Override
    public void showPrompt(){
        if (connection != 1 && connection != 2) {
            output.printTextNewLine("Choose the type of connection, enter: \n" +
                    "1 for SOCKET\n" +
                    "2 for RMI");
        }

        else if (gameMode != 1 && gameMode != 2) {
            output.printTextNewLine("Choose the game mode, enter: \n" +
                    "1 for SinglePlayer\n" +
                    "2 for MultiPlayer");
        }

        else {
            output.printTextNewLine("Enter your username:");
        }
    }
}
