package main.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SplashText {

    private SplashText(){}

    public static String readFile(String path) {
        try (var stream = SplashText.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String getDeath(){
        String[] content = readFile("/assets/splash_text/death.txt").split("\\r?\\n");
        return content[(int) (Math.random() * content.length)];
    }

    public static String getLoading(){

        String[] content = readFile("/assets/splash_text/loading.txt").split("\\r?\\n");
        return content[(int) (Math.random() * content.length)];
    }

    public static String getNo(){

        String[] content = readFile("/assets/splash_text/no.txt").split("\\r?\\n");
        return content[(int) (Math.random() * content.length)];
    }

    public static String[] getCredits(){
        return readFile("/assets/splash_text/credits.txt").split("\\r?\\n");
    }
}
