package it.polimi.se2018.utils;
import it.polimi.se2018.model.events.ModelUpdate;
import it.polimi.se2018.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is a 'false' view that makes the abstract view concrete
 * and implements all abstract methods, in a way that the concrete method doesn't do
 * something relevant but allows to see whether the method that had to be called
 * was actually called.
 *
 * @author michelemarzollo
 */
public class MockView extends View {

    private List<String> calledMethods = new ArrayList<>();

    public MockView(String playerName) {
        setPlayerName(playerName);
    }

    /**
     * The getter for {@code calledMethods}.
     *
     * @return a copy of {@code calledMethods}.
     */
    public List<String> getCalledMethods() {
        return new ArrayList<>(calledMethods);
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showMultiPlayerGame() {
        calledMethods.add("showMultiPlayerGame");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showSinglePlayerGame() {
        calledMethods.add("showSinglePlayerGame");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showError(String error) {
        calledMethods.add("showError: " + error);
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showPatternSelection() {
        calledMethods.add("showPatternSelection");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showPrivateObjectiveSelection() {
        calledMethods.add("showPrivateObjectiveSelection");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showScoreBoard() {
        calledMethods.add("showScoreBoard");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showDieSelection() {
        calledMethods.add("showDieSelection");
    }

    @Override
    public void showDieIncrementSelection() {
        calledMethods.add("showDieIncrementSelection");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showMoveSelection(int amount) {
        calledMethods.add("showMoveSelection" + amount);
    }

    @Override
    public void showMoveUpToTwo() {
        calledMethods.add("showMoveUpToTwo");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showDifficultySelection() {
        calledMethods.add("showDifficultySelection");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showLensCutterSelection() {
        calledMethods.add("showLensCutterSelection");
    }

    /**
     * The method to simulate the corresponding method in the view.
     */
    @Override
    public void showValueDestinationSelection() {
        calledMethods.add("showValueDestinationSelection");
    }

    @Override
    public void showPlaceDie() {
        calledMethods.add("showPlaceDie");
    }

    @Override
    public void showConfirm() {
        calledMethods.add("showConfirm");
    }


    @Override
    public void update(ModelUpdate message) {
        calledMethods.add("update: " + message.getEventType());
    }
}
