package it.polimi.se2018.view.cli;

import it.polimi.se2018.view.cli.CliDisplayer;

import java.io.InputStream;
import java.util.Scanner;

/**
 * This class gatherers the input from the user and passes it to the
 * {@link CliDisplayer}.
 */
public class CliInput {

    /**
     * Text scanner that parses the user's input.
     */
    private Scanner scanner;

    /**
     * The constructor of the class.
     * @param inputStream The {@link InputStream} from which the {@code scanner}
     *                    scans input.
     */
    public CliInput(InputStream inputStream) {

        this.scanner = new Scanner(inputStream);
    }

    /**
     * Scans an integer from the {@link InputStream} chosen when creating the class
     * and returns it.
     * @return The value inserted from the user.
     */
    public int readInputInt(){
        return scanner.nextInt();
    }

    /**
     * Scans a {@link String} from the {@link InputStream} chosen when creating the class
     * and returns it.
     * @return The {@link String} inserted from the user.
     */
    public String readInputString(){
        return scanner.nextLine();
    }

}
