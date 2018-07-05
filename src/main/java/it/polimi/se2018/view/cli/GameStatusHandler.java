package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.ClientView;

import java.util.Arrays;
import java.util.List;


/**
 * Input handler used to show the current game status.
 *
 * @author giorgiolabate
 */
public class GameStatusHandler extends InputEventManager {

    /**
     * Prompt messages for the possible options.
     */
    private static final String PATTERN_PROMPT = "1. patterns";
    private static final String DRAFT_POOL_PROMPT = "2. draft pool";
    private static final String ROUND_TRACK_PROMPT = "3. round track";
    private static final String PRIVATE_OBJ_PROMPT = "4. private objective card";
    private static final String PUBLIC_OBJ_PROMPT = "5. public objective cards";
    private static final String TOOL_CARD_PROMPT = "6. tool cards";

    /**
     * Reference to the manager of the turn. It's used to set the
     * subHandler to null when the task has been accomplished.
     */
    private final TurnHandlingManager manager;

    /**
     * The list of options.
     */
    private final List<Option> options = Arrays.asList(
            new Option(PATTERN_PROMPT, this::handlePatterns),
            new Option(DRAFT_POOL_PROMPT,
                    () -> getOutput().printDraftPool(getDraftPool())),
            new Option(ROUND_TRACK_PROMPT,
                    () -> getOutput().printRoundTrack(getRoundTrack())),
            new Option(PRIVATE_OBJ_PROMPT,
                    () -> getOutput().printPrivateObjectiveCards(getPrivateCards())),
            new Option(PUBLIC_OBJ_PROMPT,
                    () -> getOutput().printPublicObjectiveCards(getPublicCards())),
            new Option(TOOL_CARD_PROMPT,
                    () -> getOutput().printToolCards(getToolCards(), getUsedToolCards()))
    );

    /**
     * Constructor of the class.
     *
     * @param view    The view to which this manager is bounded.
     * @param output  The output destination where the prompts of this manager
     *                are shown.
     * @param manager The TurnHandlingManager.
     */
    public GameStatusHandler(ClientView view, CliPrinter output, TurnHandlingManager manager) {
        super(view, output);
        this.manager = manager;
    }

    /**
     * Handles the input entered by the user.
     * <p>After collecting the player choice, prints the requested information.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        try {
            int choice = Integer.parseUnsignedInt(input.trim());

            options.get(choice - 1).getHandler().run();
            manager.reset();

        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            showError();
        }
    }

    /**
     * Prints the patter according to game mode.
     */
    private void handlePatterns() {
        if (!isSinglePlayer())
            getOutput().printPatterns(getPlayers());
        else
            getOutput().printPatternLarger(getPattern());
    }

    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {
        getOutput().println("\nWhat do you want to see? Enter");
        options.forEach(o -> getOutput().println(o.getPrompt()));
    }
}
