package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.*;
import it.polimi.se2018.view.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

/**
 * This class displays the game through the command line interface. It is used
 * by the View to display the game on CLI. It receives the model update and just
 * set the correct {@link InputEventManager} for the user interaction and prints
 * what is necessary.
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
    private CliImagePrinter output;

    /**
     * The {@link ClientView} to which the CliDisplayer is bound.
     */
    private ClientView view;

    private static Consumer<Displayer> callback;

    /**
     * Constructor of the class.
     *
     * @param inputStream The {@link InputStream} from which the user's input will
     *                    be gathered.
     * @param output      The {@link PrintStream} on which the game will be displayed.
     */
    private CliDisplayer(InputStream inputStream, PrintStream output) {
        this.input = new CliInput(inputStream); //faccio partire il thread
        this.output = new CliImagePrinter(output);
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
     * Forces the displayed data to be refreshed. It is invoked every time
     * a {@link it.polimi.se2018.model.events.ModelUpdate} is notified to
     * the client and it is used to refresh the view selected by the
     * {@link ClientView}.
     * If the last Model Update is a {@link PlayerConnectionStatus} message
     * then all the clients will be notified. If it is a {@link GameSetup}
     * message then we are at the begging phase of the game and the pattern's
     * choice can be displayed. If it is a {@link NextTurn} message it means
     * that we are in the central phase of the game and a turn view is displayed.
     * Finally, if it is a {@link GameEnd} message it means that the game is ended
     * and the final score board can be displayed.
     */
    @Override
    public void refreshDisplayedData() {
        if (getDataOrganizer().getLastUpdate().getEventType().equals(ModelEvent.PLAYER_CONNECTION_STATUS)) {
            PlayerConnectionStatus connectionStatus = (PlayerConnectionStatus) getDataOrganizer().getLastUpdate();
            if (connectionStatus.isConnected()) {
                output.printTextNewLine(connectionStatus.getPlayerName() + "reconnected!");
            } else {
                output.printTextNewLine(connectionStatus.getPlayerName() + "disconnected!");
            }
            return;
        }
        if (!view.isGameRunning() && getDataOrganizer().getLastUpdate().getEventType().equals(ModelEvent.GAME_SETUP)) {
            askPattern();
            return;
        }
        if (view.isGameRunning() && getDataOrganizer().getLastUpdate().getEventType().equals(ModelEvent.NEXT_TURN)) {
            displayTurnView();
            return;
        }
        if (view.isGameRunning() && getDataOrganizer().getLastUpdate().getEventType().equals(ModelEvent.GAME_END)) {
            displayScoreBoard();
        }
    }

    /**
     * Displays the first screen and select the appropriate manager for the
     * {@link CliInput}. This method will be invoked in the main: the user
     * is not connected yet.
     */
    @Override
    public void displayLoginView() {
        output.printTextNewLine(
                "███████╗ █████╗  ██████╗ ██████╗  █████╗ ██████╗  █████╗ \n" +
                        "██╔════╝██╔══██╗██╔════╝ ██╔══██╗██╔══██╗██╔══██╗██╔══██╗\n" +
                        "███████╗███████║██║  ███╗██████╔╝███████║██║  ██║███████║\n" +
                        "╚════██║██╔══██║██║   ██║██╔══██╗██╔══██║██║  ██║██╔══██║\n" +
                        "███████║██║  ██║╚██████╔╝██║  ██║██║  ██║██████╔╝██║  ██║\n" +
                        "╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝\n");

        input.setManager(new LoginManager(view, output));
        input.getManager().showPrompt();
    }

    /**
     * Displays a wait message: this message is displayed after a correct connection
     * to the server and before the starting of the game.
     */
    @Override
    public void displayWaitMessage() {
        output.printTextNewLine("You're connected, wait for the game to start");
    }

    /**
     * Display the pattern's choice setting the correct manager for the {@link CliInput}:
     * it is invoked only when the game has been correctly set up.
     */
    @Override
    public void askPattern() {
        input.setManager(new PatternSelectionManager(view, output));
        input.getManager().showPrompt();
    }

    /**
     * Displays a wait message when the Game view is selected by the
     * {@link ClientView}. The user has correctly chosen his pattern
     * and has now to wait for the game to start.
     */
    @Override
    public void displayMultiPlayerGame() {
        output.printTextNewLine("Your pattern is:");
        output.printPattern(getPattern());
        output.printTextNewLine("Wait for the game to start");
    }

    /**
     * Sets the correct manager to display the general Game (Turn) view
     * when the game is actually begun. It is invoked during the regular development
     * of the central phase of the game.
     */
    private void displayTurnView() {
        input.setManager(new TurnHandlingManager(view, output));
        input.getManager().showPrompt();
    }

    /**
     * Displays an error message sent from the Model and restore the correct
     * view state.
     *
     * @param error The error message.
     */
    @Override
    public void displayError(String error) { //NB: è da qua che si resetta correttamente (anche gli handler devono essere resettati correttamente!)
        output.printTextNewLine(error);
        if (getDataOrganizer().getAllPlayerStatus().size() != 1) {//MP
            restoreDisplayMultiPlayer();
        } else {//SP
            restoreDisplaySinglePlayer();
        }
    }

    /**
     * Restores the correct View after showing an error in MultiPlayer mode.
     */
    private void restoreDisplayMultiPlayer() {
        //The first error that a player can commit is about the pattern's selection
        if (!view.isGameRunning()) {
            //If the pattern's choice is not correct, here it is redisplayed.
            askPattern();
            return;
        }
        if (view.isGameRunning()) {
            //If an error is made during the user's turn the Turn View is restored.
            displayTurnView();
        }
    }

    /**
     * Restores the correct View after showing an error in SinglePlayer mode.
     */
    private void restoreDisplaySinglePlayer() {
        if (!view.isGameRunning() && getDataOrganizer().getGameSetup() == null) {
            //The committed error is necessarily on the difficulty selection in this case.
            //The correct view is restored.
            askDifficulty();
            return;
        }
        if (!view.isGameRunning() && getDataOrganizer().getGameSetup() != null) {
            //The first error that a player can commit is about the pattern's selection
            //when the game is not started but the setup is complete.
            //If the pattern's choice is not correct, here it is redisplayed.
            askPattern();
            return;
        }
        if (view.isGameRunning()) {
            if (!view.isGameEndSinglePlayer()) {
                //If an error is made during the user's turn the Turn View is restored.
                displayTurnView();
            }
            //The committed error is necessarily on the Private Objective Card's choice
            //if the game is ended in Single Player mode. The correct view is restored.
            else askPrivateObjective();
        }
    }

    /**
     * Displays the final score board at the end of the game.
     */
    @Override
    public void displayScoreBoard() {
        input.setManager(null);//@TODO come si gestisce questa parte finale per il thread dell'input che continua a girare?
        input.setGameRunning(false);
        output.printTextNewLine("The game is finished");
        output.printScoreBoard(getDataOrganizer().getScoreBoard());
    }

    /**
     * Sets the correct manager for the selection of a die inserting
     * an appropriate input.
     * It is invoked when activating certain Tool Cards that request
     * the choice of a die by the user (Flux Brush).
     */
    @Override
    public void selectDie() {
        input.setManager(new SelectDieManager(view, output, input));
        input.getManager().showPrompt();
    }

    /**
     * Sets the correct manager when there is the possibility of moving a die
     * on the user's Pattern inserting an appropriate input. It is invoked by
     * the Tool Cards that allow to move dice on the Pattern.
     *
     * @param amount The maximum amount of dice to be moved.
     * @param moveAll Indicates whether the Tool Card forces to move exactly
     *                two dice ({@code true}) or if it allows to move at most
     *                two dice ({@code false}).
     */

    @Override
    public void moveDice(int amount, boolean moveAll) {
        input.setManager(new MoveDiceManager(view, output, input, amount, moveAll));
        input.getManager().showPrompt();
    }

    /**
     * Sets the correct manager to handle the possibility of swapping two dice
     * between the Draft Pool and the RoundTrack inserting an appropriate input.
     * It is invoked by the {@link it.polimi.se2018.controller.SwapDiceBehaviour},
     * it updates the dedicated view for it.
     */
    @Override
    public void askDiceToSwap() {
        input.setManager(new DiceSwappingManager(view, output, input));
        input.getManager().showPrompt();
    }

    /**
     * Selects the correct manager to handle the possibility of choosing a value
     * for a die drafted from the {@link DiceBag} and to put it on the user's Pattern
     * inserting an appropriate input.
     * It is invoked from the {@link it.polimi.se2018.controller.PullAgainAndPlaceBehaviour},
     * it updates the view for the second step of its usage.
     */
    @Override
    public void askValueDestination() {
        input.setManager(new ValueAndDestinationManager(view, output, input));
        input.getManager().showPrompt();
    }

    /**
     * Selects the correct manager for the selection of a die and the choice
     * about incrementing or decrementing its value inserting an appropriate input.
     * It is invoked by the {@link it.polimi.se2018.controller.AlterDieValueBehaviour},
     * it updates the dedicated view for it.
     */
    @Override
    public void askIncrement() {
        input.setManager(new DieIncrementManager(view, output, input));
        input.getManager().showPrompt();
    }


    /**
     * Selects the correct manager to handle the placement of a die through a
     * Tool Card usage ({@link it.polimi.se2018.controller.PlaceDieBehaviour}).
     */
    @Override
    public void askPlacement() {
        input.setManager(new ToolCardPlacementManager(view, output, input));
        input.getManager().showPrompt();
    }

    /**
     * Selects the correct manager to handle the confirm when activating a Tool Card
     * that will no ask for parameter
     * ({@link it.polimi.se2018.controller.ReRollDraftPoolBehaviour}).
     */
    @Override
    public void askConfirm() {
        input.setManager(new ToolCardConfirmManager(view, output, input));
        input.getManager().showPrompt();
    }

    /**
     * Displays a wait message when the Game view is selected by the
     * {@link ClientView}. The user has correctly chosen his pattern
     * and has now to wait for the game to start.
     */
    @Override
    public void displaySinglePlayerGame() {
        output.printTextNewLine("Your pattern is:");
        output.printPattern(getPattern());
        output.printTextNewLine("Wait for the game to start");
    }

    /**
     * Selects the correct manager to handle the possibility of choosing
     * the game's difficulty level in SinglePlayer mode inserting an appropriate input.
     */
    @Override
    public void askDifficulty() {
        input.setManager(new DifficultyManager(view, output));
        input.getManager().showPrompt();
    }

    /**
     * Selects the correct manager for the moment of choosing between the user's
     * two Private Objective Cards at the end of the game in Single Player mode
     * inserting an appropriate input.
     */
    @Override
    public void askPrivateObjective() {
        input.setManager(new PrivateObjectiveManager(view, output));
        input.getManager().showPrompt();
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
