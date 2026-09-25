(() => {
  'use strict';
  const $ = id => document.getElementById(id);

  function lineCells(from, to, width) {
    const cells = [];
    let x = from.x, y = from.y;
    const dx = Math.abs(to.x - x), dy = -Math.abs(to.y - y);
    const sx = x < to.x ? 1 : -1, sy = y < to.y ? 1 : -1;
    let error = dx + dy;
    while (true) {
      cells.push(y * width + x);
      if (x === to.x && y === to.y) break;
      const twice = 2 * error;
      if (twice >= dy) { error += dy; x += sx; }
      if (twice <= dx) { error += dx; y += sy; }
    }
    return cells;
  }

  class Gestures {
    constructor(model, view, timeline, run) {
      this.model = model;
      this.view = view;
      this.timeline = timeline;
      this.run = run;
      this.pointer = null;
      this.previous = null;
      this.tool = null;
      this.lastScreenPos = null; // Für das Panning per Mittelklick

      const canvas = view.canvas;
      canvas.addEventListener('contextmenu', event => event.preventDefault());
      canvas.addEventListener('auxclick', event => event.preventDefault());
      canvas.addEventListener('pointerdown', event => {
        if (![0, 1, 2].includes(event.button) || this.pointer !== null) return;
        event.preventDefault();
        timeline.stop();
        view.viewport.focus({ preventScroll: true });
        this.pointer = event.pointerId;
        canvas.setPointerCapture(this.pointer);
        this.apply(event);
      });
      canvas.addEventListener('pointermove', event => {
        const position = view.position(event);
        $('cell-position').textContent = position ? `${position.x}, ${position.y}` : '—, —';
        if (this.pointer === event.pointerId) {
          if (!event.buttons) this.finish();
          else this.apply(event);
        }
      });
      canvas.addEventListener('pointerleave', () => { $('cell-position').textContent = '—, —'; });
      for (const event of ['pointerup', 'pointercancel', 'lostpointercapture']) {
        canvas.addEventListener(event, e => { if (e.pointerId === this.pointer) this.finish(); });
      }
      window.addEventListener('blur', () => { this.finish(); timeline.stop(); });
      document.addEventListener('keydown', event => this.keydown(event));
    }

    apply(event) {
      // MITTELKLICK (buttons === 4): Zeichenfläche verschieben (Pan)
      if (event.buttons & 4) {
        this.tool = 'Pan';
        $('active-tool').textContent = 'Pan';

        if (this.lastScreenPos) {
          const dx = event.clientX - this.lastScreenPos.x;
          const dy = event.clientY - this.lastScreenPos.y;

          // Viewport entgegengesetzt zur Mausbewegung scrollen
          this.view.viewport.scrollLeft -= dx;
          this.view.viewport.scrollTop -= dy;
        }

        this.lastScreenPos = { x: event.clientX, y: event.clientY };
        this.previous = null;
        return;
      }

      // Falls kein Mittelklick aktiv ist, Position zurücksetzen
      this.lastScreenPos = null;

      // Linksklick = Pencil, Rechtsklick = Eraser (Picker entfällt auf Mittelklick)
      const tool = event.buttons & 2 ? 'Eraser' : 'Pencil';
      if (tool !== this.tool) this.previous = null;
      this.tool = tool;
      $('active-tool').textContent = tool;

      const position = this.view.position(event);
      if (!position) { this.previous = null; return; }

      const cells = lineCells(this.previous ?? position, position, this.model.doc.width);
      this.model.paint(cells, tool === 'Eraser');
      this.previous = position;
    }

    finish() {
      const pointer = this.pointer;
      this.pointer = null;
      this.previous = null;
      this.tool = null;
      this.lastScreenPos = null; // Position nach dem Loslassen leeren

      if (pointer !== null && this.view.canvas.hasPointerCapture(pointer)) {
        this.view.canvas.releasePointerCapture(pointer);
      }
      this.model.commit();
      $('active-tool').textContent = 'Idle';
    }

    keydown(event) {
      if (event.defaultPrevented || event.isComposing || event.target.closest('input, textarea, select, [contenteditable="true"]')) return;
      const key = event.key.toLowerCase();
      let action;
      if ((event.ctrlKey || event.metaKey) && !event.altKey) {
        if (key === 'z') action = () => { this.timeline.stop(); this.model.travel(event.shiftKey); };
        if (key === 'y') action = () => { this.timeline.stop(); this.model.travel(true); };
      } else if (!event.ctrlKey && !event.metaKey && !event.altKey) {
        if (key === 'a' || key === 'arrowleft') action = () => this.timeline.step(-1);
        if (key === 'd' || key === 'arrowright') action = () => this.timeline.step(1);
        if (key === ' ' && !event.repeat) action = () => this.timeline.toggle();
        if (key === '#' && !event.repeat) action = () => $('grid-toggle').click();
      }
      if (!action) return;
      event.preventDefault();
      this.finish();
      this.run(action);
    }
  }

  globalThis.Gestures = Gestures;
  globalThis.lineCells = lineCells;
})();