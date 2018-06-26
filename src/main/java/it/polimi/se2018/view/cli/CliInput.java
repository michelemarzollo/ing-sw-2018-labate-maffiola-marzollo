package it.polimi.se2018.view.cli;


import java.io.InputStream;
import java.util.Scanner;

/**
 * This class gatherers the input from the user and handles it continuously
 * through an {@link InputEventManager} that is set by the {@link CliDisplayer}.
 * A new thread must complete this task so that the user interaction does not
 * interfere with the network communication.
 */
public class CliInput implements Runnable {

    /**
     * Indicates if the game is going on or if it is ended.
     */
    private boolean gameRunning;

    /**
     * The handler that manages the input inserted and through which
     * the prompt interface with user is showed.
     */
    private InputEventManager manager;

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
        this.gameRunning = true;
    }

    /**
     * The thread begins its execution.
     */
    @Override
    public void run() {
        String input;
        while (gameRunning){
            if(manager != null) {
                //@TODO ci vuole una corretta sincronizzazione
                manager.showPrompt();
            }
            input = scanner.nextLine();
            if(manager != null) {
                manager.handle(input);
            }

        }
    }


    /**
     * Getter for the handler of input data.
     * @return the current handler.
     */
    public InputEventManager getManager(){
        return manager;
    }


    /**
     * Setter for the handler of input data.
     * @param manager the handler that has to be set.
     */
    public void setManager(InputEventManager manager) {
        this.manager = manager;
    }

    /**
     * Setter for the game running
     * @param gameRunning is {@code true} when the game is currently running,
     * {@code false} otherwise.
     */
    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}
