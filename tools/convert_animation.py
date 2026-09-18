import sys
import json
import logging
from typing import Any, NamedTuple

LOGGING_LEVEL = logging.INFO

IN_FORMAT = ".json"
OUT_FORMAT = ".ska"

FRAME = b"\x01"
ROW_END = b"\x02"
PIXEL = b"\x03"
PIXEL_DATA = b"\x04"

DEFAULT_FOREGROUND = "#ffffff"

logger = logging.getLogger(__name__)


def encode(string: str) -> bytes:
    return string.encode("utf-8")


class Pixel(NamedTuple):
    character: str
    foreground: str | None
    background: str | None


def read_json_file(path: str) -> Any:
    logger.info("Reading JSON file: %s", path)

    try:
        with open(path, "r", encoding="utf-8") as f:
            data = json.load(f)
    except FileNotFoundError:
        logger.error("Input file not found: %s", path)
        raise
    except json.JSONDecodeError:
        logger.exception("Failed to parse JSON file: %s", path)
        raise
    except OSError:
        logger.exception("Failed to read input file: %s", path)
        raise

    logger.info("Successfully read JSON file: %s", path)
    return data


def write_file(path: str, content: bytes):
    logger.info("Writing output file: %s (%d bytes)", path, len(content))

    try:
        with open(path, "wb") as f:
            f.write(content)
    except OSError:
        logger.exception("Failed to write output file: %s", path)
        raise

    logger.info("Successfully wrote output file: %s", path)


def main():
    logging.basicConfig(
        level=LOGGING_LEVEL,
        format="[%(levelname)s] %(message)s",
    )

    logger.info("Starting conversion")

    if len(sys.argv) < 3:
        logger.error("Expected input and output paths")
        raise ValueError("Usage: script.py <input.json> <output.ska>")

    input_path: str = sys.argv[1].removesuffix(IN_FORMAT) + IN_FORMAT
    output_path: str = sys.argv[2].removesuffix(OUT_FORMAT) + OUT_FORMAT

    logger.info("Input:  %s", input_path)
    logger.info("Output: %s", output_path)

    output = bytearray()

    input_json = read_json_file(input_path)

    if not isinstance(input_json, dict):
        logger.error("JSON root is not an object")
        raise TypeError("Expected the JSON root to be an object")

    logger.debug("JSON root keys: %s", list(input_json.keys()))

    canvas = input_json["canvas"]
    canvas_width: int = int(canvas["width"])
    canvas_height: int = int(canvas["height"])
    canvas_background: str = canvas["backgroundColor"]

    logger.info(
        "Canvas: %dx%d, background=%s",
        canvas_width,
        canvas_height,
        canvas_background,
    )

    animation = input_json["animation"]
    fps = animation["frameRate"]

    logger.info("Animation frame rate: %s FPS", fps)

    output += encode(
        f"{canvas_width},{canvas_height},{canvas_background},{fps}"
    )

    frames: list[dict] = input_json["frames"]

    logger.info("Total frames: %d", len(frames))

    old_matrix: list[list[Pixel]] | None = None

    for frame_index, frame in enumerate(frames):
        logger.info(
            "Processing frame %d/%d",
            frame_index + 1,
            len(frames),
        )

        content: str = frame["contentString"]
        lines = content.splitlines()

        logger.debug(
            "Frame %d: %d source lines, %d characters",
            frame_index,
            len(lines),
            len(content),
        )

        colors: dict = frame.get("colors", {})

        foreground_colors: dict = json.loads(
            colors.get("foreground", "{}")
        )
        background_colors: dict = json.loads(
            colors.get("background", "{}")
        )

        logger.debug(
            "Frame %d colors: %d foreground, %d background",
            frame_index,
            len(foreground_colors),
            len(background_colors),
        )

        pixel_matrix: list[list[Pixel | None]] = [
            [None for _ in range(canvas_width)]
            for _ in range(canvas_height)
        ]

        if len(lines) < canvas_height:
            missing_lines = canvas_height - len(lines)

            logger.debug(
                "Frame %d has %d missing rows; padding with spaces",
                frame_index,
                missing_lines,
            )

            lines += [" " * canvas_width] * missing_lines

        for y, line in enumerate(lines):
            line_length = len(line)

            if line_length > canvas_width:
                logger.error(
                    "Frame %d, row %d is too wide: %d > %d",
                    frame_index,
                    y,
                    line_length,
                    canvas_width,
                )

                raise ValueError(
                    f"Frame {frame_index}, row {y} is too wide: "
                    f"{line_length} > {canvas_width}"
                )

            if line_length < canvas_width:
                logger.debug(
                    "Frame %d, row %d padded: %d -> %d characters",
                    frame_index,
                    y,
                    line_length,
                    canvas_width,
                )

                line += " " * (canvas_width - line_length)

            for x, char in enumerate(line):
                colors_index = f"{x},{y}"

                foreground: str | None = foreground_colors.get(
                    colors_index,
                    None,
                )
                background: str | None = background_colors.get(
                    colors_index,
                    None,
                )

                pixel_matrix[y][x] = Pixel(
                    char,
                    foreground,
                    background,
                )

        logger.debug(
            "Frame %d pixel matrix populated: %dx%d",
            frame_index,
            canvas_width,
            canvas_height,
        )

        output += FRAME

        changed_characters = 0
        changed_foregrounds = 0
        changed_backgrounds = 0

        for y, row in enumerate(pixel_matrix):
            row_pixels: list[bytes] = []
            for x, cell in enumerate(row):
                if cell is None:
                    logger.error(
                        "Pixel matrix incomplete at frame %d (%d, %d)",
                        frame_index,
                        x,
                        y,
                    )

                    raise ValueError(
                        f"Pixel matrix is not fully filled at "
                        f"frame {frame_index} ({x}, {y})"
                    )

                pixel = bytearray()

                write_char: str | None = cell.character
                write_foreground: str | None = cell.foreground
                write_background: str | None = cell.background

                if old_matrix:
                    old = old_matrix[y][x]

                    if old.character == write_char:
                        write_char = None
                    else:
                        changed_characters += 1

                    if old.foreground == write_foreground:
                        write_foreground = None
                    else:
                        changed_foregrounds += 1

                        if write_foreground is None:
                            write_foreground = DEFAULT_FOREGROUND

                    if old.background == write_background:
                        write_background = None
                    else:
                        changed_backgrounds += 1

                        if write_background is None:
                            write_background = canvas_background
                else:
                    if write_char == " ":
                        write_char = None
                    else:
                        changed_characters += 1
                    if write_foreground is not None:
                        changed_foregrounds += 1
                    if write_background is not None:
                        changed_backgrounds += 1

                pixel += PIXEL

                if write_char:
                    pixel += PIXEL_DATA
                    pixel += encode(write_char)

                if write_foreground or write_background:
                    pixel += PIXEL_DATA

                    if write_foreground:
                        pixel += encode(write_foreground[1:])

                    if write_background:
                        pixel += PIXEL_DATA
                        pixel += encode(write_background[1:])

                row_pixels.append(bytes(pixel))

            while row_pixels and row_pixels[-1] == PIXEL:
                row_pixels.pop()

            for pixel in row_pixels:
                output += pixel

            output += ROW_END

        logger.info(
            "Frame %d encoded: %d character changes, "
            "%d foreground changes, %d background changes; "
            "output size=%d bytes",
            frame_index,
            changed_characters,
            changed_foregrounds,
            changed_backgrounds,
            len(output),
        )

        old_matrix = pixel_matrix  # type: ignore

    logger.info("Finished encoding %d frames", len(frames))

    write_file(output_path, bytes(output))

    logger.info(
        "Conversion complete: %d frames, %d output bytes",
        len(frames),
        len(output),
    )

    print(f"Converted successfully, file stored at {output_path}")


if __name__ == "__main__":
    main()
