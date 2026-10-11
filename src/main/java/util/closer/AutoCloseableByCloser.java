package main.java.util.closer;

public interface AutoCloseableByCloser extends AutoCloseable {
    Closer getCloser();

    @Override
    default void close() throws Exception {
        getCloser().close();
        closeSelf();
    }

    void closeSelf() throws Exception;
}
