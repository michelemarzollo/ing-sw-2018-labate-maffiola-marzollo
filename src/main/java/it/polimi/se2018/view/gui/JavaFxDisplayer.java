package it.polimi.se2018.view.gui;

import it.polimi.se2018.App;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.Displayer;
import it.polimi.se2018.view.ViewDataOrganizer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

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
     * The displayed game board JavaFX controller.
     */
    private GameBoard board;

    /**
     * The pattern selection JavaFX controller.
     */
    private PatternSelection patternSelection;

    @Override
    public void init() {
        App.setInstance(this);
    }

    public static void startGui(String[] args) {
        launch(args);
    }

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
        Platform.runLater(() -> {
            root.setCenter(node);
            board = loader.getController();
            board.setDisplayer(this);
            primaryStage.sizeToScene();
            primaryStage.setTitle("Sagrada");
        });
        refreshDisplayedData();
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

            Platform.runLater(() -> {
                root.setCenter(view);
                primaryStage.setTitle("Sagrada - Login");

                LoginView controller = loader.getController();
                controller.setParentController(this);
                primaryStage.sizeToScene();
            });

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
        Platform.runLater(() -> {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText(error);
            errorAlert.showAndWait();
        });
    }

    @Override
    public void displayWaitMessage() {
        askPattern();
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
            Platform.runLater(() -> {
                root.setCenter(view);
                primaryStage.setTitle("Window Pattern Card selection");
                primaryStage.sizeToScene();
            });
            patternSelection = loader.getController();
            refreshDisplayedData();


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
            Platform.runLater(() -> {
                root.setCenter(view);
                primaryStage.setTitle("Private Objective Card selection");

                PrivateSelection controller = loader.getController();
                controller.setCards();
                controller.setParentController(this);
                primaryStage.sizeToScene();
            });
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
        Platform.runLater(() -> {
            root.setCenter(node);
            ScoreBoard controller = loader.getController();
            controller.setScoreBoardMap(getDataOrganizer().getScoreBoard());
            primaryStage.sizeToScene();
        });
    }

    /**
     * Changes the event pack of the game board to allow the user to select a die.
     */
    @Override
    public void selectDie() {
        BoardEventPack eventPack = new SelectDieEventPack(clientView);
        Platform.runLater(() -> board.setEventPack(eventPack));
    }

    /**
     * Changes the event pack of the game board to allow the user to move one or two dice.
     */
    @Override
    public void moveDice(int amount, boolean moveAll) {
        BoardEventPack eventPack;
        eventPack = new MoveDiceEventPack(clientView, amount, moveAll);
        Platform.runLater(() -> board.setEventPack(eventPack));
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

            Platform.runLater(() -> {
                root.setCenter(view);
                primaryStage.setTitle("Sagrada - Difficulty selection");

                AskDifficulty controller = loader.getController();
                controller.setController(this);
                primaryStage.sizeToScene();
            });
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
        Platform.runLater(() -> board.setEventPack(eventPack));
    }

    /**
     * Changes the event pack of the game board to allow the user to choose die
     * value and place it on its pattern.
     */
    @Override
    public void askValueDestination() {
        int dieIndex = getDataOrganizer().getNextTurn().getForcedSelectionIndex();
        Die die = getDataOrganizer().getDraftPool().get(dieIndex);
        BoardEventPack eventPack = new SelectValueEventPack(clientView, die.getColour());
        Platform.runLater(() -> board.setEventPack(eventPack));
    }

    @Override
    public synchronized void refreshDisplayedData() {
        if (board != null) {
            if (getDataOrganizer().getScoreBoard() != null)
                displayScoreBoard();
            else
                Platform.runLater(() -> board.refreshData());
        }
        if (patternSelection != null && getDataOrganizer().getGameSetup() != null) {
            String player = getDataOrganizer().getLocalPlayer();
            int index = Arrays.asList(getDataOrganizer().getGameSetup().getPlayers()).indexOf(player);
            it.polimi.se2018.model.Pattern[] patterns = getDataOrganizer().getGameSetup().getCandidates()[index];
            Platform.runLater(() -> {
                patternSelection.setParentController(this);
                patternSelection.setPatterns(patterns);
                patternSelection.setGrid();
            });
        }
    }

    /**
     * Displays a window where it's written that the player must wait for
     * some reason.
     *
     * @param reason the description of the reason for which the player must
     *               wait.
     */
    void displayWaitingView(String reason) {

        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(this.getClass().getResource("waiting_view.fxml"));
            AnchorPane view = loader.load();

            primaryStage.sizeToScene();

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
    public boolean displayConfirm(String text) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, text, ButtonType.NO, ButtonType.OK);
        Optional<ButtonType> response = alert.showAndWait();

        return response.orElse(ButtonType.NO) == ButtonType.OK;
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
    public void askIncrement() {
        BoardEventPack eventPack = new IncrementDieValueEventPack(clientView);
        Platform.runLater(() -> board.setEventPack(eventPack));
    }

    @Override
    public void askPlacement() {
        BoardEventPack eventPack = new PlaceDieEventPack(clientView);
        Platform.runLater(() -> board.setEventPack(eventPack));
    }

    @Override
    public void askConfirm() {
        Platform.runLater(() -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> response = confirm.showAndWait();
            if (response.orElse(ButtonType.CLOSE) == ButtonType.OK)
                clientView.handleToolCardUsage();
        });
    }
}
