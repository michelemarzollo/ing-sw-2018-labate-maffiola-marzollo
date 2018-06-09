package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.ObjectiveCard;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.events.GameSetup;
import it.polimi.se2018.model.events.PlayerStatus;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.view.Displayer;
import it.polimi.se2018.view.ViewDataOrganizer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base JavaFX controller for the main game board.
 */
public abstract class GameBoard {

    /**
     * The current event pack.
     */
    private BoardEventPack eventPack;
    /**
     * The draft pool filler.
     */
    private DraftPoolFiller draftPoolFiller;
    /**
     * The round track filler.
     */
    private RoundTrackFiller roundTrackFiller;
    /**
     * The displayer used to represent the game.
     */
    private Displayer displayer;
    /**
     * The map linking players to their JavaFX pattern controller.
     */
    private Map<String, Pattern> patterns = new HashMap<>();

    /**
     * Getter for the public card container node.
     * @return The public card HBox container.
     */
    abstract HBox getPublicCardContainer();

    /**
     * Getter for the private card container node.
     * @return The private card HBox container.
     */
    abstract HBox getPrivateCardContainer();

    /**
     * Getter for the tool card container node.
     * @return The tool card HBox container.
     */
    abstract HBox getToolCardContainer();

    /**
     * Getter for the draft pool container node.
     * @return The draft pool HBox container.
     */
    abstract HBox getDraftPoolContainer();

    /**
     * Getter for the round track container node.
     * @return The round track HBox container.
     */
    abstract HBox getRoundTrackContainer();

    /**
     * Getter for the player pattern container node.
     * @return The player pattern BorderPane container.
     */
    abstract BorderPane getPlayerPatternContainer();

    /**
     * Getter for the label used to represent turns.
     * @return The turn label.
     */
    abstract Label getTurnLabel();

    /**
     * Tells if it's the turn of the local player.
     * @return {@code true} if it's the turn of the local player; {@code false} otherwise.
     */
    private boolean isPlayerTurn() {
        ViewDataOrganizer organizer = displayer.getDataOrganizer();
        return organizer.getNextTurn().getPlayerName().equals(organizer.getLocalPlayer());
    }

    /**
     * Event handler for the end turn button.
     * <p>It ends the local player's turn if it's its turn.</p>
     * @param event The mouse event.
     */
    @FXML
    private void endTurn(MouseEvent event) {
        if (isPlayerTurn())
            displayer.getView().handleEndTurn();
    }

    /**
     * Handler called during pattern cell selection.
     * <p>It uses the current event pack to perform its operations.</p>
     * @param coordinates The coordinates of the cell on the pattern.
     */
    private void handlePatternCellSelection(Coordinates coordinates) {
        if (isPlayerTurn()) {
            eventPack.patternHandler(coordinates);
        }
    }

    /**
     * Handler called during die selection from the draft pool.
     * <p>It uses the current event pack to perform its operations.</p>
     * @param index The index of the selected die.
     */
    private void handleDraftPoolSelection(int index) {
        if (isPlayerTurn())
            eventPack.draftPoolHandler(index);
    }

    /**
     * Handler called during die selection from the round track.
     * <p>It uses the current event pack to perform its operations.</p>
     * @param coordinates The coordinates of the selected die.
     */
    private void handleRoundTrackSelection(Coordinates coordinates) {
        if (isPlayerTurn())
            eventPack.roundTrackHandler(coordinates);
    }

    /**
     * Sets the displayer used to represent the game.
     * @param displayer The displayer used to represent the game.
     */
    public void setDisplayer(Displayer displayer) {
        this.displayer = displayer;
        start();

    }

    /**
     * Loads the pattern layout from fxml and sets the data to display.
     * <p>Every time a new pattern is loaded, the pair (player name, pattern controller)
     * is inserted in {@code patterns} to allow data refresh.</p>
     * @param playerStatus The status of the player to be displayed.
     * @param isOpponent {@code true} if the player is an opponent; {@code false} if it's
     *                               the local player.
     * @return The pattern layout for the given player status.
     */
    final BorderPane loadPatternFor(PlayerStatus playerStatus, boolean isOpponent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("pattern.xml"));
        BorderPane pattern;
        try {
            pattern = loader.load();
        } catch (IOException e) {
            Logger.getDefaultLogger().log(e.getMessage());
            return null;
        }

        Pattern controller = loader.getController();
        if (isOpponent)
            controller.minimize();
        else
            controller.setSelectionHandler(this::handlePatternCellSelection);

        controller.setStatus(playerStatus);
        patterns.put(playerStatus.getPlayerName(), controller);
        return pattern;
    }

    /**
     * Loads a pattern layout for the local player.
     * @param playerStatus The status of the local player.
     * @return The pattern layout for the local player.
     */
    private BorderPane loadPlayerPattern(PlayerStatus playerStatus) {
        return loadPatternFor(playerStatus, false);
    }

    /**
     * Populates the private objective container.
     */
    private void fillPrivateObjective() {
        ViewDataOrganizer organizer = displayer.getDataOrganizer();
        int index = Arrays.asList(organizer.getGameSetup().getPlayers())
                .indexOf(organizer.getLocalPlayer());

        for (ObjectiveCard objectiveCard : organizer.getGameSetup().getPrivateObjectives()[index]) {
            String cardName = objectiveCard.getName();
            Card card = new Card(getPrivateCardContainer(), cardName);
            getPrivateCardContainer().getChildren().add(card.getCard());
        }
    }

    /**
     * Populates the public objective container.
     */
    private void fillPublicObjective() {
        getPublicCardContainer().getChildren().clear();
        GameSetup setup = displayer.getDataOrganizer().getGameSetup();
        for (ObjectiveCard objectiveCard : setup.getPublicObjectives()) {
            String cardName = objectiveCard.getName();
            Card card = new Card(getPublicCardContainer(), cardName);
            getPublicCardContainer().getChildren().add(card.getCard());
        }
    }

    /**
     * Handler called during tool card selection.
     * <p>It uses the current event pack to perform its operations.</p>
     * @param cardName The name of the selected tool card.
     */
    private void handleToolCardSelection(String cardName) {
        if (isPlayerTurn())
            eventPack.toolCardHandler(cardName);
    }

    /**
     * Populates the tool card container.
     */
    private void fillToolCards() {
        getToolCardContainer().getChildren().clear();
        GameSetup setup = displayer.getDataOrganizer().getGameSetup();
        for (ToolCard toolCard : setup.getToolCards()) {
            Card card = new Card(getToolCardContainer(), toolCard.getName());
            ImageView cardImage = card.getCard();
            getToolCardContainer().getChildren().add(cardImage);
            card.setOnClick(this::handleToolCardSelection);
        }
    }

    /**
     * Initializes the local player pattern layout.
     */
    private void initializePlayer() {
        if (displayer.getDataOrganizer().getAllPlayerStatus() == null)
            return;

        String localPlayer = displayer.getDataOrganizer().getLocalPlayer();
        for (PlayerStatus player : displayer.getDataOrganizer().getAllPlayerStatus()) {
            if (player.getPlayerName().equals(localPlayer)) {
                BorderPane pattern = loadPlayerPattern(player);
                getPlayerPatternContainer().setCenter(pattern);
            }
        }
    }

    /**
     * Refreshes all displayed patterns.
     */
    private void refreshPatterns() {
        for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
            PlayerStatus playerStatus = getDisplayer().getDataOrganizer().getPlayerStatus(entry.getKey());
            entry.getValue().setStatus(playerStatus);
        }
    }

    /**
     * Prepares the game board for local player's turn.
     */
    private void prepareTurnForLocalPlayer() {
        boolean canPlace = !displayer.getDataOrganizer().getNextTurn().isAlreadyPlacedDie();
        boolean canUseToolCard = !displayer.getDataOrganizer().getNextTurn().isAlreadyUsedToolCard();
        setEventPack(new StandardEventPack(
                displayer.getView(),
                canPlace,
                canUseToolCard,
                isMultiPlayer()));
        getTurnLabel().setText("It's your turn.");
    }

    /**
     * Prepares the game board for opponents' turn.
     */
    private void prepareTurnForRemotePlayer() {
        setEventPack(new StandardEventPack(
                displayer.getView(),
                false,
                false,
                isMultiPlayer()));
        String currentPlayer = displayer.getDataOrganizer().getNextTurn().getPlayerName();
        getTurnLabel().setText("It's " + currentPlayer + "'s turn.");
    }

    /**
     * Refreshes all displayed data that can be refreshed.
     */
    void refreshData() {
        draftPoolFiller.setDice(displayer.getDataOrganizer().getDraftPool());
        roundTrackFiller.setLeftoverDice(displayer.getDataOrganizer().getRoundTrack());

        if (!isPlayerTurn())
            prepareTurnForRemotePlayer();
        else
            prepareTurnForLocalPlayer();

        refreshPatterns();
    }

    /**
     * Initializes the board layout.
     */
    void initializeLayout() {
        draftPoolFiller = new DraftPoolFiller(getDraftPoolContainer());
        draftPoolFiller.setSelectionHandler(this::handleDraftPoolSelection);
        roundTrackFiller = new RoundTrackFiller(getRoundTrackContainer());
        roundTrackFiller.setSelectionHandler(this::handleRoundTrackSelection);
        fillPrivateObjective();
        fillPublicObjective();
        fillToolCards();
        initializePlayer();
    }

    /**
     * Initializes and refreshes board layout.
     */
    private void start() {
        initializeLayout();
        refreshData();
    }

    /**
     * Sets the event pack to be used.
     * @param eventPack The new event pack.
     */
    synchronized void setEventPack(BoardEventPack eventPack) {
        this.eventPack = eventPack;
        eventPack.prepareControls(this);
    }

    /**
     * Returns the displayer used to represent the game.
     * @return The displayer used to represent the game.
     */
    Displayer getDisplayer() {
        return displayer;
    }

    /**
     * Tells if the rule set of the game is single player or multi player.
     * @return {@code true} if the active rule set is the multi player one; {@code false}
     * otherwise.
     */
    abstract boolean isMultiPlayer();

    /**
     * Getter for the draft pool filler.
     * <p>The returned object is not a copy of the original one.</p>
     * @return The draft pool filler.
     */
    final DraftPoolFiller getDraftPoolFiller(){
        return draftPoolFiller;
    }
}
