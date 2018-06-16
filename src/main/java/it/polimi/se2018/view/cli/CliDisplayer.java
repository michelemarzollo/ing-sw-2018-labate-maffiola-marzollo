package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.*;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class displays the game through the command line interface. It is used
 * by the View to display the game on CLI.
 */
public class CliDisplayer implements Displayer {

    /**
     * The input gatherer that scan the user input and passes it
     * to the CliDisplayer.
     */
    private CliInput input;

    /**
     * The 'printer' through which the CliDisplayer displays the game on a
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
     * @param inputStream The {@link InputStream} from which the user's input will
     *                    be gathered.
     * @param output The {@link PrintStream} on which the game will be displayed.
     */
    private CliDisplayer(InputStream inputStream, PrintStream output) {
        this.input = new CliInput(inputStream);
        this.output = new CliImagePrinter(output);
        callback.accept(this);
    }

    /**
     * Launches the cli.
     * @param in The stream used for input.
     * @param out The stream used for output.
     * @param callback The callback function to register the displayer.
     */
    public static void launchCli(InputStream in, PrintStream out, Consumer<Displayer> callback){
        CliDisplayer.callback = callback;
        new CliDisplayer(in, out);
    }

    /**
     * Getter for the {@link ClientView} to which the CliDisplayer is bound.
     * @return the {@link ClientView} to which the CliDisplayer is associated.
     */
    public ClientView getView() {
        return view;
    }

    @Override
    public void askPlacement() {
        //TODO
    }

    @Override
    public void askConfirm() {
        //TODO
    }

    /**
     * Getter for the {@link ViewDataOrganizer} associated to the CliDisplayer.
     * @return the {@link ViewDataOrganizer} to which the CliDisplayer refers.
     */
    @Override
    public ViewDataOrganizer getDataOrganizer() {
        return view.getDataOrganizer();
    }

    /**
     * Setter for the {@link ClientView}
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
        if(getDataOrganizer().getLastUpdate().getEventType().equals(ModelEvent.PLAYER_CONNECTION_STATUS)){
            PlayerConnectionStatus connectionStatus = (PlayerConnectionStatus) getDataOrganizer().getLastUpdate();
            if(connectionStatus.isConnected()){
                output.printTextNewLine(connectionStatus.getPlayerName() + "reconnected!");
            }
            else{
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
     * Displays the first screen: it is the Login view that
     * allows the user to choose the type of connection, the game mode
     * and his username. This method will be invoked in the main: the user
     * is not connected yet.
     */
    @Override
    public void displayLoginView() { //verrà chiamata da qualche parte nel main, non si è ancora connesso il client
        output.printTextNewLine(
                "███████╗ █████╗  ██████╗ ██████╗  █████╗ ██████╗  █████╗ \n" +
                "██╔════╝██╔══██╗██╔════╝ ██╔══██╗██╔══██╗██╔══██╗██╔══██╗\n" +
                "███████╗███████║██║  ███╗██████╔╝███████║██║  ██║███████║\n" +
                "╚════██║██╔══██║██║   ██║██╔══██╗██╔══██║██║  ██║██╔══██║\n" +
                "███████║██║  ██║╚██████╔╝██║  ██║██║  ██║██████╔╝██║  ██║\n" +
                "╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝\n");
        int connection;
        int gameMode;
        String userName;
        output.printTextNewLine("Choose the type of connection, enter: \n" +
                "1 for SOCKET\n" +
                "2 for RMI");
        connection = input.readInputInt();
        output.printTextNewLine("Choose the game mode, enter: \n" +
                "1 for Single player\n" +
                "2 for Multiplayer");
        gameMode = input.readInputInt();
        output.printTextNewLine("Enter your username:");
        userName = input.readInputString();
        if (connection == 1) {
            //@TODO retrieve server address and port from config
            String serverAddress = null;
            int port = 0;
            view.handleLogin(userName, serverAddress, port); //@TODO add a parameter for the gameMode
        } else {
            if (connection == 2) {
                String serverAddress = null;
                String serviceName = null;
                view.handleLogin(userName, serverAddress, serviceName); //a paramater for the gameMode have to be added
            } else {
                output.printTextNewLine("Invalid choice");
                displayLoginView();
            }
        }
    }

    /**
     * Displays a wait message: this message is displayed after a correct connection
     * to the server and before the starting of the game.
     */
    @Override
    public void displayWaitMessage(){
        output.printTextNewLine("You're connected, wait for the game to start");
    }

    /**
     * Displays the pattern's choice: it is invoked only when the game has been
     * correctly set up.
     */
    @Override
    public void askPattern() {
        String pattern;
        int playerIndex = findPlayerIndex(view.getPlayerName(), getDataOrganizer().getGameSetup().getPlayers());
        output.printPatternSelection(getDataOrganizer().getGameSetup().getCandidates()[playerIndex]);
        output.printTextNewLine("Enter the name of the pattern you want to choose:");
        pattern = input.readInputString();
        view.handlePatternSelection(pattern);
    }

    /**
     * Helper for the askPattern to display to the player the correct candidates and
     * for the getPrivateCards to retrieve the correct Private Objective Cards
     * of the user.
     * The index that the player occupies among the {@link GameSetup} players
     * corresponds to the index where his candidates are in the same message and
     * where his Private Objective Cards are: this method find that index.
     * @param name The player's name.
     * @param names The array of player's names.
     * @return The index where to find {@code name} among {@code names}.
     */
    private int findPlayerIndex(String name, String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i])) return i;
        }
        return 0; //non capita mai
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
     * Displays the general Game (Turn) view when the game is actually begun. It is
     * displayed during the regular development of the central phase of the game.
     */
    private void displayTurnView() {
        int choice;
        if (isMyTurn() && noMoveYet()) {
            output.printTextNewLine("It's your turn, enter:\n "
                    + "1 to place a die\n"
                    + "2 to use a tool card\n"
                    + "3 to skip your turn\n"
                    + "4 to see the game status");
            choice = input.readInputInt();
            handleFirstAction(choice);
            return;
        }
        if (isMyTurn() && !noMoveYet()) {
            if (getDataOrganizer().getNextTurn().isAlreadyPlacedDie()) {
                output.printTextNewLine("It's your turn, you have already placed a die, enter:\n"
                        + "1 to use a tool card\n"
                        + "2 to skip your turn\n"
                        + "3 to see the game status");
                choice = input.readInputInt();
                handleSecondActionCard(choice);
            } else {
                output.printTextNewLine("It's your turn, you have already used a tool card, enter:\n"
                        + "1 to place a die\n"
                        + "2 to skip your turn\n"
                        + "3 to see the game status");
                choice = input.readInputInt();
                handleSecondActionDie(choice);
            }
            return;
        }
        if (!isMyTurn()) {
            output.printTextNewLine("It's " + getDataOrganizer().getNextTurn().getPlayerName() + "'s turn, please wait");
        }
    }

    /**
     * Helper method to verify if it is the user's turn.
     * @return {@code true} if now it is the user's turn, {@code false}
     * otherwise.
     */
    private boolean isMyTurn() {
        return view.getPlayerName().equals(getDataOrganizer().getNextTurn().getPlayerName());
    }

    /**
     * Helper method to verify if the player whose turn is now, has not
     * done any move yet.
     * @return {@code true} if the player has done nothing in his turn yet,
     * {@code false} otherwise.
     */
    private boolean noMoveYet() {
        return !getDataOrganizer().getNextTurn().isAlreadyPlacedDie() &&
                !getDataOrganizer().getNextTurn().isAlreadyUsedToolCard();
    }

    /**
     * Handle the first move of the user.
     * @param command The action selected by the user between all the possible
     *                first actions.
     */
    private void handleFirstAction(int command) {
        switch (command) {
            case 1:
                handlePlacementSelection();
                break;
            case 2:
                handleCardSelection();
                break;
            case 3:
                handleEndTurnSelection();
                break;
            case 4:
                handleGameStatusSelection();
                break;
            default:
                goBackToTurnView();
                break;
        }
    }

    /**
     * Displays an error message when the user selects an invalid action in his turn
     * and displays again the general turn view.
     */
    private void goBackToTurnView() {
        output.printTextNewLine("Invalid input");
        displayTurnView();
    }

    /**
     * Handles the case in which the user decided to place a die in his turn.
     */
    private void handlePlacementSelection() {
        int choice;
        output.printTextNewLine("You chose to place a die, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
        choice = input.readInputInt();
        if (choice == 1) {
            output.printDraftPool(getDraftPool());
            output.printTextNewLine("Enter the index of the die:");
            int index = input.readInputInt();
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the coordinates for your placement:");
            Coordinates coordinates = readCoordinates();
            view.handlePlacement(index, coordinates);
        } else goBackToTurnView();

    }

    /**
     * Handles the case in which the user decided to use a Tool Card in his turn.
     * It is different between SinglePlayer and in MultiPlayer mode.
     */
    private void handleCardSelection() {
        int choice;
        output.printText("You chose to use a Tool Card, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
        choice = input.readInputInt();
        if (choice == 1) {
            output.printToolCards(getToolCards());
            output.printTextNewLine("Enter the name of the Tool Card:");
            String name;
            name = input.readInputString();
            if (getDataOrganizer().getAllPlayerStatus().size() != 1) { //MultiPlayer
                view.handleToolCardSelection(name);
            } else { //SinglePlayer
                int dieIndex;
                output.printDraftPool(getDraftPool());
                output.printTextNewLine("Enter the index of the die you want to spend to use the tool card:");
                dieIndex = input.readInputInt();
                view.handleToolCardSelection(name, dieIndex);
            }
        } 
        else goBackToTurnView();
    }

    /**
     * Handles the case in which the user decided to skip his turn.
     */
    private void handleEndTurnSelection() {
        int choice;
        output.printTextNewLine("You chose to skip your turn, enter:\n" +
                "1 to confirm\n" +
                "Any other number to go back");
        choice = input.readInputInt();
        if (choice == 1) view.handleEndTurn();
        else goBackToTurnView();
    }

    /**
     * Displays the possibility to insert the coordinates for an action requested
     * by the user (ex: placing a Die, using some Tool Card's effect...)
     * @return The inserted coordinates
     */
    private Coordinates readCoordinates() {
        int row;
        int col;
        output.printText("Row (starting from 0): ");
        row = input.readInputInt();
        output.printText("Column (starting from 0): ");
        col = input.readInputInt();
        return new Coordinates(row, col);
    }

    /**
     * Handles the case in which the user decided to see the game status.
     */
    private void handleGameStatusSelection() {
        int choice;
        output.printTextNewLine("What do you want to see? Enter\n" +
                "1 for Patterns\n" +
                "2 for Round Track\n" +
                "3 for Private Objective Cards\n" +
                "4 for Public Objective Cards\n" +
                "5 for Tool Cards"
        );
        choice = input.readInputInt();
        handleInformationSelection(choice);
    }

    /**
     * Handles the user's specific request on the game status.
     * @param choice The action selected by the user between all the possible
     *               actions that indicates what he wants to see about the game
     *               status.
     */
    private void handleInformationSelection(int choice) {
        switch (choice) {
            case 1:
                //MP
                if(getDataOrganizer().getAllPlayerStatus().size() != 1)  output.printPatterns(getPlayers());
                //SP
                else output.printPattern(getPattern());
                displayTurnView();
                break;
            case 2:
                output.printRoundTrack(getRoundTrack());
                displayTurnView();
                break;
            case 3:
                output.printPrivateObjectiveCards(getPrivateCards());
                displayTurnView();
                break;
            case 4:
                output.printPublicObjectiveCards(getPublicCards());
                displayTurnView();
                break;
            case 5:
                output.printToolCards(getToolCards());
                displayTurnView();
                break;
            default:
                goBackToTurnView();
                break;
        }

    }

    /**
     * Handle the second move of the user when he has already placed a die.
     * @param command The action selected by the user between all the possible
     *                actions left.
     */
    private void handleSecondActionCard(int command) {
        switch (command) {
            case 1:
                handleCardSelection();
                break;
            case 2:
                handleEndTurnSelection();
                break;
            case 3:
                handleGameStatusSelection();
                break;
            default:
                goBackToTurnView();
                break;
        }
    }

    /**
     * Handle the second move of the user when he has already used a Tool Card.
     * @param command The action selected by the user between all the possible
     *                actions left.
     */
    private void handleSecondActionDie(int command) {
        switch (command) {
            case 1:
                handlePlacementSelection();
                break;
            case 2:
                handleEndTurnSelection();
                break;
            case 3:
                handleGameStatusSelection();
                break;
            default:
                goBackToTurnView();
                break;
        }
    }

    /**
     * Displays an error message sent from the Model and redisplay the correct
     * view.
     * @param error The error message.
     */
    @Override
    public void displayError(String error) {
        output.printTextNewLine(error);
        if (getDataOrganizer().getAllPlayerStatus().size() != 1) {//MP
            restoreDisplayMultiPlayer();
        } else {//SP
            restoreDisplaySinglePlayer();
        }
    }

    /**
     * Redisplay the correct View after showing an error in MultiPlayer mode.
     */
    private void restoreDisplayMultiPlayer(){
        //The first error that a player can commit is about the pattern's selection
        if (!view.isGameRunning()) {
            //If the pattern's choice is not correct, here it is redisplayed.
            askPattern();
            return;
        }
        if (view.isGameRunning() && isMyTurn()) {
            //If an error is made during the user's turn the Turn View is redisplayed.
            displayTurnView();
        }
    }

    /**
     * Redisplay the correct View after showing an error in SinglePlayer mode.
     */
    private void restoreDisplaySinglePlayer(){
        if (!view.isGameRunning() && getDataOrganizer().getGameSetup() == null) {
            //The committed error is necessarily on the difficulty selection in this case.
            //The correct view is redisplayed.
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
            if(!view.isGameEndSinglePlayer()) {
                //If an error is made during the user's turn the Turn View is redisplayed.
                displayTurnView();
            }
            //The committed error is necessarily on the Private Objective Card's choice
            //if the game is ended in Single Player mode. The correct view is redisplayed.
            else askPrivateObjective();
        }
    }

    /**
     * Displays the final score board at the end of the game.
     */
    @Override
    public void displayScoreBoard() {
        output.printTextNewLine("The game is finished");
        output.printScoreBoard(getDataOrganizer().getScoreBoard());
    }

    /**
     * Displays the selection of a die inserting an appropriate input.
     * It is invoked when activating certain Tool Cards that request
     * the choice of a die by the user.
     */
    @Override
    public void selectDie() {
        output.printDraftPool(getDraftPool());
        output.printTextNewLine("Enter the index of the die you wish to use: \n");
        view.handleToolCardUsage(input.readInputInt());
    }

    /**
     * Displays the possibility of moving a die on the user's Pattern inserting
     * an appropriate input. It is invoked by certain Tool Cards that allow to
     * move dice on the Pattern.
     * @param amount The maximum amount of dice to be moved.
     */
    @Override
    public void moveDice(int amount, boolean moveAll) {
        output.printPattern(getPattern());
        if (amount == 1) {
            Coordinates source;
            Coordinates destination;
            output.printTextNewLine("Enter the source coordinates:");
            source = readCoordinates();
            output.printTextNewLine("Enter the destination coordinates:");
            destination = readCoordinates();
            view.handleToolCardUsage(source, destination);
            return;
        }
        if (amount == 2) {
            Coordinates[] sources = new Coordinates[2];
            Coordinates[] destinations = new Coordinates[2];
            output.printTextNewLine("Enter the first source coordinates:");
            sources[0] = readCoordinates();
            output.printTextNewLine("Enter the first destination coordinates:");
            destinations[0] = readCoordinates();
            output.printPattern(getPattern());
            output.printTextNewLine("Enter the second source coordinates:");
            sources[1] = readCoordinates();
            output.printTextNewLine("Enter the second destination coordinates:");
            destinations[1] = readCoordinates();
            view.handleToolCardUsage(sources, destinations);
        }
    }

    /**
     * Displays the possibility of swapping two dice between the Draft Pool
     * and the RoundTrack inserting an appropriate input. It is invoked
     * by the {@link it.polimi.se2018.controller.LensCutterBehaviour}, it
     * is the dedicated view for it.
     */
    @Override
    public void askDiceToSwap() {
        int index;
        Coordinates coordinates;
        output.printDraftPool(getDraftPool());
        output.printRoundTrack(getRoundTrack());
        output.printTextNewLine("Enter the index of the die in the Draft Pool you want to swap:");
        index = input.readInputInt();
        output.printTextNewLine("Enter the coordinates of the die in the Round Track you want to swap:");
        coordinates = readCoordinates();
        view.handleToolCardUsage(index, coordinates);

    }

    /**
     * Displays the possibility of choosing a value for a die drafted from the
     * {@link DiceBag} and to put it on the user's Pattern inserting an appropriate input.
     * It is invoked from the {@link it.polimi.se2018.controller.FluxRemoverBehaviour}, it
     * is the dedicated view for the second step of its usage.
     */
    @Override
    public void askValueDestination() {
        int value;
        Coordinates coordinates;
        output.printPattern(getPattern());
        output.printDraftPool(getDraftPool());
        output.printTextNewLine("The index of your chosen die in the draft pool is" +
                getDataOrganizer().getNextTurn().getForcedSelectionIndex());
        output.printTextNewLine("Enter the value you want to assign to the chosen die:");
        value = input.readInputInt();
        output.printTextNewLine("Enter the coordinates of the space in your pattern where you want to place the die:");
        coordinates = readCoordinates();
        view.handleToolCardUsage(value, coordinates);
    }

    /**
     * Displays the selection of a die and the choice about incrementin or
     * decrementing its value inserting an appropriate input.
     * It is invoked by the {@link it.polimi.se2018.controller.GrozingPliersBehaviour},
     * it is the dedicated view for it.
     */
    @Override
    public void askIncrement() {
        int index;
        int choice;
        output.printDraftPool(getDraftPool());
        output.printTextNewLine("Enter the index of the die you wish to use: \n");
        index = input.readInputInt();
        output.printTextNewLine("Enter 1 to increment the value of the selected die, 2 to decrement it:");
        choice = input.readInputInt();
        if(choice == 1){
            view.handleToolCardUsage(index, true);
            return;
        }
        if(choice == 2){
            view.handleToolCardUsage(index, false);
            return;
        }
        output.printTextNewLine("Invalid input!");
        askIncrement();
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
     * Displays the possibility of choosing the game's difficulty level in
     * SinglePlayer mode inserting an appropriate input.
     */
    @Override
    public void askDifficulty() {
        int difficulty;
        output.printTextNewLine("Choose the level of difficulty you wish to play, Enter a value between 1 and 5:");
        difficulty = input.readInputInt();
        view.handleDifficultySelection(difficulty);
    }

    /**
     * Displays the possibility of choosing between the user's two Private Objective Cards
     * at the end of the game in Single Player mode inserting an appropriate input.
     */
    @Override
    public void askPrivateObjective() {
        String cardName;
        output.printPrivateObjectiveCards(getPrivateCards());
        output.printTextNewLine("Enter the name of the private objective card you want to choose:");
        cardName = input.readInputString();
        view.handlePrivateSelection(cardName);
    }

    /**
     * Getter for the players' status in the game.
     * @return a List of {@link PlayerStatus} that represent all the players
     * in the game.
     */
    private List<PlayerStatus> getPlayers(){
        return getDataOrganizer().getAllPlayerStatus();
    }

    /**
     * Getter for the Public Objective Cards in the game.
     * @return an array of the Public Objective Cards in the game.
     */
    private PublicObjectiveCard[] getPublicCards() {
        return getDataOrganizer().getGameSetup().getPublicObjectives();
    }

    /**
     * Getter for the Private Objective Cards of the user.
     * @return an array of the user's Private Objective Cards.
     */
    private PrivateObjectiveCard[] getPrivateCards() {
        int playerIndex = findPlayerIndex(view.getPlayerName(), getDataOrganizer().getGameSetup().getPlayers());
        return getDataOrganizer().getGameSetup().getPrivateObjectives()[playerIndex];
    }

    /**
     * Getter for the Tool Cards in the game.
     * @return an array of the Tool Cards in the game.
     */
    private ToolCard[] getToolCards() {
        return getDataOrganizer().getGameSetup().getToolCards();
    }

    /**
     * Getter for the user's chosen Pattern.
     * @return The Pattern chosen from the user at the beginning of the game.
     */
    private Pattern getPattern() {
        return getDataOrganizer().getPlayerStatus(view.getPlayerName()).getPattern();
    }

    /**
     * Getter for the Round Track of the game.
     * @return a List of Lists of Dice that represents the Round Track.
     */
    private List<List<Die>> getRoundTrack(){
        return getDataOrganizer().getRoundTrack();
    }

    /**
     * getter for the Draft Pool of the game.
     * @return a List of dice that represents the Draft Pool.
     */
    private List<Die> getDraftPool(){
        return getDataOrganizer().getDraftPool();
    }

}
