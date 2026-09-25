(() => {
  'use strict';
  const CELL_W = 12, CELL_H = 20;

  function drawFrame(ctx, doc, frame, width, height, grid = false) {
    const scale = Math.min(width / (doc.width * CELL_W), height / (doc.height * CELL_H));
    const offsetX = (width - doc.width * CELL_W * scale) / 2;
    const offsetY = (height - doc.height * CELL_H * scale) / 2;
    ctx.clearRect(0, 0, width, height);
    ctx.save();
    ctx.translate(offsetX, offsetY);
    ctx.scale(scale, scale);
    ctx.font = '16px Consolas, "Courier New", monospace';
    ctx.textBaseline = 'middle';
    ctx.textAlign = 'center';
    frame.forEach((cell, i) => {
      const x = (i % doc.width) * CELL_W, y = Math.floor(i / doc.width) * CELL_H;
      ctx.fillStyle = cell.bg;
      ctx.fillRect(x, y, CELL_W, CELL_H);
      if (cell.char !== ' ') {
        ctx.save();
        ctx.beginPath();
        ctx.rect(x, y, CELL_W, CELL_H);
        ctx.clip();
        ctx.fillStyle = cell.fg;
        ctx.fillText(cell.char, x + CELL_W / 2, y + CELL_H / 2 + 1);
        ctx.restore();
      }
    });
    if (grid && CELL_W * scale >= 5) {
      ctx.beginPath();
      for (let x = 0; x <= doc.width; x++) { ctx.moveTo(x * CELL_W, 0); ctx.lineTo(x * CELL_W, doc.height * CELL_H); }
      for (let y = 0; y <= doc.height; y++) { ctx.moveTo(0, y * CELL_H); ctx.lineTo(doc.width * CELL_W, y * CELL_H); }
      ctx.strokeStyle = '#9ba9bd40';
      ctx.lineWidth = 1 / scale;
      ctx.stroke();
    }
    ctx.restore();
  }

  class CanvasView {
    constructor(model) {
      this.model = model;
      this.canvas = document.getElementById('art-canvas');
      this.viewport = document.getElementById('viewport');
      this.zoom = 1;
      this.grid = true;
      this.queued = false;
      model.addEventListener('change', event => {
        if (!['brush', 'saved'].includes(event.detail)) this.schedule();
      });
      this.viewport.addEventListener('wheel', event => {
        event.preventDefault();
        this.setZoom(this.zoom * Math.exp(-Math.max(-100, Math.min(100, event.deltaY)) * 0.0025), event.clientX, event.clientY);
      }, { passive: false });
      this.schedule();
    }

    schedule() {
      if (this.queued) return;
      this.queued = true;
      requestAnimationFrame(() => { this.queued = false; this.draw(); });
    }

    draw() {
      const doc = this.model.doc;
      const width = doc.width * CELL_W, height = doc.height * CELL_H;
      const resolution = Math.min((window.devicePixelRatio || 1) * this.zoom, 3, 8192 / width, 8192 / height, Math.sqrt(12000000 / (width * height)));
      const w = Math.max(1, Math.round(width * resolution)), h = Math.max(1, Math.round(height * resolution));
      if (this.canvas.width !== w) this.canvas.width = w;
      if (this.canvas.height !== h) this.canvas.height = h;
      this.canvas.style.width = `${width * this.zoom}px`;
      this.canvas.style.height = `${height * this.zoom}px`;
      drawFrame(this.canvas.getContext('2d'), doc, this.model.frame, w, h, this.grid);
      document.getElementById('zoom-reset').textContent = `${Math.round(this.zoom * 100)}%`;
      document.getElementById('canvas-info').textContent = `${doc.width} × ${doc.height} · ${doc.fps} FPS`;
    }

    setZoom(value, clientX, clientY) {
      const before = this.canvas.getBoundingClientRect();
      const view = this.viewport.getBoundingClientRect();
      const anchorX = clientX ?? view.left + view.width / 2;
      const anchorY = clientY ?? view.top + view.height / 2;
      const x = (anchorX - before.left) / before.width, y = (anchorY - before.top) / before.height;
      this.zoom = Math.max(0.1, Math.min(6, value));
      this.draw();
      const after = this.canvas.getBoundingClientRect();
      this.viewport.scrollLeft += after.left + x * after.width - anchorX;
      this.viewport.scrollTop += after.top + y * after.height - anchorY;
    }

    fit() {
      const doc = this.model.doc;
      this.zoom = Math.max(0.1, Math.min(6, (this.viewport.clientWidth - 96) / (doc.width * CELL_W), (this.viewport.clientHeight - 96) / (doc.height * CELL_H)));
      this.draw();
      this.viewport.scrollLeft = (this.viewport.scrollWidth - this.viewport.clientWidth) / 2;
      this.viewport.scrollTop = (this.viewport.scrollHeight - this.viewport.clientHeight) / 2;
    }

    position(event) {
      const rect = this.canvas.getBoundingClientRect();
      const x = Math.floor((event.clientX - rect.left) / rect.width * this.model.doc.width);
      const y = Math.floor((event.clientY - rect.top) / rect.height * this.model.doc.height);
      if (x < 0 || y < 0 || x >= this.model.doc.width || y >= this.model.doc.height) return null;
      return { x, y, index: y * this.model.doc.width + x };
    }
  }

  globalThis.CanvasView = CanvasView;
  globalThis.drawFrame = drawFrame;
})();
