package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.PlayerStatus;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains all the necessary print methods for the {@link CliDisplayer}
 * and directs them to the indicated {@link PrintStream}.
 * The print methods are used to print the Model's elements on a command line
 * environment.
 */
public class CliPrinter {

    /**
     * The stream to which the prints are directed. The print methods are
     * invoked on this stream.
     */
    private PrintStream stream;

    /**
     * Constructor of the class.
     *
     * @param stream The stream to which the prints are directed.
     */
    public CliPrinter(PrintStream stream) {
        this.stream = stream;
    }

    private void printName(String name) {
        println("Name: " + name);
    }

    private void printDescription(String description){
        println("Description: " + description);
    }

    public void printHeader() {
        println(
                "███████╗ █████╗  ██████╗ ██████╗  █████╗ ██████╗  █████╗ \n" +
                        "██╔════╝██╔══██╗██╔════╝ ██╔══██╗██╔══██╗██╔══██╗██╔══██╗\n" +
                        "███████╗███████║██║  ███╗██████╔╝███████║██║  ██║███████║\n" +
                        "╚════██║██╔══██║██║   ██║██╔══██╗██╔══██║██║  ██║██╔══██║\n" +
                        "███████║██║  ██║╚██████╔╝██║  ██║██║  ██║██████╔╝██║  ██║\n" +
                        "╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝\n");
    }

    /**
     * Prints a textual message starting a new line.
     *
     * @param string The textual message that has to be displayed.
     */
    public void println(String string) {
        stream.println(string);
    }

    /**
     * Prints a textual message without starting a new line.
     *
     * @param string The textual message that has to be displayed.
     */
    public void print(String string) {
        stream.print(string);
    }

    /**
     * Prints a colour showing its first character.
     *
     * @param colour The colour that has to be displayed
     */
    private void printColour(Colour colour) {
        if (colour == null) {
            return;
        }
        stream.print(String.valueOf(colour.toString().charAt(0)));
    }

    /**
     * Prints an integer value.
     *
     * @param value The integer that has to be displayed.
     */
    private void printValue(int value) {
        if (value == 0) {
            return;
        }
        stream.print(value);
    }

    /**
     * Prints a die as its value and its colour concatenated.
     *
     * @param die The die that has to be displayed.
     */
    private void printDie(Die die) {
        printValue(die.getValue());
        printColour(die.getColour());
    }

    /**
     * Prints a cell of a {@link Pattern}.
     *
     * @param cell The cell that has to be displayed.
     */
    private void printCell(Cell cell) {
        if (cell.getDie() != null) {
            printDie(cell.getDie());
            stream.print(" ");
        } else {
            if (cell.getColour() == null && cell.getValue() == 0) {
                stream.print("   ");
                return;
            }
            if (cell.getValue() != 0) {
                printValue(cell.getValue());
            }
            if (cell.getColour() != null) {
                printColour(cell.getColour());
            }
            stream.print("  ");
        }
    }

    /**
     * Prints a cell of a {@link Pattern}. The print takes up always two characters
     * (whatever the state of the cell is and whatever type of restriction it has).
     *
     * @param cell The cell that has to be displayed.
     */
    private void printCellLargerPattern(Cell cell) {
        if (cell.getDie() != null) {
            printDie(cell.getDie());
        } else {
            if (cell.getColour() == null && cell.getValue() == 0) { //no restrictions
                stream.print("  ");
                return;
            }
            if (cell.getValue() != 0) {
                printValue(cell.getValue());
            }
            if (cell.getColour() != null) {
                printColour(cell.getColour());
            }
            stream.print(" ");
        }
    }

    /**
     * Prints a {@link Pattern}.
     *
     * @param pattern The pattern that has to be displayed.
     */
    public void printPattern(Pattern pattern) {
        Cell[][] grid = pattern.getGrid();
        printName(pattern.getName());
        stream.println("Difficulty: " + pattern.getDifficulty());
        for (int i = 0; i < 4; i++) {
            stream.print("[");
            for (int j = 0; j < 5; j++) {
                printCell(grid[i][j]);
                if (j != 4) stream.print("|");
                else stream.print("]");
            }
            stream.print("\n");
        }
    }

    /**
     * Prints a {@link Pattern}.
     *
     * @param pattern The pattern that has to be displayed.
     */
    //How do we prefer to show a pattern?
    private void printPatternLarger(Pattern pattern) {
        Cell[][] grid = pattern.getGrid();
        printName(pattern.getName());
        stream.println("Difficulty: " + pattern.getDifficulty());
        printSeparator(5);
        for (int i = 0; i < Pattern.ROWS; i++) {
            for (int j = 0; j < Pattern.COLS; j++) {
                stream.print("| ");
                printCellLargerPattern(grid[i][j]);
                stream.print(" ");
            }
            stream.print("|\n");
            printRowWithoutValue(Pattern.COLS);
            printSeparator(Pattern.COLS);
        }
    }

    /**
     * Helper method for the {@code printPattern} and for the {@code printRoundTrack}
     * to print the horizontal separators of the cells with one white space on
     * the left and one on the right.
     *
     * @param n The number of horizontal cells.
     */
    private void printSeparator(int n) {
        for (int i = 0; i < n; i++) {
            stream.print(" ----");
        }
        stream.print("\n");
    }

    /**
     * Helper method for the {@code printPattern} to print a row without values
     * maintaining the pattern's structure to make the print more readable.
     *
     * @param n The number of horizontal cells.
     */
    private void printRowWithoutValue(int n) {
        for (int i = 0; i < n; i++) {
            stream.print("|    ");
        }
        stream.print("|\n");
    }

    /**
     * Prints the {@link DraftPool}.
     *
     * @param dice The list of dice in the {@link DraftPool} that
     *             have to be displayed.
     */
    public void printDraftPool(List<Die> dice) {
        int len = dice.size();
        printIndexesDraftPool(len);
        printSeparator(len);
        for (Die die : dice) {
            stream.print("| ");
            printDie(die);
            stream.print(" ");
        }
        stream.print("|");
        stream.print("\n");
        printSeparator(len);
    }

    /**
     * Helper method for the {@code printDraftPool} to print the index above each die.
     *
     * @param n The number of indexes that have to be printed, it will be the number
     *          of dice in the {@link DraftPool}.
     */
    private void printIndexesDraftPool(int n) {
        for (int i = 0; i < n; i++) {
            stream.print("  ");
            stream.print(i);
            stream.print("  ");
        }
        stream.print("\n");
    }

    /**
     * Prints the {@link RoundTrack}.
     *
     * @param roundTrack The list of list of dice in the {@link RoundTrack} that
     *                   have to be displayed.
     */
    public void printRoundTrack(List<List<Die>> roundTrack) {
        printSeparator(roundTrack.size());
        for (int i = 0; i < 9; i++) { //9 is the max number of dice that can be left in a turn.
            for (List<Die> aRoundTrack : roundTrack) {
                if (aRoundTrack.size() >= i + 1) {
                    stream.print("| ");
                    printDie(aRoundTrack.get(i));
                    stream.print(" |");
                } else {
                    if (aRoundTrack.size() == i) {
                        stream.print(" ---- ");
                    } else {
                        stream.print("      ");
                    }
                }
            }
            stream.print("\n");
        }
    }

    /**
     * Prints a {@link PublicObjectiveCard} in a textual way.
     *
     * @param card The card that has to be displayed.
     */
    private void printPublicObjectiveCard(PublicObjectiveCard card) {
        printName(card.getName());
        printDescription(card.getDescription());
        stream.print("VictoryPoint: ");
        stream.println(card.getVictoryPoints());
        stream.print("\n");
    }

    /**
     * Prints a {@link PrivateObjectiveCard} in a textual way.
     *
     * @param card The card that has to be displayed.
     */
    private void printPrivateObjectiveCard(PrivateObjectiveCard card) {
        printName(card.getName());
        printDescription(card.getDescription());
        stream.print("\n");
    }

    /**
     * Prints a {@link ToolCard} in a textual way.
     *
     * @param card The card that has to be displayed.
     */
    private void printToolCard(ToolCard card) {
        printName(card.getName());
        printDescription(card.getDescription());
        stream.println("Colour: " + card.getColour());
        stream.print("Used: ");
        if (card.isUsed()) {
            stream.println("✓");
        } else {
            stream.println("✗");
        }
        stream.print("\n");
    }

    /**
     * Prints the winner of the game and the Score Board
     *
     * @param scoreBoard the final ranking that has to be displayed.
     */
    public void printScoreBoard(Map<String, Integer> scoreBoard) {
        List<String> players = new ArrayList<>(scoreBoard.keySet());
        stream.println("The winner is " + players.get(0));

        for (int i = 0; i < players.size(); i++) {
            stream.println(i + 1 + ") " + players.get(i) + ": " + scoreBoard.get(players.get(i)));
        }
    }

    /**
     * Prints the tokens of a player.
     *
     * @param num The player's number of tokens.
     */
    private void printTokens(int num) {
        stream.print("tokens:");
        for (int i = 0; i < num; i++) stream.print("⚪");
        stream.print("\n");
    }

    /**
     * Prints all the patterns of the players in the game.
     *
     * @param players A list of {@link PlayerStatus} that contains the name,
     *                the pattern and the number of tokens of a player.
     */
    public void printPatterns(List<PlayerStatus> players) {
        for (PlayerStatus player : players) {
            stream.println(player.getPlayerName() + "'s Pattern");
            printPatternLarger(player.getPattern());
            printTokens(player.getTokens());
        }
    }

    /**
     * Prints the candidate patterns for the player at the beginning
     * of the game.
     *
     * @param patterns The array of candidate patterns that have
     *                 to be displayed.
     */
    public void printPatternSelection(Pattern[] patterns) {
        for (int i = 0; i < patterns.length; i++) {
            stream.println(i);
            printPatternLarger(patterns[i]);
        }
    }

    /**
     * Prints the Public Objective Cards passed as a parameter.
     *
     * @param publicCards The cards that have to be displayed, they
     *                    will be all the cards actually in the game.
     */
    public void printPublicObjectiveCards(PublicObjectiveCard[] publicCards) {
        for (PublicObjectiveCard card : publicCards) {
            printPublicObjectiveCard(card);
        }
    }

    /**
     * Prints the Private Objective Cards passed as a parameter.
     *
     * @param privateCards The cards that have to be displayed, they
     *                     will be all the cards actually in the game.
     */
    public void printPrivateObjectiveCards(PrivateObjectiveCard[] privateCards) {
        for (PrivateObjectiveCard card : privateCards) {
            //the second card is null in MultiPlayer mode.
            if (card != null) printPrivateObjectiveCard(card);
        }
    }

    /**
     * Prints the Tool Cards passed as a parameter.
     *
     * @param toolCards The cards that have to be displayed, they
     *                  will be all the cards actually in the game.
     */
    public void printToolCards(ToolCard[] toolCards) {
        for (ToolCard card : toolCards) {
            printToolCard(card);
        }
    }

}
