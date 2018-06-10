package it.polimi.se2018.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * The controller to show the login view, which asks the username, the game
 * mode and the type of connection.
 *
 * @author michelemarzollo
 */
public class LoginView {

    /**
     * The main controller, which instantiates this one and has the reference to the
     * client view.
     */
    private JavaFxDisplayer parentController;

    /**
     * The {@link TextField} to insert the username.
     */
    @FXML
    private TextField usernameText;

    /**
     * The button to start the game when choice were made.
     */
    @FXML
    private Button startButton;

    /**
     * The {@link RadioButton} to choose the RMI connection.
     */
    @FXML
    private RadioButton rmiButton;

    /**
     * The {@link RadioButton} to choose the socket connection.
     */
    @FXML
    private RadioButton socketButton;

    /**
     * The {@link RadioButton} to choose the multi-player game mode.
     */
    @FXML
    private RadioButton multiPlayerButton;

    /**
     * The {@link RadioButton} to choose the single-player game mode.
     */
    @FXML
    private RadioButton singlePlayerButton;

    /**
     * The setter for the {@code parentController}.
     *
     * @param parentController the main controller, which instantiates this one and has the reference to the
     *                         client view.
     */
    public void setParentController(JavaFxDisplayer parentController) {
        this.parentController = parentController;
    }

    /**
     * The method that handles the login. It collects all the necessary
     * information and calls the method on the {@link it.polimi.se2018.view.ClientView}
     * to interact with the controller.
     */
    @FXML
    private void login() {

        boolean multiPlayer = multiPlayerButton.isSelected();
        boolean rmi = rmiButton.isSelected();

        String username = usernameText.getText();
        if (username.equals(""))
            parentController.displayError("You must insert a username!");
        else {
            if (rmi) {
                parentController.getView().handleLogin(username, "//localhost/MyServer", 1099);
            } else {
                parentController.getView().handleLogin(username, "//localhost/MyServer", "servicename");
            }
            parentController.displayWaitingView("Waiting for connection...");
        }

    }

}
