package main.java.console.input;

// Base type for everything the InputParser produces; closed to key and mouse events
public sealed interface InputEvent
        permits KeyInputEvent, MouseInputEvent {
}
