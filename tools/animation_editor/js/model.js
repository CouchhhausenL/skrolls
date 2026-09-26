(() => {
  'use strict';
  const { blankFrame, DEFAULT_FG, validateHeader, validateSize } = SKA;

  class EditorModel extends EventTarget {
    constructor() {
      super();
      this.brush = { char: '█', fg: DEFAULT_FG, bg: null };
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

    // --- NEUE METHODEN FÜR DAS ÜBERTRAGEN VON FARBEN AUF ANDERE FRAMES ---

    /**
     * Überträgt die Farbpalette (fg und bg) des AKTUELLEN Frames auf ALLE anderen Frames.
     * Die Zeichen (char) der anderen Frames bleiben unverändert.
     */
    applyColorsToAllFrames() {
      if (this.doc.frames.length <= 1) return 0;

      const currentFrame = this.frame;
      let updatedFramesCount = 0;

      this.mutate(() => {
        this.doc.frames = this.doc.frames.map((frame, idx) => {
          if (idx === this.index) return frame; // Aktuellen Frame überspringen

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

    /**
     * Überträgt die Farben des aktuellen Frames NUR auf FOLGENDE Frames (vom aktuellen Index bis zum Ende).
     */
    applyColorsToNextFrames() {
      if (this.index >= this.doc.frames.length - 1) return 0;

      const currentFrame = this.frame;
      let updatedFramesCount = 0;

      this.mutate(() => {
        this.doc.frames = this.doc.frames.map((frame, idx) => {
          if (idx <= this.index) return frame; // Vorherige und aktuellen Frame überspringen

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

    // --- RECHTECK-AUSWAHL & VERSCHIEBUNG ---

    selectRect(x1, y1, x2, y2) {
      this.commit();
      
      const x = Math.max(0, Math.min(x1, x2));
      const y = Math.max(0, Math.min(y1, y2));
      const w = Math.min(this.doc.width - x, Math.abs(x2 - x1) + 1);
      const h = Math.min(this.doc.height - y, Math.abs(y2 - y1) + 1);

      if (w <= 0 || h <= 0) return;

      const pixels = [];
      const emptyCell = { char: ' ', fg: DEFAULT_FG, bg: this.doc.background };

      this.mutate(() => {
        const newFrame = this.frame.slice();

        for (let row = 0; row < h; row++) {
          for (let col = 0; col < w; col++) {
            const index = (y + row) * this.doc.width + (x + col);
            pixels.push({ ...newFrame[index] });
            newFrame[index] = { ...emptyCell };
          }
        }

        this.doc.frames[this.index] = newFrame;
      });

      this.selection = { x, y, width: w, height: h, pixels, currentX: x, currentY: y };
      this.emit('select-rect');
    }

    moveSelection(dx, dy) {
      if (!this.selection) return;

      this.selection.currentX = Math.max(0, Math.min(this.doc.width - this.selection.width, this.selection.x + dx));
      this.selection.currentY = Math.max(0, Math.min(this.doc.height - this.selection.height, this.selection.y + dy));
      
      this.emit('move-rect');
    }

    commitSelection() {
      if (!this.selection) return;

      const { currentX, currentY, width, height, pixels } = this.selection;

      this.mutate(() => {
        const newFrame = this.frame.slice();

        for (let row = 0; row < height; row++) {
          for (let col = 0; col < width; col++) {
            const targetX = currentX + col;
            const targetY = currentY + row;

            if (targetX >= 0 && targetX < this.doc.width && targetY >= 0 && targetY < this.doc.height) {
              const targetIndex = targetY * this.doc.width + targetX;
              const sourceIndex = row * width + col;
              newFrame[targetIndex] = pixels[sourceIndex];
            }
          }
        }

        this.doc.frames[this.index] = newFrame;
      });

      this.selection = null;
      this.emit('commit-rect');
    }

    cancelSelection() {
      if (!this.selection) return;

      const { x, y, width, height, pixels } = this.selection;

      this.mutate(() => {
        const newFrame = this.frame.slice();

        for (let row = 0; row < height; row++) {
          for (let col = 0; col < width; col++) {
            const index = (y + row) * this.doc.width + (x + col);
            newFrame[index] = pixels[row * width + col];
          }
        }

        this.doc.frames[this.index] = newFrame;
      });

      this.selection = null;
      this.emit('cancel-rect');
    }
  }

  globalThis.EditorModel = EditorModel;
})();