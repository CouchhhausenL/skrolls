package main.java.console;

// Flat grid of terminal cells. Rows are indexed first: [y][x].
// Colors are 0xRRGGBB. EMPTY_CELL is a hole that overlay() ignores.
public class CellBuffer {
    // Means "keep the destination background" when passed to setTransparent()
    public static final int TRANSPARENT_BACKGROUND = -1;
    public static final char EMPTY_CELL = '\0';

    private int width;
    private int height;

    // Avoid Cell object to prevent instantiation on render path
    private char[][] characters;
    private int[][] foregrounds;
    private int[][] backgrounds;

    public CellBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        characters = new char[height][width];
        foregrounds = new int[height][width];
        backgrounds = new int[height][width];
    }

    // Reallocating is simpler than zeroing every row and yields default
    // values (EMPTY_CELL, foreground 0, background 0)
    public void clear() {
        characters = new char[height][width];
        foregrounds = new int[height][width];
        backgrounds = new int[height][width];
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        clear();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInDimensions(int x, int y) {
        return !(x < 0 || x > width - 1 || y < 0 || y > height - 1);
    }

    public boolean setCharacter(int x, int y, char character) {
        if (!isInDimensions(x, y)) return false;
        characters[y][x] = character;
        return true;
    }

    public boolean setForeground(int x, int y, int foreground) {
        if (foreground < 0)
            throw new IllegalArgumentException("Foreground color cannot be negative");
        if (!isInDimensions(x, y)) return false;
        foregrounds[y][x] = foreground;
        return true;
    }

    public boolean setBackground(int x, int y, int background) {
        if (background < -1)
            throw new IllegalArgumentException("Background color cannot be negative");
        if (!isInDimensions(x, y)) return false;
        backgrounds[y][x] = background;
        return true;
    }

    public boolean set(int x, int y, char character, int foreground, int background) {
        if (!isInDimensions(x, y)) return false;
        setCharacter(x, y, character);
        setForeground(x, y, foreground);
        setBackground(x, y, background);
        return true;
    }

    // Resolves the transparent sentinel to the existing pixel before writing
    public boolean setTransparent(int x, int y, char character, int foreground, int background) {
        if (background == TRANSPARENT_BACKGROUND)
            background = getBackground(x, y);

        return set(x, y, character, foreground, background);
    }

    public char getCharacter(int x, int y) {
        return characters[y][x];
    }

    public int getForeground(int x, int y) {
        return foregrounds[y][x];
    }

    public int getBackground(int x, int y) {
        return backgrounds[y][x];
    }

    public void fill(char character, int foreground, int background) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                set(x, y, character, foreground, background);
            }
        }
    }

    // Blits another buffer at an offset. Empty cells and transparent
    // backgrounds leave the destination untouched.
    public void overlay(int rootX, int rootY, CellBuffer overlay) {
        for (int x = 0; x < overlay.width; x++) {
            for (int y = 0; y < overlay.height; y++) {
                int shiftedX = x + rootX;
                int shiftedY = y + rootY;

                char newChar = overlay.getCharacter(x, y);
                if (newChar == EMPTY_CELL) continue;
                int newForeground = overlay.getForeground(x, y);
                int newBackground = overlay.getBackground(x, y);

                setTransparent(
                        shiftedX,
                        shiftedY,
                        newChar,
                        newForeground,
                        newBackground
                );
            }
        }
    }

}
