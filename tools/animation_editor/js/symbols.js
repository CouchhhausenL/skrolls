(() => {
  'use strict';
  const $ = id => document.getElementById(id);

  class SymbolControls {
    constructor(model, report) {
      this.model = model;
      this.report = report;
      this.lastChar = model.brush.char || '@';

      const charInput = $('character-input');
      if (charInput) {
        charInput.addEventListener('input', event => {
          if (event.isComposing) return;
          this.input(event.target.value);
        });
        charInput.addEventListener('compositionend', event => this.input(event.target.value));
        charInput.addEventListener('blur', () => this.sync());
      }

      const charNone = $('char-none');
      if (charNone) {
        charNone.onchange = event => {
          model.brush.char = event.target.checked ? null : this.lastChar;
          model.emit('brush');
        };
      }

      model.addEventListener('change', event => {
        if (event.detail === 'brush') this.sync();
      });

      this.sync();
    }

    input(text) {
      if (!text) return;
      const char = Array.from(text).at(-1);
      if (!SKA.validChar(char)) {
        this.report('Choose one printable ASCII / Unicode character.', true);
        this.sync();
        return;
      }
      this.choose(char);
    }

    choose(char) {
      this.model.brush.char = char;
      this.model.emit('brush');
    }

    sync() {
      const char = this.model.brush.char;
      if (char !== null && char !== undefined) this.lastChar = char;
      const charInput = $('character-input');
      if (charInput) {
        charInput.value = char ?? this.lastChar;
        charInput.disabled = char === null;
      }
      const charNone = $('char-none');
      if (charNone) {
        charNone.checked = char === null;
      }
    }
  }

  globalThis.SymbolControls = SymbolControls;
})();
