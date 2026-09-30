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

  function getBrushFootprint(cx, cy, thickness, width, height, options = {}) {
    const { falloff = false, baseChar = '@', baseOpacity = 1.0 } = options;
    const points = [];
    if (thickness <= 1) {
      if (cx >= 0 && cx < width && cy >= 0 && cy < height) {
        points.push({ x: cx, y: cy, char: baseChar, opacity: baseOpacity });
      }
      return points;
    }
    const r = (thickness - 1) / 2;
    const r2 = r * r + 0.25;
    const minX = Math.max(0, Math.floor(cx - r));
    const maxX = Math.min(width - 1, Math.ceil(cx + r));
    const minY = Math.max(0, Math.floor(cy - r));
    const maxY = Math.min(height - 1, Math.ceil(cy + r));

    for (let y = minY; y <= maxY; y++) {
      for (let x = minX; x <= maxX; x++) {
        const dx = x - cx, dy = y - cy;
        const d2 = dx * dx + dy * dy;
        if (d2 <= r2) {
          if (!falloff) {
            points.push({ x, y, char: baseChar, opacity: baseOpacity });
          } else {
            const normDist = r > 0 ? Math.sqrt(d2) / r : 0;
            // Solid core, then a narrow fringe fades toward the edge.
            // falloffStart: the outermost (1-falloffStart) fraction of the radius fades.
            // At r=2 (t5): 0.6 -> inner 60% solid, outer 40% fades
            // At r=7 (t15): ~0.86 -> wide solid core, thin fringe
            const falloffStart = Math.max(0.6, 1.0 - (0.8 / Math.max(r, 1.0)));
            let effOpacity;
            if (normDist <= falloffStart) {
              effOpacity = baseOpacity;
            } else {
              // smoothstep over the fringe zone only
              const t = (normDist - falloffStart) / (1.0 - falloffStart);
              const smooth = 1.0 - t * t * (3 - 2 * t);
              // min 0.3 so edge chars are still clearly visible
              effOpacity = baseOpacity * Math.max(0.3, smooth);
            }
            const pct = Math.round(effOpacity * 100);
            const char = pct >= 100 ? baseChar : getAsciiCharForOpacity(pct);
            points.push({ x, y, char, opacity: effOpacity });
          }
        }
      }
    }
    return points;
  }

  function pointInPolygon(px, py, polygon) {
    let inside = false;
    for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
      const xi = polygon[i].x, yi = polygon[i].y;
      const xj = polygon[j].x, yj = polygon[j].y;
      const intersect = ((yi > py) !== (yj > py)) && (px < (xj - xi) * (py - yi) / (yj - yi) + xi);
      if (intersect) inside = !inside;
    }
    return inside;
  }

  // JavE ASCII Density Ramp: low opacity (.) -> medium ($) -> high (@)
  function getAsciiCharForOpacity(pct) {
    const val = Math.max(0, Math.min(100, Math.round(pct)));
    if (val <= 15) return '.';
    if (val <= 25) return ',';
    if (val <= 35) return ':';
    if (val <= 45) return '-';
    if (val <= 55) return '*';
    if (val <= 65) return '$'; // medium opacity
    if (val <= 75) return '%';
    if (val <= 94) return '#';
    return '@';               // high opacity
  }

  const TOOL_SHORTCUT_GROUPS = {
    's': ['select-rect', 'select-lasso'],
    't': ['text'],
    'f': ['freehand-generic', 'freehand-algo'],
    'l': ['line-generic', 'line-algo'],
    'r': ['rect-generic', 'rect-algo'],
    'o': ['ellipse-generic', 'ellipse-algo'],
    'e': ['eraser'],
    'b': ['bezier-generic', 'bezier-algo'],
    'p': ['pattern-brush'],
    'x': ['eraser'],
    'c': ['colorize'],
    'i': ['picker'],
    'k': ['picker'],
    'h': ['pan']
  };

  class Gestures {
    constructor(model, view, timeline, run) {
      this.model = model;
      this.view = view;
      this.timeline = timeline;
      this.run = run;

      this.algo = new AlgorithmicAscii();

      // Tool settings
      this.activeTool = 'freehand-generic';
      this.thickness = 1;
      this.opacity = 1.0;
      this.falloff = false;
      this.model.brush.char = '@';
      this.patternText = '/\\\n\\/';
      this.patternLines = ['/\\', '\\/'];

      // Active gesture state
      this.pointer = null;
      this.startPos = null;
      this.previous = null;
      this.currentMouseCell = null;
      this.strokePoints = []; // for continuous freehand & lasso
      this.movingSelection = false;
      this.moveOrigin = null;

      // Bézier state
      this.bezierStep = 0; // 0 = idle, 1 = start set, 2 = end set
      this.bezierPoints = [];

      this.lastScreenPos = null;

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
        this.pointerDown(event);
      });

      canvas.addEventListener('pointermove', event => {
        const position = view.position(event);
        this.currentMouseCell = position;
        const cellPos = $('cell-position');
        if (cellPos) cellPos.textContent = position ? `${position.x}, ${position.y}` : '—, —';

        if (this.pointer === event.pointerId) {
          if (!event.buttons) this.finish();
          else this.pointerMove(event);
        } else {
          this.hoverMove(event);
        }
      });

      canvas.addEventListener('pointerleave', () => {
        const cellPos = $('cell-position');
        if (cellPos) cellPos.textContent = '—, —';
      });

      for (const event of ['pointerup', 'pointercancel', 'lostpointercapture']) {
        canvas.addEventListener(event, e => {
          if (e.pointerId === this.pointer) this.pointerUp(e);
        });
      }

      window.addEventListener('blur', () => { this.finish(); timeline.stop(); });
      document.addEventListener('keydown', event => this.keydown(event));
    }

    setTool(tool) {
      if (this.activeTool === 'text' && tool !== 'text') {
        this.view.setTextCursor(null);
      }
      if (this.activeTool.startsWith('bezier') && !tool.startsWith('bezier')) {
        this.cancelBezier();
      }
      this.activeTool = tool;
      const badge = $('active-tool');
      if (badge) badge.textContent = tool;

      // Update cursor style
      if (tool.startsWith('select')) {
        this.view.canvas.style.cursor = 'crosshair';
      } else if (tool === 'text') {
        this.view.canvas.style.cursor = 'text';
      } else if (tool === 'pan') {
        this.view.canvas.style.cursor = 'grab';
      } else {
        this.view.canvas.style.cursor = 'crosshair';
      }
    }

    setThickness(val) {
      this.thickness = Math.max(1, Math.min(20, parseInt(val, 10) || 1));
    }

    setFalloff(val) {
      this.falloff = Boolean(val);
    }

    cycleTool(key) {
      const group = TOOL_SHORTCUT_GROUPS[key.toLowerCase()];
      if (!group || group.length === 0) return;
      const idx = group.indexOf(this.activeTool);
      const nextTool = idx === -1 ? group[0] : group[(idx + 1) % group.length];
      
      const btn = document.querySelector(`.tool-btn[data-tool="${nextTool}"]`);
      if (btn) {
        btn.click();
      } else {
        this.setTool(nextTool);
      }
    }

    setOpacity(val) {
      this.opacity = Math.max(0.1, Math.min(1.0, parseFloat(val) || 1.0));
      const pct = Math.round(this.opacity * 100);

      // Auto-select ASCII density character (like JavE)
      const asciiChar = getAsciiCharForOpacity(pct);
      if (this.model.brush.char !== null) {
        this.model.brush.char = asciiChar;
        this.model.emit('brush');
      }

      return asciiChar;
    }

    setAlgoMode(mode) {
      this.algo.setMode(mode);
      if (this.bezierStep > 0) this.updateBezierPreview();
    }

    setPattern(text) {
      this.patternText = text || '█';
      this.patternLines = this.patternText.split('\n');
      if (this.patternLines.length === 0 || (this.patternLines.length === 1 && !this.patternLines[0])) {
        this.patternLines = ['█'];
      }
    }

    pointerDown(event) {
      // Middle-click: Pan workspace
      if (event.button === 1 || (event.buttons & 4)) {
        this.lastScreenPos = { x: event.clientX, y: event.clientY };
        return;
      }

      const pos = this.view.position(event);
      if (!pos) return;
      this.startPos = pos;
      this.previous = pos;

      // Right-click: Quick eraser or cancel
      if (event.button === 2) {
        if (this.bezierStep > 0) {
          this.cancelBezier();
          return;
        }
        if (this.model.selection) {
          this.model.commitSelection();
          return;
        }
        const footprint = getBrushFootprint(pos.x, pos.y, this.thickness, this.model.doc.width, this.model.doc.height);
        this.model.paintCells(footprint, { isEraser: true });
        return;
      }

      // Check if clicking inside active selection to move it
      if (this.model.selection && (this.activeTool === 'select-rect' || this.activeTool === 'select-lasso')) {
        const sel = this.model.selection;
        const curMinX = sel.bounds.minX + sel.offsetX;
        const curMinY = sel.bounds.minY + sel.offsetY;
        const curMaxX = curMinX + sel.bounds.width - 1;
        const curMaxY = curMinY + sel.bounds.height - 1;

        if (pos.x >= curMinX && pos.x <= curMaxX && pos.y >= curMinY && pos.y <= curMaxY) {
          this.movingSelection = true;
          this.moveOrigin = { x: pos.x, y: pos.y, initOffsetX: sel.offsetX, initOffsetY: sel.offsetY };
          return;
        } else {
          // Clicked outside selection -> commit existing selection
          this.model.commitSelection();
        }
      }

      // Tool-specific pointerdown logic
      switch (this.activeTool) {
        case 'pan':
          this.lastScreenPos = { x: event.clientX, y: event.clientY };
          break;

        case 'picker':
          this.model.pick(pos.index, event.ctrlKey);
          break;

        case 'text':
          this.view.setTextCursor({ x: pos.x, y: pos.y });
          break;

        case 'select-rect':
          this.strokePoints = [pos];
          this.view.setPreview([], { type: 'rect', start: pos, current: pos });
          break;

        case 'select-lasso':
          this.strokePoints = [pos];
          this.view.setPreview([], { type: 'lasso', points: [pos] });
          break;

        case 'freehand-generic':
        case 'eraser':
        case 'colorize':
        case 'pattern-brush': {
          this.strokePoints = [pos];
          this.paintStrokeStep(pos, pos);
          break;
        }

        case 'freehand-algo': {
          const sub = this.view.subpixelPosition(event) || { sx: pos.x * 3 + 1, sy: pos.y * 4 + 1 };
          this.subpixelRaster = this.algo.createSubpixelRaster();
          this.subpixelRaster.setPixel(sub.sx, sub.sy);
          this.previousSub = sub;
          this.view.setPreview(this.subpixelRaster.toAsciiCells());
          break;
        }

        case 'line-generic':
        case 'line-algo':
        case 'rect-generic':
        case 'rect-algo':
        case 'ellipse-generic':
        case 'ellipse-algo':
          this.updateShapePreview(pos);
          break;

        case 'bezier-generic':
        case 'bezier-algo':
          this.handleBezierClick(pos);
          break;
      }
    }

    pointerMove(event) {
      // Middle-click panning
      if (this.lastScreenPos) {
        const dx = event.clientX - this.lastScreenPos.x;
        const dy = event.clientY - this.lastScreenPos.y;
        this.view.viewport.scrollLeft -= dx;
        this.view.viewport.scrollTop -= dy;
        this.lastScreenPos = { x: event.clientX, y: event.clientY };
        return;
      }

      const pos = this.view.position(event);
      if (!pos) return;

      // Moving active selection
      if (this.movingSelection && this.moveOrigin) {
        const dx = (pos.x - this.moveOrigin.x) + this.moveOrigin.initOffsetX;
        const dy = (pos.y - this.moveOrigin.y) + this.moveOrigin.initOffsetY;
        this.model.moveSelection(dx, dy);
        return;
      }

      // Right-click dragging (eraser)
      if (event.buttons & 2) {
        const interpolated = lineCells(this.previous ?? pos, pos, this.model.doc.width);
        for (const idx of interpolated) {
          const cx = idx % this.model.doc.width, cy = Math.floor(idx / this.model.doc.width);
          const footprint = getBrushFootprint(cx, cy, this.thickness, this.model.doc.width, this.model.doc.height);
          this.model.paintCells(footprint, { isEraser: true });
        }
        this.previous = pos;
        return;
      }

      switch (this.activeTool) {
        case 'select-rect':
          this.view.setPreview([], { type: 'rect', start: this.startPos, current: pos });
          break;

        case 'select-lasso':
          if (pos.x !== this.previous?.x || pos.y !== this.previous?.y) {
            this.strokePoints.push(pos);
            this.view.setPreview([], { type: 'lasso', points: this.strokePoints });
            this.previous = pos;
          }
          break;

        case 'freehand-generic':
        case 'eraser':
        case 'colorize':
        case 'pattern-brush': {
          this.paintStrokeStep(this.previous ?? pos, pos);
          this.previous = pos;
          break;
        }

        case 'freehand-algo': {
          const sub = this.view.subpixelPosition(event) || { sx: pos.x * 3 + 1, sy: pos.y * 4 + 1 };
          if (this.subpixelRaster && this.previousSub && (sub.sx !== this.previousSub.sx || sub.sy !== this.previousSub.sy)) {
            this.subpixelRaster.drawLine(this.previousSub.sx, this.previousSub.sy, sub.sx, sub.sy);
            this.view.setPreview(this.subpixelRaster.toAsciiCells());
            this.previousSub = sub;
            this.previous = pos;
          }
          break;
        }

        case 'line-generic':
        case 'line-algo':
        case 'rect-generic':
        case 'rect-algo':
        case 'ellipse-generic':
        case 'ellipse-algo':
          this.updateShapePreview(pos);
          break;

        case 'bezier-generic':
        case 'bezier-algo':
          if (this.bezierStep > 0) {
            this.updateBezierMove(pos);
          }
          break;
      }
    }

    hoverMove(event) {
      const pos = this.view.position(event);
      if (!pos) return;

      if (this.bezierStep > 0) {
        this.updateBezierMove(pos);
      }
    }

    pointerUp(event) {
      const pos = this.view.position(event) || this.previous || this.startPos;

      if (this.lastScreenPos) {
        this.lastScreenPos = null;
      }

      if (this.movingSelection) {
        this.movingSelection = false;
        this.moveOrigin = null;
        this.finish();
        return;
      }

      if (!this.startPos || !pos) {
        this.finish();
        return;
      }

      switch (this.activeTool) {
        case 'select-rect': {
          const x1 = this.startPos.x, y1 = this.startPos.y, x2 = pos.x, y2 = pos.y;
          const minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
          const minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
          const w = maxX - minX + 1, h = maxY - minY + 1;
          const cells = [];
          for (let y = minY; y <= maxY; y++) {
            for (let x = minX; x <= maxX; x++) cells.push({ x, y });
          }
          this.view.clearPreview();
          if (w > 0 && h > 0) {
            this.model.setSelection('rect', cells, { minX, minY, maxX, maxY, width: w, height: h });
          }
          break;
        }

        case 'select-lasso': {
          this.view.clearPreview();
          if (this.strokePoints.length >= 3) {
            const poly = this.strokePoints;
            let minX = poly[0].x, maxX = poly[0].x, minY = poly[0].y, maxY = poly[0].y;
            for (const p of poly) {
              if (p.x < minX) minX = p.x;
              if (p.x > maxX) maxX = p.x;
              if (p.y < minY) minY = p.y;
              if (p.y > maxY) maxY = p.y;
            }
            const cells = [];
            for (let y = minY; y <= maxY; y++) {
              for (let x = minX; x <= maxX; x++) {
                if (pointInPolygon(x + 0.5, y + 0.5, poly)) {
                  cells.push({ x, y });
                }
              }
            }
            if (cells.length > 0) {
              this.model.setSelection('lasso', cells, {
                minX, minY, maxX, maxY,
                width: maxX - minX + 1,
                height: maxY - minY + 1
              });
            }
          } else {
            // Single-click selection for lasso
            const p = pos || this.startPos;
            if (p) {
              this.model.setSelection('lasso', [{ x: p.x, y: p.y }], {
                minX: p.x, minY: p.y, maxX: p.x, maxY: p.y,
                width: 1, height: 1
              });
            }
          }
          this.strokePoints = [];
          break;
        }

        case 'freehand-generic':
        case 'eraser':
        case 'colorize':
        case 'pattern-brush':
          this.model.commit();
          break;

        case 'freehand-algo': {
          this.view.clearPreview();
          if (this.subpixelRaster) {
            if (this.subpixelRaster.cells.size === 1 && this.previousSub) {
              const onlyEntry = Array.from(this.subpixelRaster.cells.values())[0];
              if ((onlyEntry & (onlyEntry - 1)) === 0) {
                this.subpixelRaster.setPixel(this.previousSub.sx + 1, this.previousSub.sy);
              }
            }
            const algoCells = this.subpixelRaster.toAsciiCells();
            this.model.paintCells(algoCells, { opacity: this.opacity });
            this.model.commit();
          }
          this.subpixelRaster = null;
          this.previousSub = null;
          this.strokePoints = [];
          break;
        }

        case 'line-generic': {
          this.view.clearPreview();
          const indices = lineCells(this.startPos, pos, this.model.doc.width);
          const allPoints = [];
          const footprintOpts = {
            falloff: this.falloff,
            baseChar: this.model.brush.char || '@',
            baseOpacity: this.opacity
          };
          for (const idx of indices) {
            const cx = idx % this.model.doc.width, cy = Math.floor(idx / this.model.doc.width);
            allPoints.push(...getBrushFootprint(cx, cy, this.thickness, this.model.doc.width, this.model.doc.height, footprintOpts));
          }
          this.model.paintCells(allPoints, { opacity: this.opacity });
          this.model.commit();
          break;
        }

        case 'line-algo': {
          this.view.clearPreview();
          const algoCells = this.algo.generateLine(this.startPos.x, this.startPos.y, pos.x, pos.y);
          this.model.paintCells(algoCells, { opacity: this.opacity });
          this.model.commit();
          break;
        }

        case 'rect-generic': {
          this.view.clearPreview();
          const minX = Math.min(this.startPos.x, pos.x), maxX = Math.max(this.startPos.x, pos.x);
          const minY = Math.min(this.startPos.y, pos.y), maxY = Math.max(this.startPos.y, pos.y);
          const perimeter = [];
          for (let x = minX; x <= maxX; x++) { perimeter.push({ x, y: minY }, { x, y: maxY }); }
          for (let y = minY; y <= maxY; y++) { perimeter.push({ x: minX, y }, { x: maxX, y }); }
          const allPoints = [];
          const footprintOpts = {
            falloff: this.falloff,
            baseChar: this.model.brush.char || '@',
            baseOpacity: this.opacity
          };
          for (const p of perimeter) {
            allPoints.push(...getBrushFootprint(p.x, p.y, this.thickness, this.model.doc.width, this.model.doc.height, footprintOpts));
          }
          this.model.paintCells(allPoints, { opacity: this.opacity });
          this.model.commit();
          break;
        }

        case 'rect-algo': {
          this.view.clearPreview();
          const algoCells = this.algo.generateRectangle(this.startPos.x, this.startPos.y, pos.x, pos.y);
          this.model.paintCells(algoCells, { opacity: this.opacity });
          this.model.commit();
          break;
        }

        case 'ellipse-generic': {
          this.view.clearPreview();
          const genericAlgo = new AlgorithmicAscii();
          const ellipseCells = genericAlgo.generateEllipse(this.startPos.x, this.startPos.y, pos.x, pos.y);
          const allPoints = [];
          const footprintOpts = {
            falloff: this.falloff,
            baseChar: this.model.brush.char || '@',
            baseOpacity: this.opacity
          };
          for (const p of ellipseCells) {
            allPoints.push(...getBrushFootprint(p.x, p.y, this.thickness, this.model.doc.width, this.model.doc.height, footprintOpts));
          }
          this.model.paintCells(allPoints, { opacity: this.opacity });
          this.model.commit();
          break;
        }

        case 'ellipse-algo': {
          this.view.clearPreview();
          const algoCells = this.algo.generateEllipse(this.startPos.x, this.startPos.y, pos.x, pos.y);
          this.model.paintCells(algoCells, { opacity: this.opacity });
          this.model.commit();
          break;
        }

        case 'bezier-generic':
        case 'bezier-algo':
          // Bézier uses multi-click steps
          break;
      }

      this.finish();
    }

    paintStrokeStep(from, to) {
      const indices = lineCells(from, to, this.model.doc.width);
      const points = [];
      const footprintOpts = {
        falloff: this.falloff && this.activeTool === 'freehand-generic',
        baseChar: this.model.brush.char || '@',
        baseOpacity: this.opacity
      };
      for (const idx of indices) {
        const cx = idx % this.model.doc.width, cy = Math.floor(idx / this.model.doc.width);
        points.push(...getBrushFootprint(cx, cy, this.thickness, this.model.doc.width, this.model.doc.height, footprintOpts));
      }

      if (this.activeTool === 'eraser') {
        this.model.paintCells(points, { isEraser: true });
      } else if (this.activeTool === 'colorize') {
        this.model.paintCells(points, { isColorize: true, opacity: this.opacity });
      } else if (this.activeTool === 'pattern-brush') {
        const pLines = this.patternLines;
        const pHeight = pLines.length;
        const patternedCells = points.map(pt => {
          const row = ((pt.y % pHeight) + pHeight) % pHeight;
          const line = pLines[row] || ' ';
          const col = ((pt.x % line.length) + line.length) % line.length;
          return {
            x: pt.x,
            y: pt.y,
            char: line[col] || ' '
          };
        });
        this.model.paintCells(patternedCells, { opacity: this.opacity });
      } else {
        this.model.paintCells(points, { opacity: this.opacity });
      }
    }

    updateShapePreview(pos) {
      if (!this.startPos) return;
      const x1 = this.startPos.x, y1 = this.startPos.y, x2 = pos.x, y2 = pos.y;
      const footprintOpts = {
        falloff: this.falloff,
        baseChar: this.model.brush.char || '@',
        baseOpacity: this.opacity
      };

      switch (this.activeTool) {
        case 'line-generic': {
          const indices = lineCells(this.startPos, pos, this.model.doc.width);
          const points = [];
          for (const idx of indices) {
            const cx = idx % this.model.doc.width, cy = Math.floor(idx / this.model.doc.width);
            points.push(...getBrushFootprint(cx, cy, this.thickness, this.model.doc.width, this.model.doc.height, footprintOpts));
          }
          this.view.setPreview(points);
          break;
        }

        case 'line-algo': {
          const cells = this.algo.generateLine(x1, y1, x2, y2);
          this.view.setPreview(cells);
          break;
        }

        case 'rect-generic': {
          const minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
          const minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
          const perimeter = [];
          for (let x = minX; x <= maxX; x++) { perimeter.push({ x, y: minY }, { x, y: maxY }); }
          for (let y = minY; y <= maxY; y++) { perimeter.push({ x: minX, y }, { x: maxX, y }); }
          const points = [];
          for (const p of perimeter) {
            points.push(...getBrushFootprint(p.x, p.y, this.thickness, this.model.doc.width, this.model.doc.height, footprintOpts));
          }
          this.view.setPreview(points);
          break;
        }

        case 'rect-algo': {
          const cells = this.algo.generateRectangle(x1, y1, x2, y2);
          this.view.setPreview(cells);
          break;
        }

        case 'ellipse-generic': {
          const ellipseCells = this.algo.generateEllipse(x1, y1, x2, y2);
          const points = [];
          for (const p of ellipseCells) {
            points.push(...getBrushFootprint(p.x, p.y, this.thickness, this.model.doc.width, this.model.doc.height, footprintOpts));
          }
          this.view.setPreview(points);
          break;
        }

        case 'ellipse-algo': {
          const cells = this.algo.generateEllipse(x1, y1, x2, y2);
          this.view.setPreview(cells);
          break;
        }
      }
    }

    // --- BÉZIER PEN TOOL INTERACTION ---
    handleBezierClick(pos) {
      if (this.bezierStep === 0) {
        // Step 1: Place start point
        this.bezierPoints = [pos];
        this.bezierStep = 1;
        this.view.setPreview([], { type: 'bezier', points: [pos, pos] });
      } else if (this.bezierStep === 1) {
        // Step 2: Place end point
        this.bezierPoints.push(pos);
        const midX = Math.round((this.bezierPoints[0].x + pos.x) / 2);
        const midY = Math.round((this.bezierPoints[0].y + pos.y) / 2);
        this.bezierPoints = [this.bezierPoints[0], { x: midX, y: midY }, pos];
        this.bezierStep = 2;
        this.updateBezierPreview();
      } else if (this.bezierStep === 2) {
        // Step 3: Commit curve
        this.commitBezier();
      }
    }

    updateBezierMove(pos) {
      if (this.bezierStep === 1) {
        const pts = [this.bezierPoints[0], pos];
        this.view.setPreview([], { type: 'bezier', points: pts });
      } else if (this.bezierStep === 2) {
        this.bezierPoints[1] = pos;
        this.updateBezierPreview();
      }
    }

    updateBezierPreview() {
      if (this.bezierStep < 2 || this.bezierPoints.length < 3) return;
      const pts = this.bezierPoints;
      let cells;
      if (this.activeTool === 'bezier-algo') {
        cells = this.algo.generateBezier(pts);
      } else {
        const rawPts = this.algo.generateGenericBezierPoints(pts);
        const footprintPts = [];
        for (const p of rawPts) {
          footprintPts.push(...getBrushFootprint(p.x, p.y, this.thickness, this.model.doc.width, this.model.doc.height));
        }
        cells = footprintPts.map(p => ({ ...p, char: this.model.brush.char }));
      }
      this.view.setPreview(cells, { type: 'bezier', points: pts });
    }

    commitBezier() {
      if (this.bezierStep !== 2 || this.bezierPoints.length < 3) return;
      const pts = this.bezierPoints;
      this.view.clearPreview();

      if (this.activeTool === 'bezier-algo') {
        const cells = this.algo.generateBezier(pts);
        this.model.paintCells(cells, { opacity: this.opacity });
      } else {
        const rawPts = this.algo.generateGenericBezierPoints(pts);
        const footprintPts = [];
        for (const p of rawPts) {
          footprintPts.push(...getBrushFootprint(p.x, p.y, this.thickness, this.model.doc.width, this.model.doc.height));
        }
        this.model.paintCells(footprintPts, { opacity: this.opacity });
      }
      this.model.commit();
      this.bezierStep = 0;
      this.bezierPoints = [];
    }

    cancelBezier() {
      this.bezierStep = 0;
      this.bezierPoints = [];
      this.view.clearPreview();
    }

    finish() {
      const pointer = this.pointer;
      this.pointer = null;
      this.previous = null;
      this.startPos = null;
      this.lastScreenPos = null;

      if (pointer !== null && this.view.canvas.hasPointerCapture(pointer)) {
        this.view.canvas.releasePointerCapture(pointer);
      }
      this.model.commit();
    }

    keydown(event) {
      if (event.defaultPrevented || event.isComposing) return;
      if (event.target?.closest && event.target.closest('input, textarea, select, [contenteditable="true"]')) return;

      // In text editing mode, catch keyboard typing
      if (this.activeTool === 'text' && this.view.textCursor) {
        if (this.handleTextModeKey(event)) return;
      }

      const key = event.key.toLowerCase();
      let action;

      if (event.ctrlKey || event.metaKey) {
        if (key === 'z') action = () => { this.timeline.stop(); this.model.travel(event.shiftKey); };
        if (key === 'y') action = () => { this.timeline.stop(); this.model.travel(true); };
        if (key === 'c') action = () => { this.model.copySelection(); };
        if (key === 'x') action = () => { this.model.cutSelection(); };
        if (key === 'v') action = async () => {
          const target = this.currentMouseCell || this.view.getVisibleCenterCell();
          const clip = this.model.getClipboard();
          let tx = target.x, ty = target.y;

          // Try OS clipboard first
          let osText = null;
          try {
            if (navigator.clipboard && navigator.clipboard.readText) {
              osText = await navigator.clipboard.readText();
            }
          } catch { /* permission denied or not available */ }

          if (osText && osText.trim()) {
            // Use OS clipboard text – center it around the cursor like an internal paste
            const lines = osText.split('\n');
            const h = lines.length;
            const w = Math.max(...lines.map(l => [...l].length));
            tx = Math.max(0, Math.min(this.model.doc.width - w, target.x - Math.floor(w / 2)));
            ty = Math.max(0, Math.min(this.model.doc.height - h, target.y - Math.floor(h / 2)));
            this.model.pasteFromText(osText, tx, ty);
          } else {
            // Fall back to internal clipboard
            if (clip) {
              tx = Math.max(0, Math.min(this.model.doc.width - clip.width, target.x - Math.floor(clip.width / 2)));
              ty = Math.max(0, Math.min(this.model.doc.height - clip.height, target.y - Math.floor(clip.height / 2)));
            }
            this.model.pasteClipboard(tx, ty);
          }
        };
      } else if (event.shiftKey && !event.ctrlKey && !event.metaKey && !event.altKey) {
        if (this.model.selection) {
          if (event.key === 'ArrowRight') { this.model.adjustSelection(1, 0); event.preventDefault(); return; }
          if (event.key === 'ArrowLeft') { this.model.adjustSelection(-1, 0); event.preventDefault(); return; }
          if (event.key === 'ArrowDown') { this.model.adjustSelection(0, 1); event.preventDefault(); return; }
          if (event.key === 'ArrowUp') { this.model.adjustSelection(0, -1); event.preventDefault(); return; }
        }
      } else if (!event.ctrlKey && !event.metaKey && !event.altKey && !event.shiftKey) {
        if (TOOL_SHORTCUT_GROUPS[key]) {
          this.cycleTool(key);
          event.preventDefault();
          return;
        }

        if (key === 'escape') {
          if (this.bezierStep > 0) this.cancelBezier();
          else if (this.model.selection) this.model.commitSelection();
          else if (this.view.textCursor) this.view.setTextCursor(null);
          return;
        }
        if (key === 'enter') {
          if (this.bezierStep === 2) this.commitBezier();
          else if (this.model.selection) this.model.commitSelection();
          return;
        }
        if (key === 'delete' || key === 'backspace') {
          if (this.model.selection) {
            this.model.deleteSelection();
            event.preventDefault();
            return;
          }
        }
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

    handleTextModeKey(event) {
      const cur = this.view.textCursor;
      if (!cur) return false;

      const doc = this.model.doc;

      if (event.key === 'Escape') {
        this.view.setTextCursor(null);
        event.preventDefault();
        return true;
      }

      if (event.key === 'Enter') {
        const nextY = Math.min(doc.height - 1, cur.y + 1);
        this.view.setTextCursor({ x: 0, y: nextY });
        event.preventDefault();
        return true;
      }

      if (event.key === 'Backspace') {
        const newX = Math.max(0, cur.x - 1);
        this.model.typeCharacter(newX, cur.y, ' ');
        this.view.setTextCursor({ x: newX, y: cur.y });
        event.preventDefault();
        return true;
      }

      if (event.key === 'Delete') {
        this.model.typeCharacter(cur.x, cur.y, ' ');
        event.preventDefault();
        return true;
      }

      if (event.key === 'ArrowLeft') {
        this.view.setTextCursor({ x: Math.max(0, cur.x - 1), y: cur.y });
        event.preventDefault();
        return true;
      }
      if (event.key === 'ArrowRight') {
        this.view.setTextCursor({ x: Math.min(doc.width - 1, cur.x + 1), y: cur.y });
        event.preventDefault();
        return true;
      }
      if (event.key === 'ArrowUp') {
        this.view.setTextCursor({ x: cur.x, y: Math.max(0, cur.y - 1) });
        event.preventDefault();
        return true;
      }
      if (event.key === 'ArrowDown') {
        this.view.setTextCursor({ x: cur.x, y: Math.min(doc.height - 1, cur.y + 1) });
        event.preventDefault();
        return true;
      }

      // Check printable character
      if (event.key.length === 1 && !event.ctrlKey && !event.metaKey && !event.altKey) {
        this.model.typeCharacter(cur.x, cur.y, event.key);
        let nextX = cur.x + 1;
        let nextY = cur.y;
        if (nextX >= doc.width) {
          nextX = 0;
          nextY = Math.min(doc.height - 1, cur.y + 1);
        }
        this.view.setTextCursor({ x: nextX, y: nextY });
        event.preventDefault();
        return true;
      }

      return false;
    }
  }

  globalThis.Gestures = Gestures;
  globalThis.lineCells = lineCells;
  globalThis.getAsciiCharForOpacity = getAsciiCharForOpacity;
  globalThis.getBrushFootprint = getBrushFootprint;
})();