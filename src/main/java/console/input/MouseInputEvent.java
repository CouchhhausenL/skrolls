package main.java.console.input;

public record MouseInputEvent(Type type, int x, int y, int button) implements InputEvent {

    public enum Type {
        PRESS,
        RELEASE,
        MOVE,
        SCROLL_UP,
        SCROLL_DOWN
    }

    public static Type inferType(int button, boolean down) {
        if (button == 32 || button == 3) return Type.MOVE;
        if (button == 64) return Type.SCROLL_UP;
        if (button == 65) return Type.SCROLL_DOWN;
        return down ? Type.PRESS : Type.RELEASE;
    }
}
