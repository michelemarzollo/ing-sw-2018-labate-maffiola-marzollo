package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.PlaceDie;
import it.polimi.se2018.model.events.SelectCard;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Observer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Base abstract class for game controllers.
 * <p>The class contains the basic game logic and some common
 * moves players can do.</p>
 * <p>Concrete subclasses have to implement the abstract methods
 * which allow to tweak the behavior of the above operations.</p>
 * <p>This class is an {@code Observable<ViewMessage>} and its instances
 * have to be registered as observer to all the {@code Observable<ViewMessage>}
 * that are handled by that instance.</p>
 * <p>This class has no thread-safety yet.</p>
 */
public abstract class Controller implements Observer<ViewMessage> {
    //Timer per scandenza turni?

    private final Game game;
    private final Map<String, ToolCardBehavior> toolCardBehaviors;


    protected abstract boolean canUseToolCard(String name);

    protected abstract void performAction(ViewMessage message);

    protected abstract int getDraftAmount();

    protected abstract void fillScoreBoard();

    protected abstract void calculateScores();

    protected abstract void registerPlayer(ViewMessage message);

    protected Controller(Game game) {
        this.game = game;
        toolCardBehaviors = new HashMap<>();
        //TODO: insert behaviors
    }

    public void update(ViewMessage message) {
        performAction(message);
    }

    protected void endTurn(ViewMessage message) {
        boolean updateSuccessful = game.getTurnManager().updateTurn();
        if (!updateSuccessful)
            endRound(message);
    }

    protected boolean checkTurnEnd(){
        Turn currentTurn = game.getTurnManager().getCurrentTurn();
        return currentTurn.hasAlreadyPlacedDie() && currentTurn.hasAlreadyUsedToolCard();
    }

    protected void endRound(ViewMessage message) {
        try {
            game.getTurnManager().setupNewRound();
            refillDraftPool();
            game.getTurnManager().updateTurn();
        } catch (TurnManager.GameFinishedException e) {
            endGame(message);
        }
    }

    protected void endGame(ViewMessage message) {
        cleanDraftPool();
        calculateScores();
        fillScoreBoard();
        message.getView().showScoreBoard();
    }

    protected void cleanDraftPool() {
        // ugly
        game.getDraftPool().getDice().clear();
    }

    protected void refillDraftPool() {
        game.getDraftPool().setDice(
                game.getDiceBag().draft(getDraftAmount())
        );
    }

    protected void placeDie(ViewMessage message) {
        PlaceDie placeMessage = (PlaceDie) message;

        String playerName = placeMessage.getPlayerName();
        Turn currentTurn = game.getTurnManager().getCurrentTurn();
        if (!currentTurn.getPlayer().getName().equals(playerName)) {
            placeMessage.getView().showError("Not your turn!");
            return;
        }

        if(currentTurn.hasAlreadyPlacedDie()){
            placeMessage.getView().showError("You already placed a die!");
            return;
        }

        try {
            Die die = game.getDraftPool().draft(placeMessage.getIndex());
            currentTurn.getPlayer().getPattern().placeDie(die, placeMessage.getDestination());
            currentTurn.placeDie();
        } catch (IndexOutOfBoundsException e) {
            placeMessage.getView().showError("Invalid selection!");
        } catch (PlacementErrorException e) {
            placeMessage.getView().showError(
                    "Placement doesn't respect restrictions!\n" + e.getMessage()
            );
        }

        if(checkTurnEnd())
            endTurn(message);
    }


    protected void activateToolCard(ViewMessage message){
        SelectCard selectMessage = (SelectCard) message;
        if(canUseToolCard(selectMessage.getName()))
            toolCardBehaviors.get(selectMessage.getName()).askParameters(message);
    }

    protected void applyToolCard(ViewMessage message){
        toolCardBehaviors.get(message.getAction()).useToolCard(game, message);

        if(checkTurnEnd())
            endTurn(message);
    }

    protected void selectPattern(ViewMessage message){
        SelectCard selectMessage = (SelectCard) message;
        String patternName = selectMessage.getName();
        String playerName = selectMessage.getPlayerName();
        Player player = game.getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .orElse(null);
        if(player == null){
            selectMessage.getView().showError("Invalid player!");
            return;
        }

        Pattern pattern = Arrays.stream(player.getCandidates())
                .filter(p -> p.getName().equals(patternName))
                .findFirst()
                .orElse(null);

        if(pattern == null){
            selectMessage.getView().showError("Invalid pattern!");
            return;
        }

        player.setPattern(pattern);

        boolean gameReady = game.getPlayers().stream()
                .allMatch(p -> p.getPattern() != null);

        if(gameReady)
            game.start();
    }

    protected void disconnectPlayer(ViewMessage message){
        String playerName = message.getPlayerName();
        game.getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .ifPresent(player -> player.setConnected(false));
    }

    protected void reconnectPlayer(ViewMessage message){
        String playerName = message.getPlayerName();
        game.getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .ifPresent(player -> player.setConnected(true));
    }


}
