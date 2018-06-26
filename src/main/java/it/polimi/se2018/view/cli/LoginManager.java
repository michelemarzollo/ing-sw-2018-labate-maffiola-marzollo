package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

/**
 *it is the Login view that allows the user to choose the type of connection, the game mode
 and his username
 */


public class LoginManager extends  InputEventManager{

    private int connection;
    private int gameMode;
    private String userName;


    public LoginManager(ClientView view, CliImagePrinter output) { //questo viene settato dal Displayer quando arriva la displayLoginView
        super(view, output);
    }

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

    private void handleUserName(String input){
        if(input.equals("")){
            showError();
        }
        else{
            userName = input;
        }
    }


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
