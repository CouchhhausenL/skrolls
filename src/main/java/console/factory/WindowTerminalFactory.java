package main.java.console.factory;

import org.jline.builtins.SwingTerminal;
import org.jline.terminal.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

public final class WindowTerminalFactory {
    private WindowTerminalFactory() {
    }

    public static TerminalHolder create() throws IOException {
        SwingTerminal terminal = new SwingTerminal();
        CommonTerminalFactory.commonInit(terminal);

        SwingTerminal.TerminalComponent component = terminal.getComponent();

        component.setTerminalFont(
                loadTerminalFont()
        );

        try {
            setCursorInvisible(component);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        FontMetrics fm = component.getFontMetrics(
                component.getTerminalFont()
        );

        int cellWidth = fm.charWidth('M');
        int cellHeight = fm.getHeight();

        component.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (cellWidth <= 0 || cellHeight <= 0) {
                    return;
                }

                int columns = component.getWidth() / cellWidth;
                int rows = component.getHeight() / cellHeight;

                terminal.setSize(Size.of(columns, rows));
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int button = e.getWheelRotation() < 0 ? 64 : 65;
                sendSgrMouse(button, 'M', e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                sendSgrMouse(buttonCode(e.getButton()), 'm', e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                sendSgrMouse(buttonCode(e.getButton()), 'M', e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                sendSgrMouse(35, 'M', e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                sendSgrMouse(32 | buttonCode(e.getModifiersEx()), 'M', e.getX(), e.getY());
            }

            private void sendSgrMouse(int button, char terminator, int pixelX, int pixelY) {
                if (cellWidth <= 0 || cellHeight <= 0) {
                    return;
                }

                int x = pixelX / cellWidth + 1;
                int y = pixelY / cellHeight + 1;

                String sequence = "\033[<" + button + ";" + x + ";" + y + terminator;
                try {
                    terminal.processInputBytes(
                            sequence.getBytes(java.nio.charset.StandardCharsets.US_ASCII)
                    );
                } catch (IOException ignored) {
                }
            }

            private int buttonCode(int button) {
                return switch (button) {
                    case MouseEvent.BUTTON1 -> 0;
                    case MouseEvent.BUTTON2 -> 1;
                    case MouseEvent.BUTTON3 -> 2;
                    default -> 3;
                };
            }
        };

        component.addMouseListener(mouseAdapter);
        component.addMouseMotionListener(mouseAdapter);
        component.addMouseWheelListener(mouseAdapter);

        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (e.isAltDown()) {
                    send('\033'); // Escape
                    return;
                }

                if (e.isControlDown()) {
                    if ((int) c < 27) {
                        send(c);
                    } else if (c >= 'a' && c <= 'z') {
                        send((char) (c - 'a' + 1));
                    } else if (c >= 'A' && c <= 'Z') {
                        send((char) (c - 'A' + 1));
                    }
                }
            }

            private void send(char c) {
                try {
                    terminal.processInputBytes(
                            new byte[]{(byte) c}
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        JFrame frame = terminal.createFrame("Terminal");
        frame.setVisible(true);

        component.repaint();
        return new TerminalHolder(terminal, () -> {
            CommonTerminalFactory.commonBeforeClose(terminal);
            component.dispose();
            frame.dispose();
        });
    }


    // Uses java reflection to hide blinking cursor in swing emulator
    private static void setCursorInvisible(SwingTerminal.TerminalComponent component) throws NoSuchFieldException, IllegalAccessException {
        Class<? extends SwingTerminal.TerminalComponent> clazz = component.getClass();

        Field timerField = clazz.getDeclaredField("cursorTimer");
        timerField.setAccessible(true);

        Timer timer = (Timer) timerField.get(component);
        timer.stop();

        Field cursorVisibleField = clazz.getDeclaredField("cursorVisible");
        cursorVisibleField.setAccessible(true);

        AtomicBoolean cursorVisible = (AtomicBoolean) cursorVisibleField.get(component);
        cursorVisible.set(false);

        component.repaint();
    }

    private static Font loadTerminalFont() throws IOException {
        try (InputStream in = WindowTerminalFactory.class.getResourceAsStream(
                "/assets/font/JetBrainsMono-Regular.ttf")) {

            if (in == null) {
                throw new IOException("JetBrains Mono font resource not found");
            }

            try {
                return Font.createFont(Font.TRUETYPE_FONT, in)
                        .deriveFont(Font.PLAIN, 16);
            } catch (FontFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
