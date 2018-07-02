package it.polimi.se2018.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * The controller to show the login view, which asks the username, the game
 * mode and the type of connection.
 *
 * @author michelemarzollo
 */
public class LoginView {

    private static final String MISSING_USERNAME = "You must insert a username!";

    private static final String WAITING_MESSAGE = "Waiting for other players to connect...";

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
     * The {@link RadioButton} to choose the RMI connection.
     */
    @FXML
    private RadioButton rmiButton;

    /**
     * The {@link RadioButton} to choose the multi-player game mode.
     */
    @FXML
    private RadioButton multiPlayerButton;

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
            parentController.displayError(MISSING_USERNAME);
        else {
            parentController.getView().handleLogin(username, multiPlayer, rmi);
            parentController.displayWaitingView(WAITING_MESSAGE);
        }

    }

}
