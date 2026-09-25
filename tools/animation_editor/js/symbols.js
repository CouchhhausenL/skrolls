(() => {
  'use strict';
  const $ = id => document.getElementById(id);
  const GROUPS = [
    ['space blank', ' '],
    ['blocks shade solid fill', '█▓▒░▀▄▌▐▖▗▘▙▚▛▜▝▞▟'],
    ['box lines borders single', '─│┌┐└┘├┤┬┴┼╭╮╰╯'],
    ['box lines borders double', '═║╔╗╚╝╠╣╦╩╬'],
    ['arrows direction', '←↑→↓↔↕↖↗↘↙⇒⇐⇑⇓'],
    ['shapes circles squares triangles diamond', '■□▪▫●○◉◎◌◆◇▲△▼▽◀▶'],
    ['stars symbols suits', '★☆✦✧♥♡♦♣♠♩♪♫✓✗☀☁☂☺☻'],
    ['math operators', '+−×÷=≠≈≤≥±∞√∑π'],
    ['punctuation ascii', '.:,;!?-_~|/\\()[]{}<>@#$%&*\'"`^'],
    ['digits numbers', '0123456789'],
    ['letters uppercase', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'],
    ['letters lowercase', 'abcdefghijklmnopqrstuvwxyz']
  ];

  class SymbolControls {
    constructor(model, report) {
      this.model = model;
      this.report = report;
      this.tab = 'common';
      this.lastChar = model.brush.char;
      this.names = new Map();
      for (const [name, chars] of GROUPS) {
        for (const char of chars) this.names.set(char, name);
      }
      $('character-input').addEventListener('input', event => {
        if (event.isComposing) return;
        this.input(event.target.value);
      });
      $('character-input').addEventListener('compositionend', event => this.input(event.target.value));
      $('character-input').addEventListener('blur', () => this.sync());
      $('char-none').onchange = event => {
        model.brush.char = event.target.checked ? null : this.lastChar;
        model.emit('brush');
      };
      $('symbol-search').oninput = () => this.render();
      $('symbols-common').onclick = () => this.setTab('common');
      $('symbols-used').onclick = () => this.setTab('used');
      model.addEventListener('change', event => {
        if (event.detail === 'brush') this.sync();
        else if (this.tab === 'used' && ['commit', 'load', 'history'].includes(event.detail)) this.render();
      });
      this.render();
      this.sync();
    }

    input(text) {
      if (!text) return;
      const char = Array.from(text).at(-1);
      if (!SKA.validChar(char)) { this.report('Choose one printable Unicode character (no control characters).', true); this.sync(); return; }
      this.choose(char);
    }

    choose(char) {
      this.model.brush.char = char;
      this.model.emit('brush');
    }

    setTab(tab) {
      this.tab = tab;
      for (const key of ['common', 'used']) {
        $(`symbols-${key}`).classList.toggle('selected', key === tab);
        $(`symbols-${key}`).setAttribute('aria-selected', String(key === tab));
      }
      this.render();
    }

    sync() {
      const char = this.model.brush.char;
      if (char !== null) this.lastChar = char;
      $('character-input').value = char ?? this.lastChar;
      $('character-input').disabled = char === null;
      $('char-none').checked = char === null;
      for (const button of $('symbol-grid').querySelectorAll('button')) {
        button.classList.toggle('selected', button.dataset.char === char);
        button.setAttribute('aria-pressed', String(button.dataset.char === char));
      }
    }

    render() {
      const query = $('symbol-search').value.toLowerCase().trim();
      let chars = [...this.names.keys()];
      if (this.tab === 'used') {
        const used = new Set();
        for (const frame of this.model.doc.frames) for (const cell of frame) used.add(cell.char);
        chars = [...used].sort((a, b) => a.codePointAt(0) - b.codePointAt(0));
      }
      const buttons = chars.filter(char => {
        const code = 'u+' + char.codePointAt(0).toString(16).padStart(4, '0');
        return !query || char.toLowerCase().includes(query) || (this.names.get(char) ?? '').includes(query) || code.includes(query);
      }).map(char => {
        const button = document.createElement('button');
        const code = 'U+' + char.codePointAt(0).toString(16).toUpperCase().padStart(4, '0');
        button.textContent = char === ' ' ? '␣' : char;
        button.dataset.char = char;
        button.title = `${char === ' ' ? 'Space' : char} · ${code} · ${this.names.get(char) ?? 'In animation'}`;
        button.setAttribute('aria-label', button.title);
        button.onclick = () => this.choose(char);
        return button;
      });
      $('symbol-grid').replaceChildren(...buttons);
      if (!buttons.length) {
        const empty = document.createElement('p');
        empty.className = 'note';
        empty.textContent = 'No matching characters.';
        $('symbol-grid').append(empty);
      }
      this.sync();
    }
  }

  globalThis.SymbolControls = SymbolControls;
})();
