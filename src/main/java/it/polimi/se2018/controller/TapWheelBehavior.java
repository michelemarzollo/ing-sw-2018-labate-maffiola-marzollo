package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.MoveTwoDice;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents the "Tap Wheel" tool card.
 * <p>The effect of this tool card is to move up to two dice of the same
 * colour of a die in the round track following all placement restrictions.</p>
 * <p>This tool card requires two pairs of source-destination coordinates
 * as user-given parameters to be applied.</p>
 */
public class TapWheelBehavior implements ToolCardBehavior {
    /**
     * Selects the view to let the user choose two dice.
     * @param message The message sent by the view.
     */
    @Override
    public void askParameters(ViewMessage message) {
        message.getView().showMoveSelection(2);
    }

    /**
     * Retrieves all dice located at the specified coordinates.
     * @param pattern The pattern containing the dice.
     * @param coordinates An array of coordinates at which dice
     *                    are placed.
     * @return A list of size {@code coordinates.length} with the retrieved
     * dice. When no die is found null is set at the corresponding index.
     */
    private List<Die> getDiceAt(Pattern pattern, Coordinates[] coordinates) {
        List<Die> selected = new ArrayList<>();
        Cell[][] grid = pattern.getGrid();
        for (Coordinates c : coordinates) {
            selected.add(
                    grid[c.getRow()][c.getCol()]
                            .getDie()
            );
        }
        return selected;
    }

    /**
     * Computes the list of colours contained in a matrix of dice.
     * @param matrix The matrix where the colours are extracted from.
     * @return A list containing all found colours without repetition.
     */
    private List<Colour> getColoursIn(List<List<Die>> matrix) {
        return matrix.stream()
                // generate a matrix of colours with no row repetition
                .map(row -> row.stream()
                        .map(Die::getColour)
                        .distinct()
                        .collect(Collectors.toList()))
                // reduce to a list of unique colours
                .reduce(new ArrayList<>(), (list, colours) -> {
                    // for each colour, add only if not already present in list
                    colours.stream()
                            .filter(list::contains)
                            .forEach(list::add);
                    return list;
                });
    }

    /**
     * Checks if {@code message} is semantically correct.
     * <p>To be correct the message requires the size of the source array
     * and destination array to be equal and within range [1, 2].</p>
     * @param message The message to be checked.
     * @return {@code true} if the message is correct; {@code false} otherwise.
     */
    private boolean checkAmounts(MoveTwoDice message) {
        int sourcesNum = message.getSources().length;
        int destinationNum = message.getDestinations().length;

        if (sourcesNum != destinationNum) {
            message.getView()
                    .showError("The number of sources and destinations don't match.");
            return false;
        }

        if (sourcesNum < 1 || sourcesNum > 2) {
            message.getView().showError("Bad selection: invalid amount of dice.");
            return false;
        }

        return true;

    }

    /**
     * Checks if the selected dice are of a valid colour.
     * <p>The dice in the provided list must be of the same colour, first of all.
     * Also another die of their colour must be present in the round track.</p>
     * @param selection The dice to be checked.
     * @param roundTrack The round track leftover dice.
     * @return {@code true} if the selected die can be moved according to the rules;
     * {@code false} otherwise.
     */
    private boolean isSelectionValid(List<Die> selection, List<List<Die>> roundTrack) {
        List<Colour> selectionColours = selection.stream()
                .map(Die::getColour)
                .distinct()
                .collect(Collectors.toList());

        boolean sameColour = selectionColours.size() == 1;
        boolean colourInRoundTrack = getColoursIn(roundTrack)
                .containsAll(selectionColours);

        return sameColour && colourInRoundTrack;
    }

    /**
     * Moves up to two dice of the same colour of one die in the round track.
     * @param game The game the effect has to be applied to.
     * @param message The message sent by th view.
     */
    @Override
    public void useToolCard(Game game, ViewMessage message) {
        MoveTwoDice moveTwoDice = (MoveTwoDice) message;

        if (!checkAmounts(moveTwoDice))
            return;

        String playerName = moveTwoDice.getPlayerName();
        Optional<Player> maybePlayer = game.getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();
        if (maybePlayer.isPresent()) {
            Player player = maybePlayer.get();
            Pattern pattern = player.getPattern();
            List<Die> selected = getDiceAt(pattern, moveTwoDice.getSources());

            if (!isSelectionValid(selected, game.getRoundTrack().getLeftovers())) {
                moveTwoDice.getView().showError("Invalid selection: bad colours!");
                return;
            }

            try {
                Pattern newPattern = pattern.moveDice(moveTwoDice.getSources(), moveTwoDice.getDestinations());
                player.setPattern(newPattern);
            } catch (PlacementErrorException e) {
                moveTwoDice.getView().showError("Bad selection: cannot move");
            }

        } else {
            moveTwoDice.getView().showError("Invalid player!");
        }

    }
}
