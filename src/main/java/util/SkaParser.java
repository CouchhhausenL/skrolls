package main.java.util;

import main.java.animation.Animation;
import main.java.animation.AnimationChange;
import main.java.animation.AnimationFrame;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SkaParser {
    private SkaParser() {
    }

    private static final char FRAME = '\u0001';
    private static final char ROW_END = '\u0002';
    private static final char PIXEL = '\u0003';
    private static final char PIXEL_DATA = '\u0004';

    public static Animation parse(String path) {
        String content;
        try (var stream = SkaParser.class.getResourceAsStream("/assets/animation/" + path)) {
            if (stream == null) {
                throw new IOException("Resource not found");
            }

            content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        int c1 = content.indexOf(',');
        int c2 = content.indexOf(',', c1 + 1);
        int c3 = content.indexOf(',', c2 + 1);
        int width = Integer.parseInt(content.substring(0, c1));
        int height = Integer.parseInt(content.substring(c1 + 1, c2));
        int canvas_background = Integer.parseInt(content.substring(c2 + 1, c3).replaceFirst("^#", ""), 16);

        int fpsEnd = c3 + 1;
        while (fpsEnd < content.length() && Character.isDigit(content.charAt(fpsEnd))) fpsEnd++;
        int fps = Integer.parseInt(content.substring(c3 + 1, fpsEnd));
        String code = content.substring(fpsEnd);

        Animation.Builder builder = new Animation.Builder(width, height, canvas_background, fps);
        AnimationFrame.Builder frameBuilder = null;
        StringBuilder pixelBuffer = new StringBuilder();
        int x = -1; // Because PIXEL a starting delimiter instead of a splitter
        int y = 0;
        for (char c : code.toCharArray()) {
            if (c == FRAME) {
                if (frameBuilder != null) builder.frame(frameBuilder.build());
                frameBuilder = new AnimationFrame.Builder(width, height);
                x = -1;
                y = 0;
                continue;
            }
            if ((c == ROW_END || c == PIXEL) && !pixelBuffer.isEmpty()) {
                List<String> fields = new ArrayList<>();
                StringBuilder field = new StringBuilder();

                for (char pc : pixelBuffer.toString().toCharArray()) {
                    if (pc == PIXEL_DATA) {
                        if (field.length() > 0) fields.add(field.toString());
                        field.setLength(0);
                    } else {
                        field.append(pc);
                    }
                }
                if (field.length() > 0) fields.add(field.toString());

                AnimationChange change = changeFromFields(fields);
                assert frameBuilder != null;
                frameBuilder.set(x, y, change);
                pixelBuffer.setLength(0);
            }
            if (c == ROW_END) {
                y++;
                x = -1;
                continue;
            }
            if (c == PIXEL) {
                x++;
                pixelBuffer.setLength(0);
                continue;
            }
            pixelBuffer.append(c);
        }

        builder.frame(frameBuilder.build());
        return builder.build();
    }

    private static AnimationChange changeFromFields(List<String> fields) {

        Optional<Character> character = Optional.empty();
        Optional<Integer> foreground = Optional.empty();
        Optional<Integer> background = Optional.empty();

        switch (fields.size()) {
            case 1 -> {
                String field = fields.getFirst();

                if (field.length() == 1) {
                    character = Optional.of(field.charAt(0));
                } else if (!field.isEmpty()) {
                    foreground = Optional.of(
                            Integer.parseInt(field, 16)
                    );
                }
            }

            case 2 -> {
                String first = fields.get(0);
                String second = fields.get(1);

                if (first.length() == 1) {
                    character = Optional.of(first.charAt(0));

                    if (!second.isEmpty()) {
                        foreground = Optional.of(
                                Integer.parseInt(second, 16)
                        );
                    }
                } else {
                    if (!first.isEmpty()) {
                        foreground = Optional.of(
                                Integer.parseInt(first, 16)
                        );
                    }

                    if (!second.isEmpty()) {
                        background = Optional.of(
                                Integer.parseInt(second, 16)
                        );
                    }
                }
            }

            case 3 -> {
                String first = fields.get(0);
                String second = fields.get(1);
                String third = fields.get(2);

                if (first.length() == 1) {
                    character = Optional.of(first.charAt(0));
                }

                if (!second.isEmpty()) {
                    foreground = Optional.of(
                            Integer.parseInt(second, 16)
                    );
                }

                if (!third.isEmpty()) {
                    background = Optional.of(
                            Integer.parseInt(third, 16)
                    );
                }
            }

            default -> throw new IllegalArgumentException(
                    "Invalid pixel data"
            );
        }

        return new AnimationChange(character, foreground, background);
    }
}
