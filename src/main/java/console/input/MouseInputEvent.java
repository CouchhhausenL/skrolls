package main.java.console.input;

public record MouseInputEvent(Type type, int x, int y, int button) implements InputEvent {

    public enum Type {
        PRESS,
        RELEASE,
        MOVE,
        SCROLL_UP,
        SCROLL_DOWN
    }

    // SGR button bitmask: bit 6 (64) = wheel, bit 5 (32) = motion,
    // lower two bits = button index (3 = no button). Horizontal scroll is
    // approximated as vertical because the game only uses up/down.
    public static Type inferType(int button, boolean down) {
        if ((button & 64) != 0) {
            return switch (button & 3) {
                case 0 -> Type.SCROLL_UP;
                case 1 -> Type.SCROLL_DOWN;
                case 2 -> Type.SCROLL_UP;   // Horizontal scroll
                case 3 -> Type.SCROLL_DOWN; // Horizontal scroll
                default -> throw new IllegalStateException();
            };
        }

        if ((button & 32) != 0 || (button & 3) == 3) {
            return Type.MOVE;
        }

        return down ? Type.PRESS : Type.RELEASE;
    }
}
