package main.java.animation;

import java.util.Optional;
import java.util.function.Consumer;

// A partial pixel update. Empty optionals mean "leave this channel unchanged",
// which lets frames only store the pixels that actually differ.
public record AnimationChange(Optional<Character> character, Optional<Integer> foreground,
                              Optional<Integer> background) {
    public void ifCharacter(Consumer<Character> consumer) {
        character.ifPresent(consumer);
    }

    public void ifForeground(Consumer<Integer> consumer) {
        foreground.ifPresent(consumer);
    }

    public void ifBackground(Consumer<Integer> consumer) {
        background.ifPresent(consumer);
    }
}
