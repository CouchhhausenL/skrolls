(() => {
  'use strict';
  const $ = id => document.getElementById(id);

  class Timeline {
    constructor(model, run) {
      this.model = model;
      this.run = run;
      this.playing = false;
      this.cards = [];
      this.queue = false;
      $('previous-frame').onclick = () => this.step(-1);
      $('next-frame').onclick = () => this.step(1);
      $('play').onclick = () => this.toggle();
      for (const [id, action] of [
        ['add-frame', () => model.insert()],
        ['duplicate-frame', () => model.insert(true)],
        ['delete-frame', () => model.remove()]
      ]) $(id).onclick = () => { this.stop(); run(action); };
      model.addEventListener('change', event => {
        if (event.detail === 'commit' && this.cards[model.index]) this.cards[model.index].frame = null;
        if (['brush', 'saved', 'paint'].includes(event.detail)) return;
        this.schedule();
      });
      document.addEventListener('visibilitychange', () => { if (document.hidden) this.stop(); });
      this.render();
    }

    schedule() {
      if (this.queue) return;
      this.queue = true;
      requestAnimationFrame(() => { this.queue = false; this.render(); });
    }

    render() {
      const { doc, index } = this.model;
      const list = $('timeline');
      while (this.cards.length > doc.frames.length) this.cards.pop().button.remove();
      while (this.cards.length < doc.frames.length) {
        const i = this.cards.length;
        const button = document.createElement('button');
        button.className = 'frame-card';
        button.setAttribute('role', 'listitem');
        const canvas = document.createElement('canvas');
        canvas.width = 144; canvas.height = 98;
        canvas.setAttribute('aria-hidden', 'true');
        const label = document.createElement('span');
        label.textContent = String(i + 1).padStart(3, '0');
        button.append(canvas, label);
        button.onclick = () => { this.stop(); this.model.select(i); };
        list.append(button);
        this.cards.push({ button, canvas, frame: null, size: '' });
      }
      this.cards.forEach((card, i) => {
        const size = `${doc.width},${doc.height}`;
        if (card.frame !== doc.frames[i] || card.size !== size) {
          drawFrame(card.canvas.getContext('2d'), doc, doc.frames[i], 144, 98);
          card.frame = doc.frames[i];
          card.size = size;
        }
        card.button.classList.toggle('selected', i === index);
        card.button.setAttribute('aria-current', i === index ? 'true' : 'false');
        card.button.setAttribute('aria-label', `Frame ${i + 1}${i === index ? ', current' : ''}`);
      });
      const selected = this.cards[index]?.button;
      if (selected) {
        const left = selected.offsetLeft - list.offsetLeft;
        if (left < list.scrollLeft) list.scrollLeft = left;
        if (left + selected.offsetWidth > list.scrollLeft + list.clientWidth) list.scrollLeft = left + selected.offsetWidth - list.clientWidth;
      }
      $('frame-label').textContent = `Frame ${index + 1} / ${doc.frames.length}`;
      $('delete-frame').disabled = doc.frames.length <= 1;
      $('previous-frame').disabled = index === 0;
      $('next-frame').disabled = index === doc.frames.length - 1;
    }

    step(delta) {
      this.stop();
      this.model.select(this.model.index + delta);
    }

    toggle() {
      if (this.playing) { this.stop(); return; }
      this.model.commit();
      this.playing = true;
      this.started = performance.now();
      this.startIndex = this.model.index;
      this.lastStep = 0;
      $('play').textContent = '■ Stop';
      const tick = now => {
        if (!this.playing) return;
        const step = Math.floor((now - this.started) * this.model.doc.fps / 1000);
        if (step !== this.lastStep) {
          this.lastStep = step;
          this.model.select((this.startIndex + step) % this.model.doc.frames.length);
        }
        this.raf = requestAnimationFrame(tick);
      };
      this.raf = requestAnimationFrame(tick);
    }

    stop() {
      this.playing = false;
      cancelAnimationFrame(this.raf);
      $('play').textContent = '▶ Play';
    }
  }

  globalThis.Timeline = Timeline;
})();
