package it.polimi.se2018.view.cli;


import java.io.InputStream;
import java.util.Scanner;

/**
 * This class gatherers the input from the user and handles it continuously
 * through an {@link InputEventManager} that is set by the {@link CliDisplayer}.
 * <p>Input gathering can be done on an independent thread.</p>
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
    private final Scanner scanner;

    /**
     * Flag to indicate if a prompt has to be overridden.
     */
    private boolean overridePrompt = false;

    /**
     * Object used to synchronize prompt printing.
     */
    private final Object promptLock = new Object();

    /**
     * The constructor of the class.
     *
     * @param inputStream The {@link InputStream} from which the {@code scanner}
     *                    scans input.
     */
    public CliInput(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    /**
     * Starts a read-handle loop.
     */
    @Override
    public void run() {
        gameRunning = true;
        waitForEventManager();

        // prompt-read-handle loop
        while (gameRunning) {
            synchronized (promptLock) {
                manager.showPrompt();
                overridePrompt = true;
            }
            String input = scanner.nextLine();
            overridePrompt = false;
            if (gameRunning)
                manager.handle(input);
        }
    }

    /**
     * Waits until the event manager is set to a non-null value.
     */
    private synchronized void waitForEventManager() {
        while (manager == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Getter for the handler of input data.
     *
     * @return the current handler.
     */
    public synchronized InputEventManager getManager() {
        return manager;
    }

    /**
     * Setter for the handler of input data.
     * <p>It also prints a prompt in the case it has to be printed for the first time.</p>
     *
     * @param manager the handler that has to be set.
     */
    public synchronized void setManager(InputEventManager manager) {
        this.manager = manager;
        notifyAll();
        updatePrompt();
    }

    /**
     * Stops the read-handle loop.
     */
    void stop() {
        this.gameRunning = false;
    }

    /**
     * Tells if the game is running or not.
     *
     * @return {@code true} if the game is currently running; {@code false} otherwise.
     */
    boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * Updates the prompted message if the read-handle loop hasn't done it yet.
     */
    private void updatePrompt() {
        synchronized (promptLock) {
            if (overridePrompt)
                manager.showPrompt();
        }
    }

    /**
     * Resets the underlying input manager to its original state.
     * <p>Also, forces the prompt to be displayed.</p>
     */
    void resetInputManager(){
        manager.reset();
        updatePrompt();
    }
}
