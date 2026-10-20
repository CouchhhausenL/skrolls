package main.java.console;

// Thrown when the terminal cannot be created or reports an unusable size
public class ConsoleConstructionException extends Exception {
    public ConsoleConstructionException(String message) {
        super(message);
    }

    public ConsoleConstructionException(Throwable cause) {
        super(cause);
    }
}
