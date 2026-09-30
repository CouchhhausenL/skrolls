# JavE · SKA animation editor

Open **index.html** in a current Chrome, Edge, or Firefox browser. There is no installation, build step, server, or network dependency. All animation data stays in your browser; **Save .ska** downloads a file rather than overwriting the source file.

## JavE 5.0 Features & Editing Tools

The editor has been adapted to function like **JavE 5.0 (Java Ascii Versatile Editor)** with comprehensive ASCII-art and animation capabilities:

### Tool Palette
- **Selection Tools:**
  - **Rectangle Selection (⛶):** Drag to select a rectangular area. Drag the selection to move it, press `Ctrl+C` to copy, `Ctrl+X` to cut, `Ctrl+V` to paste, `Del` to clear.
  - **Lasso Selection (⚿):** Freehand outline a custom boundary polygon to select cells inside it via ray-casting.
- **Direct Text Editing (🗛):** Click anywhere on the canvas to place a text cursor. Type directly with your keyboard, navigate with arrow keys, and use Backspace/Enter.
- **Freehand Drawing:**
  - **Freehand Generic (✎):** Draw freehand strokes with customizable **Thickness** (1–15) and **Opacity** (10%–100% color blending).
  - **Freehand Algorithmic (ᛘ):** Draw freehand lines automatically converted by the JavE structural algorithm into directional ASCII art (`-`, `|`, `/`, `\`, and corners `+` or box characters `┌─┐│└─┘`).
- **Lines:**
  - **Line Generic (╱):** Straight lines with thickness and opacity.
  - **Line Algorithmic (✛):** Structural ASCII lines calculated using JavE angle detection and stepped Bresenham paths.
- **Rectangles:**
  - **Rectangle Generic (▭):** Outlined rectangles with brush character, thickness, and opacity.
  - **Rectangle Algorithmic (⊞):** Structural ASCII rectangles with corners (`+` or `┌┐└┘`) and edges (`-`, `|`).
- **Ellipses:**
  - **Ellipse Generic (◯):** Ellipses with brush character, thickness, and opacity.
  - **Ellipse Algorithmic (⨁):** Structural curved ASCII ellipses with quadrant-specific curve glyphs.
- **Bézier Pen:**
  - **Bézier Generic (∿):** Interactive 3-point Bézier curve (Start, End, Control handle) with thickness and opacity.
  - **Bézier Algorithmic (⟿):** Structural ASCII Bézier curves with tangent-derived directional glyphs.
- **Pattern Brush (▦):** Paints using custom repeating 1D or 2D ASCII art patterns (Diagonals, Shading Blocks, Bricks, Crosses, Dots, Waves, or freely user-defined patterns).
- **Eraser (⌫):** Clears cells to spaces and canvas background with configurable thickness.
- **Colorize / Einfärben (🎨):** Paints colors (`fg` and/or `bg`) without touching existing characters.
- **Pipette / Eyedropper (🗲):** Copies character and colors from clicked cell.
- **Pan Tool (✋):** Pan the canvas viewport (also available via Middle Mouse Button).

### Reference Tracing Image
- Load background images (`.png`, `.jpg`, `.webp`, `.svg`, etc.) with variable **Opacity (0%–100%)**.
- Choose between **Contain**, **Stretch / Fill**, or **Original 1:1** fit modes.
- Toggle visibility on and off to easily trace real images into ASCII art!

### Timeline & Frame Reordering
- **Drag & Drop:** Reorder frames directly by dragging cards along the timeline.
- **Move Controls:** Use **◀ Move** and **Move ▶** to shift frames left or right.
- **Animation Controls:** Play/Pause (`Space`), `+ Blank` frame, `Duplicate`, `Delete`. Edits are frame-independent with full undo/redo.

### Shortcuts

| Shortcut | Action |
| --- | --- |
| Ctrl + Z | Undo |
| Ctrl + Y / Ctrl + Shift + Z | Redo |
| Ctrl + C / X / V | Copy / Cut / Paste selection |
| Delete / Backspace | Clear selection (or erase in text mode) |
| Escape | Deselect / Exit text mode / Cancel Bézier |
| Enter | Commit floating selection / Newline in text mode |
| A / Left arrow | Previous frame |
| D / Right arrow | Next frame |
| Space | Play / stop |
| # | Toggle grid |
| Middle mouse | Pan viewport |
| Mouse wheel | Zoom |

Text fields retain their normal typing and editing shortcuts. Ctrl + Shift + Z also redoes; Command is accepted in place of Ctrl. The color wheel supports arrow keys: left/right change hue, up/down change saturation.

## SKA compatibility

The editor imports and exports **SKA only**, not the intermediate JSON format. It uses the provided converter's UTF-8 byte layout:

- Header: `width,height,#background,fps`.
- `01` starts each frame; `02` ends each row; `03` starts each pixel; `04` introduces component data.
- Initial state: space, `#ffffff`, and canvas background.
- Unchanged components inherit from the previous frame. Unchanged trailing pixels are omitted; leading/interior placeholders and empty frames are preserved.
- Color values in pixel data contain six hex digits without `#`. Resets export explicit default colors.

**Important compact-field detail:** the converter omits the character field entirely when it is unchanged. Therefore, following byte `03`, foreground-only data is `04 rrggbb`, background-only data is `04 04 rrggbb`, and character-plus-background data is `04 character 04 04 rrggbb`. A single printable code point identifies a character field; six hex digits identify a color field. This is not a generic fixed-position, three-column split.

The importer also accepts the older `NONE` color reset marker and unambiguous three-field records with an explicit empty character slot, found in the existing project assets. It interprets legacy `NONE` as resetting that color to its document default, not as the brush's leave-unchanged setting. Exports normalize these to the provided converter's compact fields and explicit color resets. Equivalent files may not be byte-for-byte identical after export.

Malformed UTF-8, invalid colors or geometry, control characters, and oversized inputs are rejected without replacing the open document. Limits are defined in `js/ska.js`: 300 columns × 200 rows, 2,000 frames, 2,000,000 total cells, 32 MB files, and 0.1–240 FPS. Undo retains 2–64 transactions depending on document size. The renderer caps its backing bitmap to avoid excessive GPU allocation at high zoom.

## Files

```text
index.html                  Application markup; open this file
styles.css                  Theme, panels, workspace, and responsive layout
js/ska.js                   Pure SKA codec and validation
js/model.js                 Document state, immutable snapshots, editing/history
js/renderer.js              Canvas rendering, geometry, zoom
js/colors.js                Color wheel, None states, favorites
js/symbols.js               Character input, collections, and search
js/timeline.js              Timeline, frame controls, playback
js/gestures.js              Mouse tools, stroke interpolation, keyboard shortcuts
js/app.js                   Initialization, document settings, file import/export
examples/comet.ska          Small sample animation
tests/core.test.cjs         Dependency-free codec/model tests
tests/browser_smoke.py      Optional browser integration tests
tests/generate-example.cjs  Rebuilds the example
SOURCE.md                   Text source files, each under its filename in a code block
```

Scripts use small isolated scopes and explicit globals instead of ES module imports so the editor also works directly from `file://`. There are no external fonts, libraries, analytics, or requests.

## Tests

From this folder, with Node.js 22 or later:

```powershell
node tests/core.test.cjs
```

Append paths to existing `.ska` files to include visual-state round-trip checks:

```powershell
node tests/core.test.cjs "C:\path\animation.ska"
```

Optional UI tests require Python with Playwright and an installed Chrome browser:

```powershell
python tests/browser_smoke.py
```

Set `SKA_TEST_BROWSER` to another Chromium browser executable if necessary. Browser tests use a temporary profile under `tests`, create test exports and `tests/editor.png`, and save their report to `tests/browser-results.txt`. They cover drawing, erasing, picking, None fills, shortcuts, playback, independent frames, color controls, malformed import, and actual downloads. Additional asset paths may be passed to this script as well.