package it.polimi.se2018.utils;

import java.io.PrintStream;
import java.time.Instant;

public class Logger {

    private static Logger defaultLogger;

    private final PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public static Logger getDefaultLogger(){
        if(defaultLogger == null)
            defaultLogger = new Logger(System.err);
        return defaultLogger;
    }

    public synchronized void log(String message){
        String timestamp = Instant.now().toString();
        out.println("[" + timestamp + "] " + message);
    }
}
