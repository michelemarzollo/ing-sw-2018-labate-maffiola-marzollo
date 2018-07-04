package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.DiceBag;
import it.polimi.se2018.model.Pattern;
import it.polimi.se2018.model.events.GameEnd;
import it.polimi.se2018.model.events.GameSetup;
import it.polimi.se2018.model.events.NextTurn;
import it.polimi.se2018.model.events.PlayerConnectionStatus;
import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.Displayer;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

/**
 * This class displays the game through the command line interface.
 * <p>It is used by the View to display the game on CLI. It receives the model update
 * and just set the correct {@link InputEventManager} for the user interaction and prints
 * what is necessary.</p>
 */
public class CliDisplayer implements Displayer {

    /**
     * The input gatherer that scan the user input and passes it
     * to the correct manager set by the CliDisplayer.
     */
    private CliInput input;

    /**
     * The 'printer' through which the CliDisplayer displays some information on a
     * {@link PrintStream}.
     */
    private CliPrinter output;

    /**
     * The {@link ClientView} to which the CliDisplayer is bound.
     */
    private ClientView view;

    /**
     * Callback function to use upon instantiation.
     */
    private static Consumer<Displayer> callback;

    /**
     * Flag to indicate if game setup has already been displayed.
     */
    private boolean alreadySetup = false;

    /**
     * Constructor of the class.
     *
     * @param inputStream The {@link InputStream} from which the user's input will
     *                    be gathered.
     * @param output      The {@link PrintStream} on which the game will be displayed.
     */
    private CliDisplayer(InputStream inputStream, PrintStream output) {
        this.input = new CliInput(inputStream); //faccio partire il thread
        new Thread(input).start();
        this.output = new CliPrinter(output);
        callback.accept(this);
    }

    /**
     * Launches the cli.
     *
     * @param in       The stream used for input.
     * @param out      The stream used for output.
     * @param callback The callback function to register the displayer.
     */
    public static void launchCli(InputStream in, PrintStream out, Consumer<Displayer> callback) {
        CliDisplayer.callback = callback;
        new CliDisplayer(in, out);
    }

    /**
     * Getter for the {@link ClientView} to which the CliDisplayer is bound.
     *
     * @return the {@link ClientView} to which the CliDisplayer is associated.
     */
    public ClientView getView() {
        return view;
    }

    /**
     * Getter for the {@link ViewDataOrganizer} associated to the CliDisplayer.
     *
     * @return the {@link ViewDataOrganizer} to which the CliDisplayer refers.
     */
    @Override
    public ViewDataOrganizer getDataOrganizer() {
        return view.getDataOrganizer();
    }

    /**
     * Setter for the {@link ClientView}
     *
     * @param view The view that will handle requests.
     */
    @Override
    public void setView(ClientView view) {
        this.view = view;
    }

    /**
     * Forces the displayed data to be refreshed.
     * <p>If the last Model Update is a {@link PlayerConnectionStatus} message
     * then all the clients will be notified. If it is a {@link GameSetup}
     * message then we are at the begging phase of the game and the pattern's
     * choice can be displayed. If it is a {@link NextTurn} message it means
     * that we are in the central phase of the game and a turn view is displayed.
     * Finally, if it is a {@link GameEnd} message it means that the game is ended
     * and the final score board can be displayed.</p>
     */
    @Override
    public void refreshDisplayedData() {
        int index = getDataOrganizer().getChangedConnectionIndex();
        if (index != -1) {
            PlayerConnectionStatus connectionStatus
                    = getDataOrganizer().getAllConnectionStatus().get(index);
            if (connectionStatus.isConnected()) {
                output.println(connectionStatus.getPlayerName() + " reconnected!");
            } else {
                output.println(connectionStatus.getPlayerName() + " disconnected!");
            }
            return;
        }
        if(!alreadySetup){
            alreadySetup = true;
            input.updatePrompt();
        } else if (input.isGameRunning() && mustUpdatePrompt())
            restoreInputManager();

    }

    /**
     * Tries to reset the input manager of the input gatherer.
     * <p>If it fails, the manager is rolled back to the turn manager.</p>
     */
    private void restoreInputManager() {
        try {
            input.resetInputManager();
        } catch (UnsupportedOperationException e) {
            //Can't reset, roll back to turn manager
            input.setManager(new TurnHandlingManager(view, output));
        }
    }

    /**
     * Tells if it is the case to update the prompt message.
     * <p>This is the case only when a new turn has to be notified to the player.</p>
     * @return {@code true} if the prompt has to be updated; {@code false} otherwise.
     */
    private boolean mustUpdatePrompt() {
        NextTurn nextTurn = getDataOrganizer().getNextTurn();
        if (nextTurn == null)
            return false;
        return getDataOrganizer().isTurnChanged() &&
                !(nextTurn.isAlreadyPlacedDie() && nextTurn.isAlreadyUsedToolCard());
    }

    /**
     * Displays the login screen and select the appropriate manager for the
     * {@link CliInput}.
     */
    @Override
    public void displayLoginView() {
        output.printHeader();
        input.setManager(new LoginManager(view, output));
    }

    /**
     * Allows the user to select a pattern among its candidates by setting the appropriate
     * manager for the {@link CliInput}.
     */
    @Override
    public void askPattern() {
        input.setManager(new PatternSelectionManager(view, output));
    }

    /**
     * Displays the multi player game by setting the correct input event handler
     * in {@link ClientView}.
     */
    @Override
    public void displayMultiPlayerGame() {
        displayGame();
    }

    /**
     * Displays an error message sent from the Model and restores the view state.
     *
     * @param error The error message.
     */
    @Override
    public void displayError(String error) {
        output.println(error);
        restoreInputManager();
    }

    /**
     * Displays the final score board at the end of the game.
     * <p>Also, ensures the connection is gracefully closed.</p>
     */
    @Override
    public void displayScoreBoard() {
        output.println("\n\nThe game is finished.");
        output.printScoreBoard(getDataOrganizer().getScoreBoard());
        output.println("Press enter to exit.");
        input.stop();
        getView().handleDisconnect();
    }

    /**
     * Sets the appropriate manager to select a die from the draft pool.
     */
    @Override
    public void selectDie() {
        input.setManager(new SelectDieManager(view, output));
    }

    /**
     * Sets the correct manager when there is the possibility of moving some dice
     * on the user's Pattern.
     *
     * @param amount  The maximum amount of dice to be moved.
     * @param moveAll Indicates whether the Tool Card forces to move exactly
     *                the maximum amount of dice ({@code true}) or if it allows to move at
     *                most the given amount of dice ({@code false}).
     */
    @Override
    public void moveDice(int amount, boolean moveAll) {
        input.setManager(new MoveDiceManager(view, output, amount, moveAll));
    }

    /**
     * Sets the correct manager to handle the possibility of swapping two dice
     * between the Draft Pool and the RoundTrack.
     */
    @Override
    public void askDiceToSwap() {
        input.setManager(new DiceSwappingManager(view, output));
    }

    /**
     * Selects the correct manager to handle the possibility of choosing a value
     * for a die drafted from the {@link DiceBag} and to put it on the user's Pattern.
     */
    @Override
    public void askValueDestination() {
        input.setManager(new ValueAndDestinationManager(view, output));
    }

    /**
     * Selects the correct manager for the selection of a die and the choice
     * about incrementing or decrementing its value.
     */
    @Override
    public void askIncrement() {
        input.setManager(new DieIncrementManager(view, output));
    }


    /**
     * Selects the correct manager to handle the placement of a die through a Tool Card.
     */
    @Override
    public void askPlacement() {
        input.setManager(new ToolCardPlacementManager(view, output));
    }

    /**
     * Selects the correct manager to handle confirmation when activating a Tool Card
     * that needs no parameters.
     */
    @Override
    public void askConfirm() {
        input.setManager(new ToolCardConfirmManager(view, output));
    }

    /**
     * Displays the single player game by setting the correct input event handler
     * in {@link ClientView}.
     */
    @Override
    public void displaySinglePlayerGame() {
        displayGame();
    }

    /**
     * Prints the player pattern and allows the player to interact by setting an appropriate
     * input event manager in {@link CliInput}.
     */
    private void displayGame(){
        output.println("\n\nYour pattern is:");
        output.printPattern(getPattern());
        output.println("Wait for the game to start");

        input.setManager(new TurnHandlingManager(view, output));
    }

    /**
     * Selects the correct manager to handle the possibility of choosing
     * the game's difficulty level in SinglePlayer mode.
     */
    @Override
    public void askDifficulty() {
        input.setManager(new DifficultyManager(view, output));
    }

    /**
     * Selects the correct manager for the moment of choosing between the user's
     * two Private Objective Cards at the end of the game in Single Player mode.
     */
    @Override
    public void askPrivateObjective() {
        input.setManager(new PrivateObjectiveManager(view, output));
    }

    /**
     * Getter for the user's chosen Pattern.
     *
     * @return The Pattern chosen from the user at the beginning of the game.
     */
    private Pattern getPattern() {
        return getDataOrganizer().getPlayerStatus(view.getPlayerName()).getPattern();
    }

}
