package main.java.util.closer;

// Cleanup callback for resources that live outside the owning object
// (e.g. restoring terminal state before the terminal itself is closed)
public interface Closer {
    void close() throws Exception;
}
