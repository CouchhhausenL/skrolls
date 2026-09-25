# SKA animation editor

Open **index.html** in a current Chrome, Edge, or Firefox browser. There is no installation, build step, server, or network dependency. All animation data stays in your browser; **Save .ska** downloads a file rather than overwriting the source file.

Try **Open .ska → examples/comet.ska**, then press **Space**. The initial document is a blank 40 × 20 animation.

## Editing

- **Left mouse:** pencil. Drag to draw a continuous stroke.
- **Right mouse:** eraser. Writes a space, white foreground, and the document's background.
- **Middle mouse:** picker. Copies the character and both colors. **Ctrl + middle mouse** copies only the colors.
- Each brush component has a **None** checkbox. None means leave the current cell component unchanged, not transparent or reset. Picking or choosing a value enables that component again.
- **Fill frame** applies the brush to every cell, respecting all three None settings.
- Click **Foreground** or **Background** to choose which color the wheel edits. Use the brightness slider or enter a six-digit hex value. **Swap** exchanges both color settings, including their None states. **Reset colors** enables white foreground and the document background; it does not change the character.
- **☆** saves the current wheel color to favorites. Click a favorite to use it; right-click to remove it. Favorites persist through browser storage when available. They are not embedded in the SKA file.
- Type one character directly, or choose one from **Essentials** or **In animation**. Search by character, category (such as `arrows`, `blocks`, `space`), or code point (`U+2588`). Unicode symbols are supported; font coverage varies. A cell holds one Unicode code point, not a multi-code-point emoji sequence.

## Timeline and document settings

Click any thumbnail to inspect that frame's fully reconstructed state. **+ Blank** inserts a default-filled frame after the current frame; **Duplicate** copies it. **Delete** removes it, keeping at least one frame. Playback loops at the document FPS and stops when drawing or modifying the document.

Edits affect **only the selected frame**. Later frames retain their visual state; the exporter recalculates their deltas. A mouse stroke, fill, frame operation, resize, FPS change, or background change is one undo step. Brush selection, zoom, favorites, grid visibility, and the download filename are not document history.

- **Apply size** resizes every frame, anchored at the top-left. New cells use defaults; cropped cells can be recovered with Undo.
- **Canvas background** changes the header and all cells matching the previous canvas background. Custom backgrounds remain unchanged. SKA stores color values, so explicitly painting a color equal to the old default is indistinguishable from using that default.
- **File name** controls the exported filename. `.ska` is appended automatically.
- Scroll over the workspace to zoom around the pointer. Use the viewport scrollbars to move around a large canvas, or click **Fit**.
- Unsaved changes trigger a warning before replacing the animation or closing the page. There is no automatic animation backup: download your work regularly.

### Shortcuts

| Shortcut | Action |
| --- | --- |
| Ctrl + Z | Undo |
| Ctrl + Y | Redo |
| A / Left arrow | Previous frame |
| D / Right arrow | Next frame |
| Space | Play / stop |
| # | Toggle grid |
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