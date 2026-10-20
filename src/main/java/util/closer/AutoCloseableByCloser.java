package main.java.util.closer;

// Two-phase close: first the external Closer (shared cleanup), then the
// object's own resources. Order matters because cleanup may need the object alive.
public interface AutoCloseableByCloser extends AutoCloseable {
    Closer getCloser();

    @Override
    default void close() throws Exception {
        getCloser().close();
        closeSelf();
    }

    void closeSelf() throws Exception;
}
