(() => {
  'use strict';
  const $ = id => document.getElementById(id);
  const STORAGE_KEY = 'ska-editor-favorites-v1';

  function hsvToHex(h, s, v) {
    const f = n => {
      const k = (n + h / 60) % 6;
      return Math.round(255 * (v - v * s * Math.max(0, Math.min(k, 4 - k, 1)))).toString(16).padStart(2, '0');
    };
    return '#' + f(5) + f(3) + f(1);
  }

  function hexToHsv(hex) {
    const rgb = hex.slice(1).match(/../g).map(value => parseInt(value, 16) / 255);
    const [r, g, b] = rgb, max = Math.max(...rgb), min = Math.min(...rgb), delta = max - min;
    let h = 0;
    if (delta) {
      if (max === r) h = ((g - b) / delta) % 6;
      else if (max === g) h = (b - r) / delta + 2;
      else h = (r - g) / delta + 4;
      h *= 60;
    }
    return { h: (h + 360) % 360, s: max ? delta / max : 0, v: max };
  }

  class ColorControls {
    constructor(model, report) {
      this.model = model;
      this.report = report;
      this.target = 'fg';
      this.remembered = { fg: SKA.DEFAULT_FG, bg: model.doc.background };
      this.hsv = { h: 0, s: 0, v: 1 };
      this.favorites = ['#ffffff', '#171a21', '#a8e6b0', '#f3cf83', '#ee8e91', '#8eaff0'];
      try {
        const stored = JSON.parse(localStorage.getItem(STORAGE_KEY));
        if (Array.isArray(stored)) this.favorites = [...new Set(stored.filter(c => typeof c === 'string' && SKA.HEX.test(c)).map(c => c.toLowerCase()))].slice(0, 48);
      } catch { /* Storage is optional, including on file://. */ }
      this.bind();
      this.renderPalette();
      this.sync();
      model.addEventListener('change', event => {
        if (['brush', 'load', 'history'].includes(event.detail)) this.sync();
      });
    }

    bind() {
      for (const key of ['fg', 'bg']) {
        $(`select-${key}`).onclick = () => { this.target = key; this.sync(); };
        $(`${key}-none`).onchange = event => {
          if (event.target.checked) {
            this.remembered[key] = this.model.brush[key] ?? this.remembered[key];
            this.model.brush[key] = null;
          } else this.model.brush[key] = this.remembered[key];
          this.model.emit('brush');
        };
      }
      $('swap-colors').onclick = () => {
        const brush = this.model.brush;
        [brush.fg, brush.bg] = [brush.bg, brush.fg];
        [this.remembered.fg, this.remembered.bg] = [this.remembered.bg, this.remembered.fg];
        this.model.emit('brush');
      };
      $('reset-colors').onclick = () => {
        this.model.brush.fg = SKA.DEFAULT_FG;
        this.model.brush.bg = this.model.doc.background;
        this.model.emit('brush');
      };
      $('hex-color').onchange = event => {
        const hex = '#' + event.target.value.replace(/^#/, '').toLowerCase();
        if (!SKA.HEX.test(hex)) { this.report('Use a six-digit hex color, for example #a8e6b0.', true); this.sync(); return; }
        this.choose(hex);
      };
      $('brightness').oninput = event => { this.hsv.v = Number(event.target.value) / 100; this.chooseHsv(); };
      const wheel = $('color-wheel');
      const pick = event => {
        const rect = wheel.getBoundingClientRect();
        const x = (event.clientX - rect.left) / rect.width * 2 - 1;
        const y = (event.clientY - rect.top) / rect.height * 2 - 1;
        this.hsv.h = (Math.atan2(y, x) * 180 / Math.PI + 360) % 360;
        this.hsv.s = Math.min(1, Math.hypot(x, y) / (86 / 90));
        this.chooseHsv();
      };
      let pointer = null;
      wheel.onpointerdown = event => {
        if (event.button !== 0) return;
        pointer = event.pointerId;
        wheel.setPointerCapture(pointer);
        pick(event);
      };
      wheel.onpointermove = event => { if (event.pointerId === pointer) pick(event); };
      wheel.onpointerup = wheel.onpointercancel = wheel.onlostpointercapture = () => { pointer = null; };
      wheel.onkeydown = event => {
        if (!['ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown'].includes(event.key)) return;
        event.preventDefault();
        event.stopPropagation();
        if (event.key === 'ArrowLeft') this.hsv.h = (this.hsv.h + 355) % 360;
        if (event.key === 'ArrowRight') this.hsv.h = (this.hsv.h + 5) % 360;
        if (event.key === 'ArrowUp') this.hsv.s = Math.min(1, this.hsv.s + 0.05);
        if (event.key === 'ArrowDown') this.hsv.s = Math.max(0, this.hsv.s - 0.05);
        this.chooseHsv();
      };
      $('favorite-color').onclick = () => {
        const color = hsvToHex(this.hsv.h, this.hsv.s, this.hsv.v);
        if (this.favorites.includes(color)) return;
        if (this.favorites.length >= 48) { this.report('Palette is full. Right-click a favorite to remove it.', true); return; }
        this.favorites.push(color);
        this.savePalette();
      };
    }

    chooseHsv() {
      this.preserveHsv = true;
      this.choose(hsvToHex(this.hsv.h, this.hsv.s, this.hsv.v));
      this.preserveHsv = false;
    }

    choose(hex) {
      this.model.brush[this.target] = hex;
      this.model.emit('brush');
    }

    sync() {
      for (const key of ['fg', 'bg']) {
        const color = this.model.brush[key];
        if (color !== null) this.remembered[key] = color;
        $(`${key}-none`).checked = color === null;
        $(`${key}-swatch`).style.background = color ?? 'transparent';
        $(`${key}-swatch`).classList.toggle('none', color === null);
        $(`select-${key}`).classList.toggle('selected', key === this.target);
        $(`select-${key}`).setAttribute('aria-pressed', String(key === this.target));
      }
      const color = this.model.brush[this.target] ?? this.remembered[this.target];
      if (!this.preserveHsv) this.hsv = hexToHsv(color);
      $('hex-color').value = color;
      $('brightness').value = Math.round(this.hsv.v * 100);
      this.drawWheel();
    }

    drawWheel() {
      const canvas = $('color-wheel'), ctx = canvas.getContext('2d');
      if (this.wheelBrightness !== this.hsv.v) {
        const image = ctx.createImageData(180, 180);
        for (let y = 0; y < 180; y++) {
          for (let x = 0; x < 180; x++) {
            const dx = x - 90, dy = y - 90, saturation = Math.hypot(dx, dy) / 86;
            if (saturation > 1) continue;
            const hue = (Math.atan2(dy, dx) * 180 / Math.PI + 360) % 360;
            const hex = hsvToHex(hue, saturation, this.hsv.v);
            const i = (y * 180 + x) * 4;
            image.data[i] = parseInt(hex.slice(1, 3), 16);
            image.data[i + 1] = parseInt(hex.slice(3, 5), 16);
            image.data[i + 2] = parseInt(hex.slice(5, 7), 16);
            image.data[i + 3] = 255;
          }
        }
        this.wheelImage = image;
        this.wheelBrightness = this.hsv.v;
      }
      ctx.putImageData(this.wheelImage, 0, 0);
      const angle = this.hsv.h * Math.PI / 180;
      ctx.beginPath();
      ctx.arc(90 + Math.cos(angle) * this.hsv.s * 86, 90 + Math.sin(angle) * this.hsv.s * 86, 5, 0, Math.PI * 2);
      ctx.strokeStyle = '#000'; ctx.lineWidth = 4; ctx.stroke();
      ctx.strokeStyle = '#fff'; ctx.lineWidth = 2; ctx.stroke();
      canvas.setAttribute('aria-valuenow', String(Math.round(this.hsv.h) % 360));
    }

    savePalette() {
      try { localStorage.setItem(STORAGE_KEY, JSON.stringify(this.favorites)); }
      catch { this.report('Favorites are available this session; browser storage is unavailable.'); }
      this.renderPalette();
    }

    renderPalette() {
      $('palette').replaceChildren(...this.favorites.map(color => {
        const button = document.createElement('button');
        button.style.background = color;
        button.title = `${color} · Right-click to remove`;
        button.setAttribute('aria-label', `Use favorite ${color}`);
        button.onclick = () => this.choose(color);
        button.oncontextmenu = event => {
          event.preventDefault();
          this.favorites = this.favorites.filter(c => c !== color);
          this.savePalette();
        };
        return button;
      }));
    }
  }

  globalThis.ColorControls = ColorControls;
})();
