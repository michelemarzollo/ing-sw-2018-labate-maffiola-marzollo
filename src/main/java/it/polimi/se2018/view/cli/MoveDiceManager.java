package it.polimi.se2018.view.cli;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

import java.util.ArrayList;
import java.util.List;

/**
 * Input manager used to move dice on player's pattern.
 */
public class MoveDiceManager extends InputEventManager {

    /**
     * Indicates the number of dice that have to be moved.
     */
    private final int amount;

    /**
     * Indicates whether it is mandatory to move both dice (in the
     * case in which the {@code amount} is equals to two) or not.
     */
    private final boolean moveAll;

    /**
     * Indicates whether the user has chosen to move both dice
     * (if he has this possibility) or not.
     */
    private boolean hasConfirmed;

    /**
     * List of inserted source coordinates.
     */
    private List<Coordinates> sources = new ArrayList<>();
    /**
     * List of inserted destination coordinates.
     */
    private List<Coordinates> destinations = new ArrayList<>();
    /**
     * The last inserted row index.
     */
    private int lastRow = -1;


    /**
     * Constructor of the class.
     *
     * @param view    The view to which this manager is bounded.
     * @param output  The output destination where the prompts of this manager
     *                are shown.
     * @param amount  The amount of dice to move.
     * @param moveAll {@code true} if exactly {@code amount} dice have to be moved;
     *                {@code false} otherwise.
     */
    public MoveDiceManager(ClientView view, CliPrinter output,
                           int amount, boolean moveAll) {
        super(view, output);
        this.amount = amount;
        this.moveAll = moveAll;
    }


    /**
     * Handles the input entered by the user.
     * <p>After collecting user confirmation, and the required amount of source
     * and destination coordinates, calls the proper handler in {@link ClientView}.</p>
     *
     * @param input The String inserted by the user.
     */
    @Override
    public void handle(String input) {
        int choice;
        try {
            choice = Integer.parseUnsignedInt(input);
        } catch (NumberFormatException e) {
            showError();
            return;
        }

        if (isConfirm()) {
            handleConfirm(choice);
            return;
        }

        if (lastRow == -1) {
            lastRow = choice;
        } else if (sources.size() == destinations.size()) {
            sources.add(new Coordinates(lastRow, choice));
            lastRow = -1;
        } else {
            destinations.add(new Coordinates(lastRow, choice));
            lastRow = -1;
        }

        if (destinations.size() == amount)
            getView().handleToolCardUsage(
                    sources.toArray(new Coordinates[0]),
                    destinations.toArray(new Coordinates[0]));

    }

    /**
     * Tells if the next input will be a continue confirmation.
     *
     * @return {@code true} if the next input will be a continue confirmation;
     * {@code false} otherwise.
     */
    private boolean isConfirm() {
        return !hasConfirmed && !moveAll && !sources.isEmpty()
                && sources.size() == destinations.size() && sources.size() != amount;
    }

    /**
     * Handles the confirm in the case in which is it possible to move several dice.
     *
     * @param choice The choice inserted by the user.
     */
    private void handleConfirm(int choice) {
        if (choice == 1)
            setHasConfirmed();

        else if (choice == 0)
            getView().handleToolCardUsage(
                    sources.toArray(new Coordinates[0]),
                    destinations.toArray(new Coordinates[0]));
    }


    /**
     * Shows the correct textual messages to the player in this phase
     * according to what he can do.
     */
    @Override
    public void showPrompt() {

        if (isConfirm()) {
            getOutput().println("Do you want to move another die? Enter 1 if so, 0 otherwise");
            return;
        }

        getOutput().print("Insert the ");
        if (lastRow == -1) {
            getOutput().printPatternLarger(getPattern());
            String target = (sources.size() == destinations.size()) ? "source" : "destination";
            getOutput().print((destinations.size() + 1) + ". " + target + " row: ");
        } else if (sources.size() == destinations.size()) {
            getOutput().print((sources.size() + 1) + ". source column: ");
        } else {
            getOutput().print((destinations.size() + 1) + ". destination column: ");
        }
    }

    /**
     * Sets the confirmation flag.
     */
    private void setHasConfirmed() {
        this.hasConfirmed = true;
    }
}
