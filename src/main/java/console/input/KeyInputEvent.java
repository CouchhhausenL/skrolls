package main.java.console.input;

public final class KeyInputEvent implements InputEvent {
    public static final int NO_CODE = -1;

    private final Type type;
    private final int code;
    private KeyInputEvent(Type type, int code) {
        this.type = type;
        this.code = code;
    }

    public Type type() {
        return type;
    }
    public int code() {
        return code;
    }

    @Override
    public String toString() {
        return "KeyInputEvent{" +
                "code=" + code +
                ", type=" + type +
                '}';
    }

    public enum Type {
        CHARACTER,
        ESCAPE,
        CONTROL,
        ALT,
        ARROW_UP,
        ARROW_DOWN,
        ARROW_RIGHT,
        ARROW_LEFT,
        TAB,
        BACKSPACE,
        ENTER,
        DELETE
    }

    public static KeyInputEvent of(int code) {

        if (code == 9) return tab(); // Also ctrl + I
        if (code == 8 || code == 128) return backspace(); // Also ctrl + H
        if (code == 126) return delete();
        if (code == 10 || code == 13) return enter(); //Also ctrl + J / Ctrl + M
        if (code == 27) return escape();
        if (code < 27) return control(code);

        return character(code);
    }

    public static KeyInputEvent character(int code) {
        return new KeyInputEvent(Type.CHARACTER, code);
    }
    public static KeyInputEvent escape() {
        return new KeyInputEvent(Type.ESCAPE, 27);
    }
    public static KeyInputEvent control(int code) {
        return new KeyInputEvent(Type.CONTROL, code);
    }
    public static KeyInputEvent alt(int code) {
        return new KeyInputEvent(Type.ALT, code);
    }
    public static KeyInputEvent arrow_up() {
        return new KeyInputEvent(Type.ARROW_UP, NO_CODE);
    }
    public static KeyInputEvent arrow_down() {
        return new KeyInputEvent(Type.ARROW_DOWN, NO_CODE);
    }
    public static KeyInputEvent arrow_right() {
        return new KeyInputEvent(Type.ARROW_RIGHT, NO_CODE);
    }
    public static KeyInputEvent arrow_left() {
        return new KeyInputEvent(Type.ARROW_LEFT, NO_CODE);
    }
    public static KeyInputEvent tab() {
        return new KeyInputEvent(Type.TAB, 9);
    }
    public static KeyInputEvent backspace() {
        return new KeyInputEvent(Type.BACKSPACE, 8);
    }
    public static KeyInputEvent enter() {
        return new KeyInputEvent(Type.ENTER, 10);
    }
    public static KeyInputEvent delete() {
        return new KeyInputEvent(Type.DELETE, 126);
    }

    private boolean is_letter(Letter letter) {
        int ord = letter.ordinal();
        return
                code == ('a' + ord) || // lowercase
                code == ('A' + ord) || // uppercase
                code == ord + 1;       // ctrl + ...
    }

    public boolean is_char() {
        return type == Type.CHARACTER;
    }
    public boolean is_char(Letter letter) {
        return is_char() && is_letter(letter);
    }
    public boolean is_escape() {
        return type == Type.ESCAPE;
    }
    public boolean is_ctrl() {
        return type == Type.CONTROL;
    }
    public boolean is_ctrl(Letter letter) {
        return is_ctrl() && is_letter(letter);
    }
    public boolean is_alt() {
        return type == Type.ALT;
    }
    public boolean is_alt(Letter letter) {
        return is_alt() && is_letter(letter);
    }
    public Direction get_arrow_direction() {
        return switch (type) {
            case ARROW_UP -> Direction.UP;
            case ARROW_DOWN -> Direction.DOWN;
            case ARROW_LEFT -> Direction.LEFT;
            case ARROW_RIGHT -> Direction.RIGHT;
            default -> null;
        };
    }
    public boolean is_arrow() {
        return get_arrow_direction() != null;
    }
    public boolean is_arrow(Direction direction) {
        return get_arrow_direction() == direction;
    }
    public boolean is_tab() {
        return type == Type.TAB;
    }
    public boolean is_backspace() {
        return type == Type.BACKSPACE;
    }
    public boolean is_enter() {
        return type == Type.ENTER;
    }
    public boolean is_delete() {
        return type == Type.DELETE;
    }

    public enum Letter {
        A, B, C, D, E, F, G, H, I,
        J, K, L, M, N, O, P, Q, R,
        S, T, U, V, W, X, Y, Z
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
