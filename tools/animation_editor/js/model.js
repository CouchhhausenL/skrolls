(() => {
  'use strict';
  const { blankFrame, DEFAULT_FG, validateHeader, validateSize } = SKA;

  // Hex color blending helper
  function blendColor(baseHex, brushHex, alpha) {
    if (!brushHex || alpha >= 1) return brushHex || baseHex;
    if (alpha <= 0) return baseHex;
    try {
      const r1 = parseInt(baseHex.slice(1, 3), 16), g1 = parseInt(baseHex.slice(3, 5), 16), b1 = parseInt(baseHex.slice(5, 7), 16);
      const r2 = parseInt(brushHex.slice(1, 3), 16), g2 = parseInt(brushHex.slice(3, 5), 16), b2 = parseInt(brushHex.slice(5, 7), 16);
      const r = Math.max(0, Math.min(255, Math.round(r1 * (1 - alpha) + r2 * alpha))).toString(16).padStart(2, '0');
      const g = Math.max(0, Math.min(255, Math.round(g1 * (1 - alpha) + g2 * alpha))).toString(16).padStart(2, '0');
      const b = Math.max(0, Math.min(255, Math.round(b1 * (1 - alpha) + b2 * alpha))).toString(16).padStart(2, '0');
      return `#${r}${g}${b}`;
    } catch {
      return brushHex;
    }
  }

  // Internal clipboard shared across model instances
  let sharedClipboard = null;

  class EditorModel extends EventTarget {
    constructor() {
      super();
      this.brush = { char: '@', fg: DEFAULT_FG, bg: null };
      this.selection = null;
      this.load(this.createDocument());
    }

    createDocument() {
      return { width: 40, height: 20, background: '#171a21', fps: 12, frames: [blankFrame(40, 20, '#171a21')] };
    }

    emit(kind = 'document') {
      this.dispatchEvent(new CustomEvent('change', { detail: kind }));
    }

    load(doc) {
      this.doc = doc;
      this.index = 0;
      this.undoStack = [];
      this.redoStack = [];
      this.pending = null;
      this.saved = doc;
      this.selection = null;
      this.emit('load');
    }

    get frame() { return this.doc.frames[this.index]; }
    get dirty() { return this.doc !== this.saved; }

    markSaved() { this.saved = this.doc; this.emit('saved'); }

    begin() {
      if (this.pending) return;
      this.pending = { doc: this.doc, index: this.index };
      this.doc = { ...this.doc, frames: this.doc.frames.slice() };
      this.copiedFrame = false;
      this.changed = false;
    }

    commit() {
      if (!this.pending) return;
      if (this.changed) {
        this.undoStack.push(this.pending);
        const limit = Math.max(2, Math.min(64, Math.floor(4000000 / (this.doc.width * this.doc.height * this.doc.frames.length))));
        if (this.undoStack.length > limit) this.undoStack.splice(0, this.undoStack.length - limit);
        this.redoStack = [];
      } else {
        this.doc = this.pending.doc;
      }
      this.pending = null;
      this.emit('commit');
    }

    mutate(action) {
      this.commit();
      this.begin();
      try {
        action();
        this.changed = true;
        this.commit();
      } catch (error) {
        this.doc = this.pending.doc;
        this.index = this.pending.index;
        this.pending = null;
        this.emit();
        throw error;
      }
    }

    // Classic paint method preserving backwards compatibility
    paint(indices, erase = false) {
      this.begin();
      const brush = erase ? { char: ' ', fg: DEFAULT_FG, bg: this.doc.background } : this.brush;
      let changed = false;
      for (const i of indices) {
        const old = this.frame[i];
        if (!old) continue;
        const next = { char: brush.char ?? old.char, fg: brush.fg ?? old.fg, bg: brush.bg ?? old.bg };
        if (next.char === old.char && next.fg === old.fg && next.bg === old.bg) continue;
        if (!this.copiedFrame) {
          this.doc.frames[this.index] = this.frame.slice();
          this.copiedFrame = true;
        }
        this.frame[i] = next;
        changed = true;
      }
      if (changed) { this.changed = true; this.emit('paint'); }
    }

    // Extended paint method supporting opacity, custom character per cell, eraser, and colorize
    paintCells(cells, options = {}) {
      if (!cells || cells.length === 0) return;
      this.begin();

      const {
        opacity = 1.0,
        isEraser = false,
        isColorize = false
      } = options;

      let changed = false;
      const width = this.doc.width, height = this.doc.height;

      for (const item of cells) {
        let x, y, char, fg, bg;
        if (typeof item === 'number') {
          x = item % width;
          y = Math.floor(item / width);
          char = this.brush.char;
          fg = this.brush.fg;
          bg = this.brush.bg;
        } else {
          x = item.x !== undefined ? item.x : (item.index % width);
          y = item.y !== undefined ? item.y : Math.floor(item.index / width);
          char = item.char !== undefined ? item.char : this.brush.char;
          fg = item.fg !== undefined ? item.fg : this.brush.fg;
          bg = item.bg !== undefined ? item.bg : this.brush.bg;
        }

        if (x < 0 || x >= width || y < 0 || y >= height) continue;
        const idx = y * width + x;
        const old = this.frame[idx];
        if (!old) continue;

        let next;
        if (isEraser) {
          next = { char: ' ', fg: DEFAULT_FG, bg: this.doc.background };
        } else if (isColorize) {
          // Colorize only changes fg and bg, never char!
          const nextFg = fg ? blendColor(old.fg, fg, opacity) : old.fg;
          const nextBg = bg ? blendColor(old.bg, bg, opacity) : old.bg;
          next = { char: old.char, fg: nextFg, bg: nextBg };
        } else {
          // Standard / algorithmic painting with opacity blending
          const nextChar = char ?? old.char;
          const cellOpacity = item.opacity !== undefined ? item.opacity : opacity;
          const nextFg = fg ? blendColor(old.fg, fg, cellOpacity) : old.fg;
          const nextBg = bg ? blendColor(old.bg, bg, cellOpacity) : old.bg;
          next = { char: nextChar, fg: nextFg, bg: nextBg };
        }

        if (next.char === old.char && next.fg === old.fg && next.bg === old.bg) continue;

        if (!this.copiedFrame) {
          this.doc.frames[this.index] = this.frame.slice();
          this.copiedFrame = true;
        }
        this.frame[idx] = next;
        changed = true;
      }

      if (changed) {
        this.changed = true;
        this.emit('paint');
      }
    }

    // Type a single character at (x, y) like a text editor
    typeCharacter(x, y, char) {
      if (x < 0 || x >= this.doc.width || y < 0 || y >= this.doc.height) return;
      this.mutate(() => {
        const idx = y * this.doc.width + x;
        const old = this.frame[idx];
        const next = {
          char: char,
          fg: this.brush.fg ?? old.fg,
          bg: this.brush.bg ?? old.bg
        };
        const newFrame = this.frame.slice();
        newFrame[idx] = next;
        this.doc.frames[this.index] = newFrame;
      });
    }

    fill() {
      this.commit();
      this.paint(Array.from({ length: this.frame.length }, (_, i) => i));
      this.commit();
    }

    pick(i, colorsOnly) {
      const pixel = this.frame[i];
      if (!pixel) return;
      this.brush.fg = pixel.fg;
      this.brush.bg = pixel.bg;
      if (!colorsOnly) this.brush.char = pixel.char;
      this.emit('brush');
    }

    select(index) {
      this.commit();
      this.index = Math.max(0, Math.min(index, this.doc.frames.length - 1));
      this.emit('selection');
    }

    travel(redo = false) {
      this.commit();
      const from = redo ? this.redoStack : this.undoStack;
      const to = redo ? this.undoStack : this.redoStack;
      const snapshot = from.pop();
      if (!snapshot) return;
      to.push({ doc: this.doc, index: this.index });
      this.doc = snapshot.doc;
      this.index = snapshot.index;
      this.selection = null;
      this.emit('history');
    }

    insert(duplicate = false) {
      validateSize(this.doc.width, this.doc.height, this.doc.frames.length + 1);
      this.mutate(() => {
        const frame = duplicate ? this.frame.slice() : blankFrame(this.doc.width, this.doc.height, this.doc.background);
        this.doc.frames.splice(++this.index, 0, frame);
      });
    }

    remove() {
      if (this.doc.frames.length === 1) return;
      this.mutate(() => {
        this.doc.frames.splice(this.index, 1);
        this.index = Math.min(this.index, this.doc.frames.length - 1);
      });
    }

    // Reorder frame fromIndex to toIndex (Timeline drag and drop / move)
    reorderFrame(fromIndex, toIndex) {
      if (fromIndex === toIndex) return;
      if (fromIndex < 0 || fromIndex >= this.doc.frames.length) return;
      if (toIndex < 0 || toIndex >= this.doc.frames.length) return;

      this.mutate(() => {
        const [moved] = this.doc.frames.splice(fromIndex, 1);
        this.doc.frames.splice(toIndex, 0, moved);
        this.index = toIndex;
      });
    }

    moveFrame(direction) {
      const target = this.index + direction;
      if (target >= 0 && target < this.doc.frames.length) {
        this.reorderFrame(this.index, target);
      }
    }

    resize(width, height) {
      validateHeader({ ...this.doc, width, height });
      validateSize(width, height, this.doc.frames.length);
      if (width === this.doc.width && height === this.doc.height) return;
      this.mutate(() => {
        const oldWidth = this.doc.width, oldHeight = this.doc.height;
        this.doc.frames = this.doc.frames.map(frame => {
          const resized = blankFrame(width, height, this.doc.background);
          for (let y = 0; y < Math.min(height, oldHeight); y++) {
            for (let x = 0; x < Math.min(width, oldWidth); x++) resized[y * width + x] = frame[y * oldWidth + x];
          }
          return resized;
        });
        this.doc.width = width;
        this.doc.height = height;
      });
    }

    setBackground(background) {
      validateHeader({ ...this.doc, background });
      if (background === this.doc.background) return;
      this.mutate(() => {
        const old = this.doc.background;
        this.doc.frames = this.doc.frames.map(frame => frame.map(cell => cell.bg === old ? { ...cell, bg: background } : cell));
        this.doc.background = background;
      });
    }

    setFps(fps) {
      validateHeader({ ...this.doc, fps });
      if (fps !== this.doc.fps) this.mutate(() => { this.doc.fps = fps; });
    }

    replaceColor(char, fg = null, bg = null) {
      if (fg === null && bg === null) return 0;
      let count = 0;
      this.mutate(() => {
        this.doc.frames = this.doc.frames.map(frame => {
          let frameChanged = false;
          const newFrame = frame.map(cell => {
            if (cell.char === char) {
              const nextFg = fg !== null ? fg : cell.fg;
              const nextBg = bg !== null ? bg : cell.bg;
              if (nextFg !== cell.fg || nextBg !== cell.bg) {
                frameChanged = true;
                count++;
                return { ...cell, fg: nextFg, bg: nextBg };
              }
            }
            return cell;
          });
          return frameChanged ? newFrame : frame;
        });
      });
      return count;
    }

    applyColorsToAllFrames() {
      if (this.doc.frames.length <= 1) return 0;
      const currentFrame = this.frame;
      let updatedFramesCount = 0;
      this.mutate(() => {
        this.doc.frames = this.doc.frames.map((frame, idx) => {
          if (idx === this.index) return frame;
          updatedFramesCount++;
          return frame.map((cell, i) => ({
            ...cell,
            fg: currentFrame[i].fg,
            bg: currentFrame[i].bg
          }));
        });
      });
      return updatedFramesCount;
    }

    applyColorsToNextFrames() {
      if (this.index >= this.doc.frames.length - 1) return 0;
      const currentFrame = this.frame;
      let updatedFramesCount = 0;
      this.mutate(() => {
        this.doc.frames = this.doc.frames.map((frame, idx) => {
          if (idx <= this.index) return frame;
          updatedFramesCount++;
          return frame.map((cell, i) => ({
            ...cell,
            fg: currentFrame[i].fg,
            bg: currentFrame[i].bg
          }));
        });
      });
      return updatedFramesCount;
    }

    // --- SELECTION ENGINE (RECTANGLE & LASSO, MOVE, COPY, PASTE, CUT) ---

    // Set active selection
    setSelection(type, cells, bounds) {
      this.commit();
      const points = new Set();
      const pixels = [];

      for (const c of cells) {
        points.add(`${c.x},${c.y}`);
        const idx = c.y * this.doc.width + c.x;
        const cellData = this.frame[idx] || { char: ' ', fg: DEFAULT_FG, bg: this.doc.background };
        pixels.push({
          relX: c.x - bounds.minX,
          relY: c.y - bounds.minY,
          x: c.x,
          y: c.y,
          char: cellData.char,
          fg: cellData.fg,
          bg: cellData.bg
        });
      }

      this.selection = {
        type, // 'rect' or 'lasso'
        bounds, // { minX, minY, maxX, maxY, width, height }
        points, // Set of 'x,y'
        pixels, // Array of { relX, relY, char, fg, bg }
        floating: false,
        liftedOriginals: null,
        offsetX: 0,
        offsetY: 0
      };

      this.emit('selection-changed');
    }

    // Lift selection to float so it can be moved around
    liftSelection() {
      if (!this.selection || this.selection.floating) return;
      this.begin();

      const newFrame = this.frame.slice();
      const liftedOriginals = [];
      const emptyCell = { char: ' ', fg: DEFAULT_FG, bg: this.doc.background };

      for (const p of this.selection.pixels) {
        const idx = p.y * this.doc.width + p.x;
        liftedOriginals.push({ idx, cell: newFrame[idx] });
        newFrame[idx] = { ...emptyCell };
      }

      this.doc.frames[this.index] = newFrame;
      this.copiedFrame = true;
      this.changed = true;
      this.selection.floating = true;
      this.selection.liftedOriginals = liftedOriginals;
      this.emit('selection-lifted');
    }

    // Move floating selection offset
    moveSelection(dx, dy) {
      if (!this.selection) return;
      if (!this.selection.floating) {
        this.liftSelection();
      }
      this.selection.offsetX = dx;
      this.selection.offsetY = dy;
      this.emit('selection-moved');
    }

    // Commit floating selection into current frame
    commitSelection() {
      if (!this.selection) return;

      if (this.selection.floating) {
        this.mutate(() => {
          const newFrame = this.frame.slice();
          const targetStartX = this.selection.bounds.minX + this.selection.offsetX;
          const targetStartY = this.selection.bounds.minY + this.selection.offsetY;

          for (const p of this.selection.pixels) {
            const tx = targetStartX + p.relX;
            const ty = targetStartY + p.relY;
            if (tx >= 0 && tx < this.doc.width && ty >= 0 && ty < this.doc.height) {
              const idx = ty * this.doc.width + tx;
              newFrame[idx] = { char: p.char, fg: p.fg, bg: p.bg };
            }
          }
          this.doc.frames[this.index] = newFrame;
        });
      }

      this.selection = null;
      this.emit('selection-committed');
    }

    // Cancel selection without dropping floating pixels
    cancelSelection() {
      if (!this.selection) return;

      if (this.selection.floating && this.selection.liftedOriginals) {
        this.mutate(() => {
          const newFrame = this.frame.slice();
          for (const item of this.selection.liftedOriginals) {
            newFrame[item.idx] = item.cell;
          }
          this.doc.frames[this.index] = newFrame;
        });
      }

      this.selection = null;
      this.emit('selection-cancelled');
    }

    // Delete selected cells
    deleteSelection() {
      if (!this.selection) return;

      if (!this.selection.floating) {
        this.mutate(() => {
          const newFrame = this.frame.slice();
          const emptyCell = { char: ' ', fg: DEFAULT_FG, bg: this.doc.background };
          for (const p of this.selection.pixels) {
            const idx = p.y * this.doc.width + p.x;
            newFrame[idx] = { ...emptyCell };
          }
          this.doc.frames[this.index] = newFrame;
        });
      }

      this.selection = null;
      this.emit('selection-deleted');
    }

    // Copy selection to clipboard
    copySelection() {
      if (!this.selection) return null;
      const clip = {
        width: this.selection.bounds.width,
        height: this.selection.bounds.height,
        pixels: this.selection.pixels.map(p => ({ ...p }))
      };
      sharedClipboard = clip;

      // Also generate plain ASCII text for OS clipboard
      try {
        const grid = Array.from({ length: clip.height }, () => Array(clip.width).fill(' '));
        for (const p of clip.pixels) {
          if (p.relY >= 0 && p.relY < clip.height && p.relX >= 0 && p.relX < clip.width) {
            grid[p.relY][p.relX] = p.char;
          }
        }
        const text = grid.map(row => row.join('')).join('\n');
        if (navigator.clipboard && navigator.clipboard.writeText) {
          navigator.clipboard.writeText(text).catch(() => {});
        }
      } catch { /* Clipboard access optional */ }

      return clip;
    }

    // Cut selection
    cutSelection() {
      if (!this.selection) return;
      this.copySelection();
      this.deleteSelection();
    }

    // Paste clipboard into current frame as floating selection
    pasteClipboard(targetX = 0, targetY = 0) {
      if (!sharedClipboard) return false;
      this.commitSelection(); // Commit any previous selection first

      const bounds = {
        minX: targetX,
        minY: targetY,
        maxX: targetX + sharedClipboard.width - 1,
        maxY: targetY + sharedClipboard.height - 1,
        width: sharedClipboard.width,
        height: sharedClipboard.height
      };

      const points = new Set();
      const pixels = sharedClipboard.pixels.map(p => {
        points.add(`${targetX + p.relX},${targetY + p.relY}`);
        return {
          ...p,
          x: targetX + p.relX,
          y: targetY + p.relY
        };
      });

      this.selection = {
        type: 'rect',
        bounds,
        points,
        pixels,
        floating: true,
        liftedOriginals: [],
        offsetX: 0,
        offsetY: 0
      };

      this.emit('selection-pasted');
      return true;
    }

    getClipboard() {
      return sharedClipboard;
    }

    adjustSelection(dirX, dirY) {
      if (!this.selection) return false;
      if (this.selection.floating) {
        this.selection.offsetX += dirX;
        this.selection.offsetY += dirY;
        this.emit('selection-moved');
        return true;
      }

      const doc = this.doc;
      const b = this.selection.bounds;
      
      if (this.selection.anchorX === undefined) {
        this.selection.anchorX = b.minX;
        this.selection.activeEndX = b.maxX;
      }
      if (this.selection.anchorY === undefined) {
        this.selection.anchorY = b.minY;
        this.selection.activeEndY = b.maxY;
      }

      if (dirX !== 0) {
        let target = this.selection.activeEndX + dirX;
        target = Math.max(0, Math.min(doc.width - 1, target));
        this.selection.activeEndX = target;
      }

      if (dirY !== 0) {
        let target = this.selection.activeEndY + dirY;
        target = Math.max(0, Math.min(doc.height - 1, target));
        this.selection.activeEndY = target;
      }

      const minX = Math.min(this.selection.anchorX, this.selection.activeEndX);
      const maxX = Math.max(this.selection.anchorX, this.selection.activeEndX);
      const minY = Math.min(this.selection.anchorY, this.selection.activeEndY);
      const maxY = Math.max(this.selection.anchorY, this.selection.activeEndY);
      const width = maxX - minX + 1;
      const height = maxY - minY + 1;

      const cells = [];
      for (let y = minY; y <= maxY; y++) {
        for (let x = minX; x <= maxX; x++) {
          cells.push({ x, y });
        }
      }

      const savedAnchorX = this.selection.anchorX;
      const savedAnchorY = this.selection.anchorY;
      const savedActiveEndX = this.selection.activeEndX;
      const savedActiveEndY = this.selection.activeEndY;

      this.setSelection('rect', cells, { minX, minY, maxX, maxY, width, height });
      this.selection.anchorX = savedAnchorX;
      this.selection.anchorY = savedAnchorY;
      this.selection.activeEndX = savedActiveEndX;
      this.selection.activeEndY = savedActiveEndY;
      return true;
    }

    // Backward-compatible selectRect for older tests/handlers
    selectRect(x1, y1, x2, y2) {
      const minX = Math.max(0, Math.min(x1, x2));
      const minY = Math.max(0, Math.min(y1, y2));
      const maxX = Math.min(this.doc.width - 1, Math.max(x1, x2));
      const maxY = Math.min(this.doc.height - 1, Math.max(y1, y2));
      const w = maxX - minX + 1, h = maxY - minY + 1;
      if (w <= 0 || h <= 0) return;

      const cells = [];
      for (let y = minY; y <= maxY; y++) {
        for (let x = minX; x <= maxX; x++) {
          cells.push({ x, y });
        }
      }
      this.setSelection('rect', cells, { minX, minY, maxX, maxY, width: w, height: h });
    }
  }

  globalThis.EditorModel = EditorModel;
  globalThis.sharedClipboard = sharedClipboard;
})();