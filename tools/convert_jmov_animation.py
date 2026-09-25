"""Convert JavE 5 ASCII movies (.jmov) to UTF-8 delta animations (.ska).

No third-party packages are required. The output defaults to the input path
with a .ska suffix. J: starts a full canvas; S:<x> <y> <image> overlays a
selection on that canvas without starting another frame. Image headers are
A<width> <height> <data> (row-based RLE) or B<width> <height> <data>
(uncompressed row-major text). In A images, %% is a literal percent, %0 ends
a row, and %N<char> repeats char N times. Digit and percent run characters
are escaped: %3%8 produces 888 and %3%% produces three percent signs.
A percent not followed by digits or another percent is literal. Missing cells
are spaces. C: supplies the global foreground/background colors.

In the supplied Movie Editor layout, the J: before the editor metadata/C:
record is the preview, and the J: records after it are animation frames.
--include-preview also encodes that preview. Without this metadata boundary,
all J: records are treated as frames. Identical animation frames are retained.
There is no timing field in the sample; FPS defaults to 10 and is configurable.
Other JavE image encodings/record types are rejected, not silently discarded.
Selections are placed at zero-based coordinates and clipped to the canvas.

SKA: header width,height,#background,fps; 01 starts each frame; 02 ends
 each row; 03 starts each pixel; 04 prefixes each positional field
(character, foreground, background). Empty fields mean unchanged. Colors in
pixel fields omit '#'. Trailing unchanged pixels are omitted, but every row
and frame is retained. The initial canvas is space/white/canvas-background.
"""

from __future__ import annotations

import argparse
import math
from pathlib import Path
import re
import sys
from typing import NamedTuple

FRAME = b"\x01"
ROW_END = b"\x02"
PIXEL = b"\x03"
PIXEL_DATA = b"\x04"
DEFAULT_FOREGROUND = "#ffffff"
DEFAULT_BACKGROUND = "#000000"


class Image(NamedTuple):
    width: int
    height: int
    rows: list[str]


class Movie(NamedTuple):
    frames: list[Image]
    foreground: str
    background: str
    preview_skipped: bool


def color(value: str) -> str:
    if not re.fullmatch(r"#[0-9a-fA-F]{6}", value):
        raise ValueError(f"Expected #RRGGBB color, got {value!r}")
    return value.lower()


def decode_image(record: str) -> Image:
    """Decode a J: record or an embedded A/B image, preserving whitespace."""
    record = record.removeprefix("J:")
    # Consume exactly one payload separator, not the image's leading spaces.
    match = re.fullmatch(r"([AB]?)([0-9]+)[ \t]+([0-9]+)(?:[ \t](.*))?", record)
    if match is None:
        raise ValueError("Expected a JavE A/B image: <type><width> <height> <data>")
    kind = match[1] or "A"
    width, height = int(match[2]), int(match[3])
    if width <= 0 or height <= 0:
        raise ValueError("Canvas dimensions must be positive")
    payload = match[4] or ""
    if any(c in payload for c in "\x01\x02\x03\x04\r\n"):
        raise ValueError("Image contains a reserved SKA delimiter or line break")
    if kind == "B":
        if len(payload) > width * height:
            raise ValueError(f"Image exceeds its declared {width}x{height} canvas")
        data = payload.ljust(width * height)
        return Image(width, height, [data[y * width:(y + 1) * width]
                                     for y in range(height)])

    rows: list[str] = []
    parts: list[str] = []
    row_width = 0
    i = 0
    while i < len(payload):
        character = payload[i]
        count = 1
        i += 1
        if character == "%":
            if i < len(payload) and payload[i] == "%":
                i += 1
            else:
                start = i
                while i < len(payload) and "0" <= payload[i] <= "9":
                    i += 1
                if i > start:
                    count = int(payload[start:i])
                    if count == 0:
                        if len(rows) >= height:
                            raise ValueError("Image has more rows than its declared height")
                        rows.append("".join(parts).ljust(width))
                        parts = []
                        row_width = 0
                        continue
                    if i == len(payload):
                        raise ValueError("Repeat count has no following character")
                    character = payload[i]
                    i += 1
                    if character == "%":
                        # JavE escapes the run character, not just percent:
                        # %4%8 is four '8' characters, %4%% is four '%'.
                        if i == len(payload):
                            raise ValueError("Incomplete escaped repeat character")
                        character = payload[i]
                        i += 1
                # Otherwise this is a bare literal percent; leave the next
                # character for the next iteration.
        if len(rows) >= height or row_width + count > width:
            raise ValueError(f"Image exceeds its declared {width}x{height} canvas")
        parts.append(character * count)
        row_width += count
    if len(rows) < height:
        rows.append("".join(parts).ljust(width))
    rows.extend([" " * width] * (height - len(rows)))
    return Image(width, height, rows)


def overlay_selection(canvas: Image, record: str) -> Image:
    """Flatten a floating selection onto its preceding J: canvas."""
    match = re.fullmatch(r"S:([-+]?[0-9]+)[ \t]+([-+]?[0-9]+)[ \t]+(.*)", record)
    if match is None:
        raise ValueError("Expected S:<x> <y> <image>")
    x, y = int(match[1]), int(match[2])
    selection = decode_image(match[3])
    rows = canvas.rows.copy()
    left, right = max(0, x), min(canvas.width, x + selection.width)
    if left < right:
        for target_y in range(max(0, y), min(canvas.height, y + selection.height)):
            row = rows[target_y]
            patch = selection.rows[target_y - y][left - x:right - x]
            rows[target_y] = row[:left] + patch + row[right:]
    return Image(canvas.width, canvas.height, rows)


def read_movie(path: Path, encoding: str = "utf-8-sig", *,
               include_preview: bool = False) -> Movie:
    text = path.read_text(encoding=encoding)
    images: list[tuple[int, Image]] = []
    foreground, background = DEFAULT_FOREGROUND, DEFAULT_BACKGROUND
    color_line: int | None = None
    signature_seen = False
    for line_number, raw in enumerate(text.split("\n"), 1):
        line = raw.removesuffix("\r")
        if not line:
            continue
        try:
            if line.startswith("*:JavE"):
                signature_seen = True
            elif line.startswith("J:"):
                images.append((line_number, decode_image(line)))
            elif line.startswith("S:"):
                if not images:
                    raise ValueError("Selection has no preceding J: canvas")
                number, canvas = images[-1]
                images[-1] = (number, overlay_selection(canvas, line))
            elif line.startswith("C:"):
                if color_line is not None:
                    raise ValueError("Multiple C: records/per-frame colors are not supported")
                values = line[2:].split()
                if len(values) != 2:
                    raise ValueError("C: must contain foreground and background colors")
                foreground, background = map(color, values)
                color_line = line_number
            elif line.startswith(("D:", "|:", "^:")):
                pass  # Timestamp and editor coordinates, not animation data.
            else:
                raise ValueError(f"Unsupported JavE record: {line[:60]!r}")
        except ValueError as exc:
            raise ValueError(f"{path.name}, line {line_number}: {exc}") from exc
    if not signature_seen:
        raise ValueError("Missing JavE file signature")
    skipped = False
    if not include_preview and color_line is not None:
        before = [image for number, image in images if number < color_line]
        after = [(number, image) for number, image in images if number > color_line]
        if len(before) == 1 and after:
            images = after
            skipped = True
        elif len(before) > 1:
            raise ValueError("Ambiguous preview layout; use --include-preview to keep all images")
    frames = [image for _, image in images]
    if not frames:
        raise ValueError("No animation frames found")
    size = frames[0][:2]
    for index, image in enumerate(frames):
        if image[:2] != size:
            raise ValueError(f"Frame {index} has a different canvas size")
    return Movie(frames, foreground, background, skipped)


def encode_pixel(old: tuple[str, str, str], new: tuple[str, str, str]) -> bytes:
    fields = [value if value != previous else ""
              for previous, value in zip(old, new)]
    for index in (1, 2):
        fields[index] = fields[index].removeprefix("#")
    while fields and not fields[-1]:
        fields.pop()
    # Preserve an empty character field for foreground/background-only changes.
    return PIXEL + b"".join(PIXEL_DATA + field.encode("utf-8") for field in fields)


def encode_movie(movie: Movie, fps: float = 10.0) -> bytes:
    if not math.isfinite(fps) or fps <= 0:
        raise ValueError("FPS must be a positive finite number")
    if not movie.frames:
        raise ValueError("No animation frames found")
    width, height, _ = movie.frames[0]
    output = bytearray(f"{width},{height},{movie.background},{fps:g}".encode("utf-8"))
    previous: Image | None = None
    for image in movie.frames:
        output += FRAME
        for y, row in enumerate(image.rows):
            pixels: list[bytes] = []
            for x, character in enumerate(row):
                old = ((previous.rows[y][x], movie.foreground, movie.background)
                       if previous is not None
                       else (" ", DEFAULT_FOREGROUND, movie.background))
                pixels.append(encode_pixel(old, (character, movie.foreground, movie.background)))
            while pixels and pixels[-1] == PIXEL:
                pixels.pop()
            output += b"".join(pixels) + ROW_END
        previous = image
    return bytes(output)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__,
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("input", type=Path, help="Input .jmov file")
    parser.add_argument("output", type=Path, nargs="?", help="Output .ska file (optional)")
    parser.add_argument("--fps", type=float, default=10.0, help="Frame rate (default: 10)")
    parser.add_argument("--encoding", default="utf-8-sig",
                        help="Input text encoding; e.g. cp1252 for legacy files")
    parser.add_argument("--include-preview", action="store_true",
                        help="Also encode the initial editor preview image")
    parser.add_argument("--foreground", help="Override global foreground (#RRGGBB)")
    parser.add_argument("--background", help="Override canvas background (#RRGGBB)")
    args = parser.parse_args()
    source = args.input
    if not source.suffix:
        source = source.with_suffix(".jmov")
    destination = args.output or source.with_suffix(".ska")
    if not destination.suffix:
        destination = destination.with_suffix(".ska")
    try:
        if source.resolve() == destination.resolve():
            raise ValueError("Input and output paths must be different")
        movie = read_movie(source, args.encoding, include_preview=args.include_preview)
        if args.foreground:
            movie = movie._replace(foreground=color(args.foreground))
        if args.background:
            movie = movie._replace(background=color(args.background))
        data = encode_movie(movie, args.fps)
        destination.write_bytes(data)
    except (OSError, ValueError, LookupError) as exc:
        print(f"Error: {exc}", file=sys.stderr)
        return 1
    width, height, _ = movie.frames[0]
    print(f"Converted {len(movie.frames)} frames, {width}x{height}, {args.fps:g} FPS "
          f"-> {destination} ({len(data)} bytes)")
    if movie.preview_skipped:
        print("Skipped editor preview; use --include-preview to retain it.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
