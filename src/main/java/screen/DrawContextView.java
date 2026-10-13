package main.java.screen;

public interface DrawContextView {
    void cell(int x, int y, char character, int foreground, int background);
    void cell(int x, int y, int background);

    void fill(int x1, int y1, int x2, int y2, char character, int foreground, int background);
    void fill(int x1, int y1, int x2, int y2, int background);

    void drawString(int x, int y, String string, int foreground, int background);
    void drawString(int x, int y, String string, int foreground);

    void pushScissor(int x1, int y1, int x2, int y2);
    void popScissor();
}
