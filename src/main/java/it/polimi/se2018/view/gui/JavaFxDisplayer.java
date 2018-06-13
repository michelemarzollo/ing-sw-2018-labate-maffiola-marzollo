package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.Displayer;
import it.polimi.se2018.view.ViewDataOrganizer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class displays the game using a JavaFX gui.
 */
public class JavaFxDisplayer extends Application implements Displayer {

    /**
     * The primary stage of the gui.
     */
    private Stage primaryStage;
    /**
     * The root layout element.
     */
    private BorderPane root = new BorderPane();
    /**
     * The client view for this displayer.
     */
    private ClientView clientView;
    /**
     * The displayed game board.
     */
    private GameBoard board;

    /**
     * The first method that is called and sets the {@code primaryStage}.
     *
     * @param primaryStage the primary stage of the gui.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new BorderPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        displayLoginView();
    }

    /**
     * Loads and updates the game board found at the given resource.
     *
     * @param resource The resource where the layout is stored.
     */
    private void displayGameBoard(String resource) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(resource));
        Node node;
        try {
            node = loader.load();
        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
            return;
        }
        root.setCenter(node);
        board = loader.getController();
        board.setDisplayer(this);
    }

    /**
     * Displays the login view, where the usere must insert it's username
     * and choose the type of connection and the game mode (multi-player or single-player).
     */
    @Override
    public void displayLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(this.getClass().getResource("login.fxml"));
            AnchorPane view = loader.load();

            setStageSize(623, 440);
            root.setCenter(view);
            primaryStage.setTitle("Sagrada - Login");

            LoginView controller = loader.getController();
            controller.setParentController(this);

        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }
    }

    /**
     * Displays a game board for multi player mode.
     */
    @Override
    public void displayMultiPlayerGame() {
        displayGameBoard("multiPlayerBoard.fxml");
    }

    /**
     * Displays a game for single player mode.
     */
    @Override
    public void displaySinglePlayerGame() {
        displayGameBoard("singlePlayerBoard.fxml");
    }

    /**
     * Shows on the screen if there were errors during the game.
     *
     * @param error The error message.
     */
    @Override
    public void displayError(String error) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(error);
        errorAlert.showAndWait();
    }

    /**
     * Shows to the player a set of patterns to allow him to decide which one
     * to use during the game.
     */
    @Override
    public void askPattern() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(this.getClass().getResource("pattern_card_selection.fxml"));
            AnchorPane view = loader.load();

            root.setCenter(view);
            primaryStage.setTitle("Window Pattern Card selection");

            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.setMaxWidth(1300);
            primaryStage.setMaxHeight(755);

            PatternSelection controller = loader.getController();
            for (int i = 0; i < getDataOrganizer().getGameSetup().getPlayers().length; i++) {
                if (getDataOrganizer().getGameSetup().getPlayers()[i].equals(clientView.getPlayerName())) {
                    controller.setParentController(this);
                    controller.setPatterns(getDataOrganizer().getGameSetup().getCandidates()[i]);
                    controller.setGrid();
                }
            }

        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }
    }

    /**
     * Asks, at the end of the game, which {@link it.polimi.se2018.model.PrivateObjectiveCard}
     * he wants to use to calculate the score on his final {@link it.polimi.se2018.view.gui.Pattern}.
     */
    @Override
    public void askPrivateObjective() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(this.getClass().getResource("private_selection.fxml"));
            AnchorPane view = loader.load();

            root.setCenter(view);
            primaryStage.setTitle("Private Objective Card selection");
            primaryStage.setMinWidth(770);
            primaryStage.setMinHeight(550);

            PrivateSelection controller = loader.getController();
            controller.setCards();
            controller.setParentController(this);

        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }
    }

    /**
     * Loads and displays the score board.
     */
    @Override
    public void displayScoreBoard() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("scoreBoard.fxml"));
        Node node;
        try {
            node = loader.load();
        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
            return;
        }
        root.setCenter(node);
        ScoreBoard controller = loader.getController();
        controller.setScoreBoardMap(getDataOrganizer().getScoreBoard());
    }

    /**
     * Changes the event pack of the game board to allow the user to select a die.
     */
    @Override
    public void selectDie() {
        BoardEventPack eventPack = new SelectDieEventPack(clientView);
        board.setEventPack(eventPack);
    }

    /**
     * Changes the event pack of the game board to allow the user to move one or two dice.
     */
    @Override
    public void moveDie(int amount) {
        BoardEventPack eventPack;
        if (amount == 1)
            eventPack = new MoveDieEventPack(clientView);
        else
            eventPack = new MoveDiceEventPack(clientView);
        board.setEventPack(eventPack);
    }

    /**
     * Asks, in SinglePlayer mode, the difficulty of the game. Depending on
     * the difficulty a certain number of cards will be given to the player,
     * to be used during the game.
     */
    @Override
    public void askDifficulty() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(this.getClass().getResource("ask_difficulty.fxml"));
            AnchorPane view = loader.load();

            setStageSize(623, 440);

            root.setCenter(view);
            primaryStage.setTitle("Sagrada - Difficulty selection");

            AskDifficulty controller = loader.getController();
            controller.setController(this);

        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }
    }

    /**
     * Changes the event pack of the game board to allow the user to swap one die
     * from the round track with one from the draft pool.
     */
    @Override
    public void askDiceToSwap() {
        BoardEventPack eventPack = new SwapDiceEventPack(clientView);
        board.setEventPack(eventPack);
    }

    /**
     * Changes the event pack of the game board to allow the user to choose die
     * value and place it on its pattern.
     */
    @Override
    public void askValueDestination() {
        int dieIndex = clientView.getDataOrganizer().getNextTurn().getForcedSelectionIndex();
        Die die = clientView.getDataOrganizer().getDraftPool().get(dieIndex);
        BoardEventPack eventPack = new SelectValueEventPack(clientView, die.getColour());
        board.setEventPack(eventPack);
    }

    @Override
    public void refreshDisplayedData() {
        if (board != null)
            board.refreshData();
    }

    /**
     * Resizes the stage.
     * @param width The new width.
     * @param height The new height.
     */
    private void setStageSize(int width, int height) {
        primaryStage.setMaxWidth(width);
        primaryStage.setMaxHeight(height);
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
    }

    /**
     * Displays a window where it's written that the player must wait for
     * some reason.
     *
     * @param reason the description of the reason for which the player must
     *               wait.
     */
    public void displayWaitingView(String reason) {

        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(this.getClass().getResource("waiting_view.fxml"));
            AnchorPane view = loader.load();

            setStageSize(623, 440);

            WaitingView controller = loader.getController();
            controller.setText(reason);

            root.setCenter(view);
            primaryStage.setTitle("Wait");

        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }

    }

    /**
     * Asks the user if he wants to confirm the choice he made.
     *
     * @param text a description of the choice that was just made.
     * @return {@code true} if he confirmed the choice, {@code} false otherwise.
     */
    public boolean displayConfirmationView(String text) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("confirmation_view.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Confirmation");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ConfirmationView controller = loader.getController();
            controller.setText(text + "\nDo you confirm your choice?");
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            return controller.isConfirmed();

        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
            return false;
        }
    }

    /**
     * The getter for the {@link ViewDataOrganizer} of the view.
     *
     * @return the {@link ViewDataOrganizer}.
     */
    @Override
    public ViewDataOrganizer getDataOrganizer() {
        return clientView.getDataOrganizer();
    }

    /**
     * The setter for the {@code clientView}.
     *
     * @param view The view that will handle requests.
     */
    @Override
    public void setView(ClientView view) {
        this.clientView = view;
    }

    /**
     * The getter for the {@code clientView}.
     *
     * @return the {@code clientView}.
     */
    @Override
    public ClientView getView() {
        return clientView;
    }

    @Override
    public void setDataOrganizer(ViewDataOrganizer organizer) {

    }

    @Override
    public void displayWaitMessage() {

    }

    @Override
    public void selectDieAndIncrement() {
        //@TODO add implementation: it's the view dedicated to Grozing Pliers.
    }


}
