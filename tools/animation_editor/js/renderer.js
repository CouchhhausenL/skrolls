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

      // Reference image state
      this.referenceImage = {
        img: null,
        opacity: 0.4,
        visible: false,
        fit: 'contain' // 'contain', 'fill', 'center'
      };

      // Interactive preview overlays
      this.previewCells = [];
      this.previewOverlay = null; // { type: 'lasso'|'bezier'|'rect', ... }

      // Text cursor state
      this.textCursor = null; // { x, y }
      this.textCursorVisible = true;
      this.cursorBlinkInterval = setInterval(() => {
        if (this.textCursor) {
          this.textCursorVisible = !this.textCursorVisible;
          this.schedule();
        }
      }, 530);

      model.addEventListener('change', event => {
        if (!['brush', 'saved'].includes(event.detail)) this.schedule();
      });

      this.viewport.addEventListener('wheel', event => {
        event.preventDefault();
        this.setZoom(this.zoom * Math.exp(-Math.max(-100, Math.min(100, event.deltaY)) * 0.0025), event.clientX, event.clientY);
      }, { passive: false });

      this.schedule();
    }

    setReferenceImage(img, opacity = 0.4, fit = 'contain', visible = true) {
      this.referenceImage.img = img;
      this.referenceImage.opacity = opacity;
      this.referenceImage.fit = fit;
      this.referenceImage.visible = visible;
      this.schedule();
    }

    setReferenceOpacity(opacity) {
      this.referenceImage.opacity = opacity;
      this.schedule();
    }

    setReferenceVisible(visible) {
      this.referenceImage.visible = visible;
      this.schedule();
    }

    setReferenceFit(fit) {
      this.referenceImage.fit = fit;
      this.schedule();
    }

    setTextCursor(cursor) {
      this.textCursor = cursor;
      this.textCursorVisible = true;
      this.schedule();
    }

    setPreview(cells = [], overlay = null) {
      this.previewCells = cells;
      this.previewOverlay = overlay;
      this.schedule();
    }

    clearPreview() {
      this.previewCells = [];
      this.previewOverlay = null;
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

      const ctx = this.canvas.getContext('2d');
      const scale = Math.min(w / (doc.width * CELL_W), h / (doc.height * CELL_H));
      const offsetX = (w - doc.width * CELL_W * scale) / 2;
      const offsetY = (h - doc.height * CELL_H * scale) / 2;

      ctx.clearRect(0, 0, w, h);
      ctx.save();
      ctx.translate(offsetX, offsetY);
      ctx.scale(scale, scale);

      // 1. Draw cell backgrounds
      const frame = this.model.frame;
      for (let i = 0; i < frame.length; i++) {
        const cell = frame[i];
        const x = (i % doc.width) * CELL_W, y = Math.floor(i / doc.width) * CELL_H;
        ctx.fillStyle = cell.bg;
        ctx.fillRect(x, y, CELL_W, CELL_H);
      }

      // 2. Draw Reference Image (over cell background, beneath text for optimal tracing)
      const ref = this.referenceImage;
      if (ref.img && ref.visible && ref.opacity > 0) {
        ctx.save();
        ctx.globalAlpha = Math.max(0, Math.min(1, ref.opacity));
        const docPxW = doc.width * CELL_W;
        const docPxH = doc.height * CELL_H;

        let dw = docPxW, dh = docPxH, dx = 0, dy = 0;
        if (ref.fit === 'contain') {
          const ratio = Math.min(docPxW / ref.img.naturalWidth, docPxH / ref.img.naturalHeight);
          dw = ref.img.naturalWidth * ratio;
          dh = ref.img.naturalHeight * ratio;
          dx = (docPxW - dw) / 2;
          dy = (docPxH - dh) / 2;
        } else if (ref.fit === 'center') {
          dw = ref.img.naturalWidth;
          dh = ref.img.naturalHeight;
          dx = (docPxW - dw) / 2;
          dy = (docPxH - dh) / 2;
        }
        ctx.drawImage(ref.img, dx, dy, dw, dh);
        ctx.restore();
      }

      // 3. Draw text characters of base frame
      ctx.font = '16px Consolas, "Courier New", monospace';
      ctx.textBaseline = 'middle';
      ctx.textAlign = 'center';

      for (let i = 0; i < frame.length; i++) {
        const cell = frame[i];
        if (cell.char !== ' ') {
          const x = (i % doc.width) * CELL_W, y = Math.floor(i / doc.width) * CELL_H;
          ctx.save();
          ctx.beginPath();
          ctx.rect(x, y, CELL_W, CELL_H);
          ctx.clip();
          ctx.fillStyle = cell.fg;
          ctx.fillText(cell.char, x + CELL_W / 2, y + CELL_H / 2 + 1);
          ctx.restore();
        }
      }

      // 4. Draw Floating Selection (if moving/pasted)
      const sel = this.model.selection;
      if (sel && sel.floating) {
        const startX = sel.bounds.minX + sel.offsetX;
        const startY = sel.bounds.minY + sel.offsetY;

        for (const p of sel.pixels) {
          // Space / empty pixels are transparent – don't overdraw the canvas beneath
          if (!p.char || p.char === ' ') continue;

          const px = (startX + p.relX) * CELL_W;
          const py = (startY + p.relY) * CELL_H;

          ctx.fillStyle = p.bg || doc.background;
          ctx.fillRect(px, py, CELL_W, CELL_H);

          ctx.save();
          ctx.beginPath();
          ctx.rect(px, py, CELL_W, CELL_H);
          ctx.clip();
          ctx.fillStyle = p.fg || SKA.DEFAULT_FG;
          ctx.fillText(p.char, px + CELL_W / 2, py + CELL_H / 2 + 1);
          ctx.restore();
        }

        // Draw floating selection border
        ctx.save();
        ctx.strokeStyle = '#4da6ff';
        ctx.lineWidth = 1.5 / scale;
        ctx.setLineDash([4 / scale, 4 / scale]);
        ctx.strokeRect(startX * CELL_W, startY * CELL_H, sel.bounds.width * CELL_W, sel.bounds.height * CELL_H);
        ctx.restore();
      } else if (sel && !sel.floating) {
        // Draw static selection border and tint
        ctx.save();
        ctx.fillStyle = 'rgba(77, 166, 255, 0.2)';
        ctx.strokeStyle = '#4da6ff';
        ctx.lineWidth = 1.5 / scale;
        ctx.setLineDash([4 / scale, 4 / scale]);

        for (const p of sel.pixels) {
          ctx.fillRect(p.x * CELL_W, p.y * CELL_H, CELL_W, CELL_H);
        }

        if (sel.type === 'rect') {
          ctx.strokeRect(sel.bounds.minX * CELL_W, sel.bounds.minY * CELL_H, sel.bounds.width * CELL_W, sel.bounds.height * CELL_H);
        } else {
          // For lasso, stroke around selected cells
          for (const p of sel.pixels) {
            ctx.strokeRect(p.x * CELL_W, p.y * CELL_H, CELL_W, CELL_H);
          }
        }
        ctx.restore();
      }

      // 5. Draw Live Tool Preview
      if (this.previewCells && this.previewCells.length > 0) {
        for (const p of this.previewCells) {
          const px = p.x * CELL_W, py = p.y * CELL_H;
          if (p.x < 0 || p.x >= doc.width || p.y < 0 || p.y >= doc.height) continue;

          ctx.fillStyle = p.bg || 'rgba(168, 230, 176, 0.35)';
          ctx.fillRect(px, py, CELL_W, CELL_H);

          if (p.char && p.char !== ' ') {
            ctx.save();
            ctx.beginPath();
            ctx.rect(px, py, CELL_W, CELL_H);
            ctx.clip();
            ctx.fillStyle = p.fg || '#ffffff';
            ctx.fillText(p.char, px + CELL_W / 2, py + CELL_H / 2 + 1);
            ctx.restore();
          }

          ctx.strokeStyle = 'rgba(168, 230, 176, 0.6)';
          ctx.lineWidth = 1 / scale;
          ctx.strokeRect(px, py, CELL_W, CELL_H);
        }
      }

      // 6. Draw Preview Overlays (Selection rect, Lasso boundary line, or Bézier control handles)
      if (this.previewOverlay) {
        if (this.previewOverlay.type === 'rect' && this.previewOverlay.start && this.previewOverlay.current) {
          ctx.save();
          const start = this.previewOverlay.start;
          const cur = this.previewOverlay.current;
          const minX = Math.min(start.x, cur.x);
          const maxX = Math.max(start.x, cur.x);
          const minY = Math.min(start.y, cur.y);
          const maxY = Math.max(start.y, cur.y);
          const rx = minX * CELL_W;
          const ry = minY * CELL_H;
          const rw = (maxX - minX + 1) * CELL_W;
          const rh = (maxY - minY + 1) * CELL_H;

          ctx.fillStyle = 'rgba(77, 166, 255, 0.2)';
          ctx.fillRect(rx, ry, rw, rh);
          ctx.strokeStyle = '#4da6ff';
          ctx.lineWidth = 1.5 / scale;
          ctx.setLineDash([4 / scale, 4 / scale]);
          ctx.strokeRect(rx, ry, rw, rh);
          ctx.restore();
        } else if (this.previewOverlay.type === 'lasso' && this.previewOverlay.points.length > 1) {
          ctx.save();
          ctx.strokeStyle = '#a8e6b0';
          ctx.lineWidth = 1.5 / scale;
          ctx.setLineDash([3 / scale, 3 / scale]);
          ctx.beginPath();
          const pts = this.previewOverlay.points;
          ctx.moveTo((pts[0].x + 0.5) * CELL_W, (pts[0].y + 0.5) * CELL_H);
          for (let i = 1; i < pts.length; i++) {
            ctx.lineTo((pts[i].x + 0.5) * CELL_W, (pts[i].y + 0.5) * CELL_H);
          }
          ctx.stroke();
          ctx.restore();
        } else if (this.previewOverlay.type === 'bezier' && this.previewOverlay.points.length >= 2) {
          ctx.save();
          const pts = this.previewOverlay.points;
          // Control polygon
          ctx.strokeStyle = 'rgba(255, 200, 80, 0.6)';
          ctx.lineWidth = 1 / scale;
          ctx.setLineDash([2 / scale, 2 / scale]);
          ctx.beginPath();
          ctx.moveTo((pts[0].x + 0.5) * CELL_W, (pts[0].y + 0.5) * CELL_H);
          for (let i = 1; i < pts.length; i++) {
            ctx.lineTo((pts[i].x + 0.5) * CELL_W, (pts[i].y + 0.5) * CELL_H);
          }
          ctx.stroke();

          // Control point handles
          for (let i = 0; i < pts.length; i++) {
            const cx = (pts[i].x + 0.5) * CELL_W;
            const cy = (pts[i].y + 0.5) * CELL_H;
            ctx.fillStyle = i === 0 || i === pts.length - 1 ? '#a8e6b0' : '#ffc850';
            ctx.beginPath();
            ctx.arc(cx, cy, 3.5 / scale, 0, Math.PI * 2);
            ctx.fill();
            ctx.strokeStyle = '#111';
            ctx.lineWidth = 1 / scale;
            ctx.stroke();
          }
          ctx.restore();
        }
      }

      // 7. Draw Text Editor Caret
      if (this.textCursor && this.textCursorVisible) {
        const cx = this.textCursor.x * CELL_W, cy = this.textCursor.y * CELL_H;
        ctx.save();
        ctx.fillStyle = 'rgba(168, 230, 176, 0.4)';
        ctx.fillRect(cx, cy, CELL_W, CELL_H);
        ctx.strokeStyle = '#a8e6b0';
        ctx.lineWidth = 2 / scale;
        ctx.strokeRect(cx, cy, CELL_W, CELL_H);
        ctx.restore();
      }

      // 8. Draw Grid
      if (this.grid && CELL_W * scale >= 5) {
        ctx.beginPath();
        for (let x = 0; x <= doc.width; x++) { ctx.moveTo(x * CELL_W, 0); ctx.lineTo(x * CELL_W, doc.height * CELL_H); }
        for (let y = 0; y <= doc.height; y++) { ctx.moveTo(0, y * CELL_H); ctx.lineTo(doc.width * CELL_W, y * CELL_H); }
        ctx.strokeStyle = '#9ba9bd35';
        ctx.lineWidth = 1 / scale;
        ctx.stroke();
      }

      ctx.restore();

      // Update UI displays
      const zoomReset = document.getElementById('zoom-reset');
      if (zoomReset) zoomReset.textContent = `${Math.round(this.zoom * 100)}%`;
      const canvasInfo = document.getElementById('canvas-info');
      if (canvasInfo) canvasInfo.textContent = `${doc.width} × ${doc.height} · ${doc.fps} FPS`;
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

    subpixelPosition(event) {
      const rect = this.canvas.getBoundingClientRect();
      const subW = this.model.doc.width * 3;
      const subH = this.model.doc.height * 4;
      const sx = Math.floor((event.clientX - rect.left) / rect.width * subW);
      const sy = Math.floor((event.clientY - rect.top) / rect.height * subH);
      if (sx < 0 || sy < 0 || sx >= subW || sy >= subH) return null;
      return { sx, sy };
    }

    getVisibleCenterCell() {
      const viewRect = this.viewport.getBoundingClientRect();
      const midX = viewRect.left + viewRect.width / 2;
      const midY = viewRect.top + viewRect.height / 2;
      const pos = this.position({ clientX: midX, clientY: midY });
      if (pos) return pos;
      return {
        x: Math.floor(this.model.doc.width / 2),
        y: Math.floor(this.model.doc.height / 2)
      };
    }
  }

  globalThis.CanvasView = CanvasView;
  globalThis.drawFrame = drawFrame;
})();
