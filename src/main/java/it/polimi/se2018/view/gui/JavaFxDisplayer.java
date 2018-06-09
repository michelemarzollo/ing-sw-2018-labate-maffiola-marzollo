package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.Displayer;
import it.polimi.se2018.view.ViewDataOrganizer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class displays the game using a JavaFX gui.
 */
public class JavaFxDisplayer extends Application implements Displayer {

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

    @Override
    public void start(Stage primaryStage) {
        //TODO
    }

    /**
     * Loads and updates the game board found at the given resource.
     * @param resource The resource where the layout is stored.
     */
    private void displayGameBoard(String resource){
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

    @Override
    public void displayError(String error) {
        //TODO
    }

    @Override
    public void askPattern() {
        //TODO
    }

    @Override
    public void askPrivateObjective() {
        //TODO
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

    @Override
    public void askDifficulty() {
        //TODO
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
        if(board != null)
            board.refreshData();
    }

    @Override
    public ViewDataOrganizer getDataOrganizer() {
        return clientView.getDataOrganizer();
    }

    @Override
    public void setView(ClientView view) {
        this.clientView = view;
    }

    @Override
    public ClientView getView() {
        return clientView;
    }
}
