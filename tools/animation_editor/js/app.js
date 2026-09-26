(() => {
  'use strict';
  const $ = id => document.getElementById(id);
  const model = new EditorModel();
  const view = new CanvasView(model);
  let gestures;

  function report(message, error = false) {
    $('status').textContent = message;
    $('status').classList.toggle('error', error);
  }

  function run(action) {
    try { action(); }
    catch (error) { report(error.message, true); syncDocument(); }
  }

  const timeline = new Timeline(model, run);
  new ColorControls(model, report);
  new SymbolControls(model, report);
  gestures = new Gestures(model, view, timeline, run);

  function edit(action) {
    gestures.finish();
    timeline.stop();
    run(action);
  }

  function syncDocument() {
    const doc = model.doc;
    $('canvas-width').value = doc.width;
    $('canvas-height').value = doc.height;
    $('canvas-background').value = doc.background;
    $('fps').value = doc.fps;
    syncHistory();
  }

  function syncHistory() {
    $('undo').disabled = model.undoStack.length === 0;
    $('redo').disabled = model.redoStack.length === 0;
    $('dirty-dot').hidden = !model.dirty;
    const name = $('file-name').value.trim() || 'untitled';
    $('document-state').textContent = `${name.replace(/\.ska$/i, '')}.ska${model.dirty ? ' · Edited' : ''}`;
    document.title = `${model.dirty ? '• ' : ''}${name} — SKA editor`;
  }

  model.addEventListener('change', event => {
    if (['load', 'history', 'commit'].includes(event.detail)) syncDocument();
    else if (event.detail !== 'brush') syncHistory();
  });
  $('file-name').oninput = syncHistory;
  $('undo').onclick = () => edit(() => model.travel());
  $('redo').onclick = () => edit(() => model.travel(true));
  $('fill-frame').onclick = () => edit(() => { model.fill(); report('Frame filled. None settings were left untouched.'); });
  $('resize-canvas').onclick = () => {
    const width = Number($('canvas-width').value), height = Number($('canvas-height').value);
    edit(() => { model.resize(width, height); report('Canvas resized. Cropped cells can be restored with Undo.'); });
  };
  $('canvas-background').onchange = event => {
    const value = event.target.value;
    edit(() => { model.setBackground(value); report('Canvas background updated, including cells matching the previous default.'); });
  };
  $('fps').onchange = event => {
    const fps = Number(event.target.value);
    edit(() => model.setFps(fps));
  };
  $('grid-toggle').onchange = event => { view.grid = event.target.checked; view.schedule(); };
  $('zoom-out').onclick = () => view.setZoom(view.zoom / 1.2);
  $('zoom-in').onclick = () => view.setZoom(view.zoom * 1.2);
  $('zoom-reset').onclick = () => view.setZoom(1);
  $('zoom-fit').onclick = () => view.fit();

  function canReplace() {
    return !model.dirty || window.confirm('This animation has unsaved edits. Discard them and continue?');
  }

  $('new-file').onclick = () => {
    gestures.finish();
    timeline.stop();
    if (!canReplace()) return;
    model.load(model.createDocument());
    $('file-name').value = 'untitled';
    syncHistory();
    view.fit();
    report('New animation. Left mouse draws, right erases, middle picks.');
  };

  $('open-file').onclick = () => { gestures.finish(); timeline.stop(); $('file-input').click(); };
  $('file-input').onchange = async event => {
    const file = event.target.files[0];
    event.target.value = '';
    if (!file) return;
    try {
      if (!/\.ska$/i.test(file.name)) throw new Error('Choose a .ska animation file.');
      if (file.size > SKA.LIMITS.bytes) throw new Error('File exceeds the 32 MB editor limit.');
      const doc = SKA.decode(await file.arrayBuffer());
      gestures.finish();
      timeline.stop();
      if (!canReplace()) return;
      model.load(doc);
      $('file-name').value = file.name.replace(/\.ska$/i, '');
      syncHistory();
      view.fit();
      report(`Opened ${file.name} · ${doc.frames.length} frames.`);
    } catch (error) { report(`Could not open file: ${error.message}`, true); }
  };

  $('save-file').onclick = () => {
    gestures.finish();
    run(() => {
      const bytes = SKA.encode(model.doc);
      const name = ($('file-name').value.trim().replace(/\.ska$/i, '').replace(/[<>:"/\\|?*\u0000-\u001f]/g, '_').replace(/[. ]+$/, '') || 'untitled') + '.ska';
      const url = URL.createObjectURL(new Blob([bytes], { type: 'application/octet-stream' }));
      const link = document.createElement('a');
      link.href = url;
      link.download = name;
      document.body.append(link);
      link.click();
      link.remove();
      setTimeout(() => URL.revokeObjectURL(url), 60000);
      model.markSaved();
      report(`Exported ${name} · ${bytes.length.toLocaleString()} bytes. Check your browser downloads.`);
    });
  };

  window.addEventListener('beforeunload', event => {
    if (!model.dirty) return;
    event.preventDefault();
    event.returnValue = '';
  });
  window.addEventListener('resize', () => view.schedule());
  syncDocument();
  requestAnimationFrame(() => view.fit());

  document.querySelector('#apply-colors-all')?.addEventListener('click', () => {
  const updated = model.applyColorsToAllFrames();
  showStatus(`${updated} Frame(s) mit neuen Farben aktualisiert.`);
});

document.querySelector('#apply-colors-next')?.addEventListener('click', () => {
  console.log("HEy")
  const updated = model.applyColorsToNextFrames();
  showStatus(`${updated} nachfolgende(r) Frame(s) aktualisiert.`);
});



})();
