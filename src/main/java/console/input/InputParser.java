package main.java.console.input;

import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputParser {
    private final NonBlockingReader reader;

    private final Thread inputThread;
    private boolean running;

    private final Queue<InputEvent> eventQueue;

    public InputParser(Terminal terminal) {
        reader = terminal.reader();

        eventQueue = new ConcurrentLinkedQueue<>();
        running = false;
        inputThread = new Thread(this::readLoop, "terminal_input");
        inputThread.setDaemon(true);
    }

    public void start() {
        if (running) return;

        running = true;
        inputThread.start();
    }

    private void readLoop() {
        List<Integer> parserBuffer = new ArrayList<>();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                parserBuffer.clear();
                ParserMode mode = ParserMode.NORMAL;

                int key = reader.read(0); // 0 -> wait forever
                if (key >= 0) {
                    while (key >= 0) {
                        mode = readKey(mode, key, parserBuffer);
                        key = reader.read(10);
                    }
                    readKey(mode, -1, parserBuffer);
                }


            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static final int ESCAPE_CODE = 27;
    private static final int CSI_CODE = '[';
    private static final int SS3_CODE = 'O';
    private static final int SGR_MOUSE_CODE = '<';
    private static final int SGR_MOUSE_PRESS_CODE = 'M';
    private static final int SGR_MOUSE_RELEASE_CODE = 'm';
    private static final String SGR_MOUSE_SPLITTER = ";";

    private ParserMode readKey(ParserMode mode, int key, List<Integer> buffer) {
        // Last iteration
        if (key == -1) {
            if (mode == ParserMode.ESCAPE) eventQueue.add(KeyInputEvent.escape());
            return null;
        }

        if (mode == ParserMode.NORMAL) {
            if (key == ESCAPE_CODE) return ParserMode.ESCAPE;
            eventQueue.add(KeyInputEvent.of(key));
            return ParserMode.NORMAL;
        }
        if (mode == ParserMode.ESCAPE) {
            if (key == ESCAPE_CODE) { // Double escape code
                eventQueue.add(KeyInputEvent.escape());
                return ParserMode.ESCAPE;
            }

            if (key == CSI_CODE) return ParserMode.CSI;
            if (key == SS3_CODE) return ParserMode.SS3;

            eventQueue.add(KeyInputEvent.alt(key));
            return ParserMode.NORMAL;
        }
        if (mode == ParserMode.CSI) {
            if (key == SGR_MOUSE_CODE) return ParserMode.SGR_MOUSE;
            if (key == SGR_MOUSE_PRESS_CODE || key == SGR_MOUSE_RELEASE_CODE)
                throw new RuntimeException("Terminal emulator does not support SGR encoding");
        }

        if (mode == ParserMode.CSI || mode == ParserMode.SS3) {
            KeyInputEvent event = switch (key) {
                case 65 -> KeyInputEvent.arrowUp();    // A
                case 66 -> KeyInputEvent.arrowDown();  // B
                case 67 -> KeyInputEvent.arrowRight(); // C
                case 68 -> KeyInputEvent.arrowLeft();  // D
                default -> null;
            };

            if (event != null) {
                eventQueue.add(event);
                return ParserMode.NORMAL;
            }

            // Ignore unrecognized csi / ss3 codes
            return ParserMode.NORMAL;
        }


        if (mode == ParserMode.SGR_MOUSE) {
            if (key == SGR_MOUSE_PRESS_CODE || key == SGR_MOUSE_RELEASE_CODE) {
                StringBuilder builder = new StringBuilder();
                for (int entry : buffer) {
                    builder.append((char) entry);
                }

                String[] split = builder.toString().split(SGR_MOUSE_SPLITTER);

                int button = Integer.parseInt(split[0]);
                int x = Integer.parseInt(split[1]) - 1; // SGR mouse events are
                int y = Integer.parseInt(split[2]) - 1; // 1-based
                boolean down = key == SGR_MOUSE_PRESS_CODE;

                eventQueue.add(new MouseInputEvent(
                        MouseInputEvent.inferType(button, down),
                        x,
                        y,
                        button
                ));

                buffer.clear();
                return ParserMode.NORMAL;
            }

            buffer.add(key);
            return ParserMode.SGR_MOUSE;
        }

        throw new IllegalStateException("Input parser has reached an impossible state: " + mode.name());
    }

    private enum ParserMode {
        NORMAL,
        ESCAPE,
        CSI,
        SS3,
        SGR_MOUSE
    }

    public InputEvent drainEvent() {
        if (!running) throw new IllegalStateException("Input parser thread was never started");
        return eventQueue.poll();
    }

    public void stop() {
        if (running) inputThread.interrupt();
    }
}
