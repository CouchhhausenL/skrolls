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
      this.draggedIndex = null;

      $('previous-frame').onclick = () => this.step(-1);
      $('next-frame').onclick = () => this.step(1);
      $('play').onclick = () => this.toggle();

      const moveLeft = $('move-frame-left');
      if (moveLeft) moveLeft.onclick = () => { this.stop(); run(() => model.moveFrame(-1)); };
      const moveRight = $('move-frame-right');
      if (moveRight) moveRight.onclick = () => { this.stop(); run(() => model.moveFrame(1)); };

      for (const [id, action] of [
        ['add-frame', () => model.insert()],
        ['duplicate-frame', () => model.insert(true)],
        ['delete-frame', () => model.remove()]
      ]) {
        const el = $(id);
        if (el) el.onclick = () => { this.stop(); run(action); };
      }

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
        button.draggable = true;

        const canvas = document.createElement('canvas');
        canvas.width = 144; canvas.height = 98;
        canvas.setAttribute('aria-hidden', 'true');

        const label = document.createElement('span');
        label.textContent = String(i + 1).padStart(3, '0');

        button.append(canvas, label);

        // Click to select
        button.onclick = (e) => {
          if (this.draggedIndex !== null) return;
          this.stop();
          this.model.select(this.cards.findIndex(c => c.button === button));
        };

        // HTML5 Drag and Drop for frame reordering
        button.addEventListener('dragstart', event => {
          if (this.playing) { event.preventDefault(); return; }
          const curIdx = this.cards.findIndex(c => c.button === button);
          this.draggedIndex = curIdx;
          event.dataTransfer.setData('text/plain', String(curIdx));
          event.dataTransfer.effectAllowed = 'move';
          button.classList.add('dragging');
        });

        button.addEventListener('dragover', event => {
          event.preventDefault();
          event.dataTransfer.dropEffect = 'move';
          button.classList.add('drag-over');
        });

        button.addEventListener('dragleave', () => {
          button.classList.remove('drag-over');
        });

        button.addEventListener('drop', event => {
          event.preventDefault();
          button.classList.remove('drag-over');
          const fromIdx = parseInt(event.dataTransfer.getData('text/plain'), 10);
          const targetIdx = this.cards.findIndex(c => c.button === button);
          this.draggedIndex = null;
          if (!isNaN(fromIdx) && targetIdx >= 0 && fromIdx !== targetIdx) {
            this.stop();
            this.run(() => this.model.reorderFrame(fromIdx, targetIdx));
          }
        });

        button.addEventListener('dragend', () => {
          this.draggedIndex = null;
          button.classList.remove('dragging');
          for (const card of this.cards) card.button.classList.remove('drag-over');
        });

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
        card.button.querySelector('span').textContent = String(i + 1).padStart(3, '0');
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

      const moveLeft = $('move-frame-left');
      if (moveLeft) moveLeft.disabled = index === 0;
      const moveRight = $('move-frame-right');
      if (moveRight) moveRight.disabled = index === doc.frames.length - 1;
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
