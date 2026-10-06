package main.java.console;

public class CellBuffer {
    private int width;
    private int height;
    private Cell[][] cells;

    public CellBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[height][width];
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[height][width];
    }

    public void set(int x, int y, Cell cell) {
        cells[y][x] = cell;
    }

    public Cell get(int x, int y) {
        return cells[y][x];
    }

}
