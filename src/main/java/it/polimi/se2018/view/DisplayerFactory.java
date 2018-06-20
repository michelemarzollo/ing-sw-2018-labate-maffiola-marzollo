package it.polimi.se2018.view;

import it.polimi.se2018.view.cli.CliDisplayer;
import it.polimi.se2018.view.gui.JavaFxDisplayer;

import java.util.concurrent.Semaphore;

/**
 * Factory for Displayer classes.
 * <p>This class is a singleton.</p>
 */
public class DisplayerFactory {

    /**
     * The only instance for the class.
     */
    private static DisplayerFactory instance;

    /**
     * The displayer instance.
     */
    private Displayer displayer;

    /**
     * The semaphore used to synchronize displayer callbacks.
     */
    private Semaphore displayerSemaphore = new Semaphore(0);

    /**
     * Private constructor to force singleton behaviour.
     */
    private DisplayerFactory(){
    }

    /**
     * Getter for class instance.
     * @return The class instance.
     */
    public static DisplayerFactory getInstance(){
        if(instance == null)
            instance = new DisplayerFactory();
        return instance;
    }

    /**
     * Callback function for displayers.
     * @param displayer The displayer instance.
     */
    private void setDisplayer(Displayer displayer){
        this.displayer = displayer;
        displayerSemaphore.release();
    }

    /**
     * Creates a new CLI displayer.
     * @return A new CLI displayer.
     */
    public synchronized Displayer newCliDisplayer(){
        CliDisplayer.launchCli(System.in, System.out, this::setDisplayer);
        try{
            displayerSemaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return displayer;
    }

    /**
     * Creates a new GUI displayer.
     * @return A new GUI displayer.
     */
    public synchronized Displayer newGuiDisplayer(){
        new Thread(() -> JavaFxDisplayer.launchGui(this::setDisplayer), "Factory-Thread").start();
        try {
            displayerSemaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return displayer;
    }
}
