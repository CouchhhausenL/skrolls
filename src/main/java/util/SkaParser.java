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

    // Control characters used by the .ska format to delimit structure
    private static final char FRAME = '\u0001';
    private static final char ROW_END = '\u0002';
    private static final char PIXEL = '\u0003';
    private static final char PIXEL_DATA = '\u0004';

    // Parses a .ska animation resource. Layout:
    // width,height,#canvasBackground,fps, followed by frame data
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

        // Header fields are comma separated: width,height,#background,fps
        int c1 = content.indexOf(',');
        int c2 = content.indexOf(',', c1 + 1);
        int c3 = content.indexOf(',', c2 + 1);
        int width = Integer.parseInt(content.substring(0, c1));
        int height = Integer.parseInt(content.substring(c1 + 1, c2));
        int canvas_background = Integer.parseInt(content.substring(c2 + 1, c3).replaceFirst("^#", ""), 16);

        // fps is not comma terminated; it ends at the first non-digit
        int fpsEnd = c3 + 1;
        while (fpsEnd < content.length() && Character.isDigit(content.charAt(fpsEnd))) fpsEnd++;
        int fps = Integer.parseInt(content.substring(c3 + 1, fpsEnd));
        String code = content.substring(fpsEnd);

        Animation.Builder builder = new Animation.Builder(width, height, canvas_background, fps);
        AnimationFrame.Builder frameBuilder = null;
        StringBuilder pixelBuffer = new StringBuilder();
        int x = -1; // PIXEL is a start delimiter, so the first pixel increments to 0
        int y = 0;
        for (char c : code.toCharArray()) {
            // FRAME closes the previous frame and starts a new one at the origin
            if (c == FRAME) {
                if (frameBuilder != null) builder.frame(frameBuilder.build());
                frameBuilder = new AnimationFrame.Builder(width, height);
                x = -1;
                y = 0;
                continue;
            }
            // PIXEL and ROW_END both terminate the pixel data currently buffered
            if ((c == ROW_END || c == PIXEL) && !pixelBuffer.isEmpty()) {
                // PIXEL_DATA separates the fields of one pixel: [char];[fg];[bg]
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

                // Empty fields are omitted by the nested loop, so changeFromFields
                // infers their meaning from position and count
                AnimationChange change = changeFromFields(fields);
                assert frameBuilder != null;
                frameBuilder.set(x, y, change);
                pixelBuffer.setLength(0);
            }
            // ROW_END advances to the next row and resets the column
            if (c == ROW_END) {
                y++;
                x = -1;
                continue;
            }
            // PIXEL advances to the next column and starts collecting its data
            if (c == PIXEL) {
                x++;
                pixelBuffer.setLength(0);
                continue;
            }
            pixelBuffer.append(c);
        }

        // Flush the final frame (the format has no trailing FRAME marker)
        builder.frame(frameBuilder.build());
        return builder.build();
    }

    // Converts the raw fields of one pixel into an AnimationChange.
    // Empty fields are dropped, so the field count disambiguates the meaning:
    // 1 field  -> char or foreground color
    // 2 fields -> char + foreground, or foreground + background
    // 3 fields -> char + foreground + background
    private static AnimationChange changeFromFields(List<String> fields) {

        Optional<Character> character = Optional.empty();
        Optional<Integer> foreground = Optional.empty();
        Optional<Integer> background = Optional.empty();

        switch (fields.size()) {
            case 1 -> {
                // A single character is a glyph; longer values are colors
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

                // "c;fg" when the first field is a glyph, otherwise "fg;bg"
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
                // "c;fg;bg"
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
