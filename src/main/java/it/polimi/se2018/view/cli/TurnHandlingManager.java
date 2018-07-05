package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

import java.util.ArrayList;
import java.util.List;

/**
 * Input manager used to let the user select the available options during his turn.
 */
public class TurnHandlingManager extends InputEventManager {

    /**
     * Prompt messages for all the possible options.
     */
    private static final String PLACE_DIE_PROMPT = "place a die";
    private static final String TOOL_CARD_PROMPT = "use a tool card";
    private static final String END_TURN_PROMPT = "end current turn";
    private static final String GAME_STATUS_PROMPT = "show game status";

    /**
     * This manager has a subHandler for each of the four actions the user
     * can choose to perform: place a die, use a Tool Card, ask for information,
     * skip the turn.
     */
    private InputEventManager subHandler;

    /**
     * The list of available options.
     */
    private List<Option> availableOptions = new ArrayList<>();

    /**
     * Constructor of the class.
     *
     * @param view   The view to which this manager is bounded.
     * @param output The output destination where the prompts of this manager
     *               are shown.
     */
    public TurnHandlingManager(ClientView view, CliPrinter output) {
        super(view, output);
    }

    /**
     * Refills the list of available options according to the current game status.
     */
    private void refillOptionMap() {
        availableOptions.clear();
        if (!hasPlacedDie())
            addOption(
                    PLACE_DIE_PROMPT,
                    () -> setSubHandler(new DiePlacementHandler(getView(), getOutput(), this)));

        if (!hasUsedToolCard())
            addOption(
                    TOOL_CARD_PROMPT,
                    () -> setSubHandler(new ToolCardHandler(getView(), getOutput(), this))
            );

        addOption(
                END_TURN_PROMPT,
                () -> setSubHandler(new SkipTurnHandler(getView(), getOutput(), this))
        );
        addOption(
                GAME_STATUS_PROMPT,
                () -> setSubHandler(new GameStatusHandler(getView(), getOutput(), this))
        );
    }

    /**
     * Inserts an entry to the available options list.
     *
     * @param prompt  The prompt message.
     * @param handler The handler to be used to process input.
     */
    private void addOption(String prompt, Runnable handler) {
        int index = availableOptions.size() + 1;
        availableOptions.add(new Option(
                index + ". " + prompt,
                handler
        ));
    }

    /**
     * Handles the input entered by the user.
     * <p>After collecting the user choice, sets the sub-handler to the requested one and
     * delegates it for the following input handling.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        if (isNotMyTurn())
            return;

        if (subHandler != null)
            subHandler.handle(input);
        else {
            try {
                int choice = Integer.parseUnsignedInt(input) - 1;
                availableOptions.get(choice).getHandler().run();
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                showError();
            }
        }
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        if (isNotMyTurn()) {
            if (getDataOrganizer().getNextTurn() == null)
                getOutput().println("\nWaiting other players...");
            else
                getOutput().println("\nIt's " + getDataOrganizer().getNextTurn().getPlayerName() + "'s turn, please wait");
            return;
        }
        if (subHandler != null)
            subHandler.showPrompt();
        else {
            refillOptionMap();
            String turnNumber
                    = getDataOrganizer().getNextTurn().isSecondTurnAvailable() ? "T1" : "T2";
            getOutput().println("\n\nIt's your turn (" + turnNumber + "), choose between: ");
            availableOptions.forEach(o -> getOutput().println(o.getPrompt()));
        }
    }

    /**
     * Resets the input manager.
     */
    @Override
    public void reset() {
        setSubHandler(null);
    }

    /**
     * Helper method to verify if it is the user's turn.
     *
     * @return {@code true} if now it is the user's turn, {@code false}
     * otherwise.
     */
    private boolean isNotMyTurn() {
        if (getDataOrganizer().getNextTurn() == null)
            return true;
        return !getView().getPlayerName().equals(getDataOrganizer().getNextTurn().getPlayerName());
    }

    /**
     * Setter for the sub-handler.
     *
     * @param subHandler The subHandler for turn handling.
     */
    private void setSubHandler(InputEventManager subHandler) {
        this.subHandler = subHandler;
    }

}

