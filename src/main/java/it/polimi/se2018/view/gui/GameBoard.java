package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.ObjectiveCard;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.events.GameSetup;
import it.polimi.se2018.model.events.PlayerConnectionStatus;
import it.polimi.se2018.model.events.PlayerStatus;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.view.Displayer;
import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

    private static final double OBJECTIVE_CARD_RATIO = 1.400267738;

    private static final double TOOL_CARD_RATIO = 1.422535211;

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
     * The BorderPane used to display the local player pattern.
     */
    @FXML
    private BorderPane playerPatternContainer;

    /**
     * The HBox used to display the round track.
     */
    @FXML
    private HBox roundTrackContainer;

    /**
     * The HBox used to display the draft pool.
     */
    @FXML
    private HBox draftPoolContainer;

    /**
     * The HBox used to display tool cards.
     */
    @FXML
    private HBox toolCardContainer;

    /**
     * The HBox used to display private cards.
     */
    @FXML
    private HBox privateCardContainer;

    /**
     * The HBox used to display public cards.
     */
    @FXML
    private HBox publicCardContainer;

    /**
     * The label to show the player who has the current turn.
     */
    @FXML
    private Label turnLabel;

    /**
     * Getter for the public card container
     *
     * @return The public card container.
     */
    private HBox getPublicCardContainer() {
        return publicCardContainer;
    }

    /**
     * Getter for the private card container
     *
     * @return The private card container.
     */
    private HBox getPrivateCardContainer() {
        return privateCardContainer;
    }

    /**
     * Getter for the tool card container
     *
     * @return The tool card container.
     */
    HBox getToolCardContainer() {
        return toolCardContainer;
    }

    /**
     * Getter for the daft pool container
     *
     * @return The draft pool container.
     */
    HBox getDraftPoolContainer() {
        return draftPoolContainer;
    }

    /**
     * Getter for the round track container
     *
     * @return The round track container.
     */
    private HBox getRoundTrackContainer() {
        return roundTrackContainer;
    }

    /**
     * Getter for the player pattern container
     *
     * @return The player pattern container.
     */
    BorderPane getPlayerPatternContainer() {
        return playerPatternContainer;
    }

    /**
     * Getter for the turn label.
     *
     * @return The turn label.
     */
    private Label getTurnLabel() {
        return turnLabel;
    }

    /**
     * Tells if it's the turn of the local player.
     *
     * @return {@code true} if it's the turn of the local player; {@code false} otherwise.
     */
    private boolean isPlayerTurn() {
        ViewDataOrganizer organizer = displayer.getDataOrganizer();
        if(organizer.getNextTurn() != null)
            return organizer.getNextTurn().getPlayerName().equals(organizer.getLocalPlayer());
        return false;
    }

    /**
     * Event handler for the end turn button.
     * <p>It ends the local player's turn if it's its turn.</p>
     *
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
     *
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
     *
     * @param index The index of the selected die.
     */
    private void handleDraftPoolSelection(int index) {
        if (isPlayerTurn())
            eventPack.draftPoolHandler(index);
    }

    /**
     * Handler called during die selection from the round track.
     * <p>It uses the current event pack to perform its operations.</p>
     *
     * @param coordinates The coordinates of the selected die.
     */
    private void handleRoundTrackSelection(Coordinates coordinates) {
        if (isPlayerTurn())
            eventPack.roundTrackHandler(coordinates);
    }

    /**
     * Sets the displayer used to represent the game.
     *
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
     *
     * @param playerName The name of the player to be displayed.
     * @param isOpponent {@code true} if the player is an opponent; {@code false} if it's
     *                   the local player.
     * @return The pattern layout for the given player status.
     */
    final AnchorPane loadPatternFor(String playerName, boolean isOpponent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("pattern.fxml"));
        AnchorPane pattern;
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

        patterns.put(playerName, controller);
        return pattern;
    }

    /**
     * Loads a pattern layout for the local player.
     *
     * @param playerName The name of the local player.
     * @return The pattern layout for the local player.
     */
    private AnchorPane loadPlayerPattern(String playerName) {
        return loadPatternFor(playerName, false);
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
            Card card = new Card(getPrivateCardContainer(), cardName, OBJECTIVE_CARD_RATIO);
            card.insert();
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
            Card card = new Card(getPublicCardContainer(), cardName, OBJECTIVE_CARD_RATIO);
            card.insert();
        }
    }

    /**
     * Handler called during tool card selection.
     * <p>It uses the current event pack to perform its operations.</p>
     *
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
            Card card = new Card(getToolCardContainer(), toolCard.getName(), TOOL_CARD_RATIO);
            card.insert();
            card.setOnClick(this::handleToolCardSelection);
        }
    }

    /**
     * Initializes the local player pattern layout.
     */
    private void initializePlayer() {
        if (displayer.getDataOrganizer().getGameSetup() == null)
            return;

        String localPlayer = displayer.getDataOrganizer().getLocalPlayer();
        for (String playerName : displayer.getDataOrganizer().getGameSetup().getPlayers()) {
            if (playerName.equals(localPlayer)) {
                AnchorPane pattern = loadPlayerPattern(playerName);
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
            if(playerStatus != null)
                entry.getValue().setStatus(playerStatus);
            checkConnected(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Checks if an opponent is connected or disconnected.
     * <p>If the player results disconnected, its pattern is altered in order to
     * display such information.</p>
     * @param pattern The pattern layout of the player.
     * @param playerName The name of the player to be checked.
     */
    private void checkConnected(Pattern pattern, String playerName) {
        PlayerConnectionStatus connectionStatus =
                getDisplayer().getDataOrganizer().getConnectionStatus(playerName);

        if (connectionStatus == null || connectionStatus.isConnected())
            pattern.setDisconnected(false);
        else
            pattern.setDisconnected(true);
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
        if (displayer.getDataOrganizer().getNextTurn() != null) {
            String currentPlayer = displayer.getDataOrganizer().getNextTurn().getPlayerName();
            getTurnLabel().setText("It's " + currentPlayer + "'s turn.");
        } else
            getTurnLabel().setText("");
    }

    private void refreshDraftPool(){
        ViewDataOrganizer organizer = displayer.getDataOrganizer();
        if (organizer.getDraftPool() != null){
            draftPoolFiller.setDice(displayer.getDataOrganizer().getDraftPool());
            if(organizer.getNextTurn() != null){
                int index = organizer.getNextTurn().getSacrificeIndex();
                if(index != -1)
                    draftPoolFiller.setSacrifice(index);
                index = organizer.getNextTurn().getForcedSelectionIndex();
                if(index != -1)
                    draftPoolFiller.setForcedSelection(index);
            }
        }
    }

    /**
     * Refreshes all displayed data that can be refreshed.
     */
    void refreshData() {
        refreshDraftPool();

        if (displayer.getDataOrganizer().getRoundTrack() != null)
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
     *
     * @param eventPack The new event pack.
     */
    synchronized void setEventPack(BoardEventPack eventPack) {
        this.eventPack = eventPack;
        eventPack.prepareControls(this);
    }

    /**
     * Returns the displayer used to represent the game.
     *
     * @return The displayer used to represent the game.
     */
    Displayer getDisplayer() {
        return displayer;
    }

    /**
     * Tells if the rule set of the game is single player or multi player.
     *
     * @return {@code true} if the active rule set is the multi player one; {@code false}
     * otherwise.
     */
    abstract boolean isMultiPlayer();

    /**
     * Getter for the draft pool filler.
     * <p>The returned object is not a copy of the original one.</p>
     *
     * @return The draft pool filler.
     */
    final DraftPoolFiller getDraftPoolFiller() {
        return draftPoolFiller;
    }
}
