(() => {
  'use strict';
  const $ = id => document.getElementById(id);
  const model = new EditorModel();
  const view = new CanvasView(model);
  let gestures;

  function report(message, error = false) {
    const el = $('status');
    if (el) {
      el.textContent = message;
      el.classList.toggle('error', error);
    }
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

  // --- TOOL PALETTE SELECTION ---
  const toolButtons = document.querySelectorAll('.tool-btn');
  toolButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      toolButtons.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      const tool = btn.dataset.tool;
      gestures.setTool(tool);

      // Contextual UI updates
      const isSelect = tool.startsWith('select');
      const selGroup = document.querySelector('.selection-actions');
      if (selGroup) selGroup.style.opacity = isSelect ? '1' : '0.85';

      report(`Tool: ${tool.replace('-', ' ').toUpperCase()}`);
    });
  });

  // --- TOOL OPTIONS WIRING ---
  const thicknessInput = $('tool-thickness');
  if (thicknessInput) {
    thicknessInput.oninput = event => {
      const val = event.target.value;
      gestures.setThickness(val);
      const display = $('tool-thickness-val');
      if (display) display.textContent = val;
    };
  }

  const opacityInput = $('tool-opacity');
  if (opacityInput) {
    opacityInput.oninput = event => {
      const pct = Number(event.target.value);
      const autoChar = gestures.setOpacity(pct / 100);
      const display = $('tool-opacity-val');
      if (display) display.textContent = `${pct}% (${autoChar})`;
    };
  }

  const falloffInput = $('tool-falloff');
  if (falloffInput) {
    falloffInput.onchange = event => {
      gestures.setFalloff(event.target.checked);
      report(`Soft Edges Falloff: ${event.target.checked ? 'Enabled' : 'Disabled'}`);
    };
  }

  // --- SELECTION ACTIONS ---
  $('btn-sel-copy')?.addEventListener('click', () => {
    if (model.selection) {
      model.copySelection();
      report('Selection copied to clipboard (also available as ASCII text).');
    } else {
      report('No active selection to copy.', true);
    }
  });

  $('btn-sel-cut')?.addEventListener('click', () => {
    if (model.selection) {
      model.cutSelection();
      report('Selection cut to clipboard.');
    } else {
      report('No active selection to cut.', true);
    }
  });

  $('btn-sel-paste')?.addEventListener('click', () => {
    const target = gestures.currentMouseCell || view.getVisibleCenterCell();
    const clip = model.getClipboard();
    let tx = target.x, ty = target.y;
    if (clip) {
      tx = Math.max(0, Math.min(model.doc.width - clip.width, target.x - Math.floor(clip.width / 2)));
      ty = Math.max(0, Math.min(model.doc.height - clip.height, target.y - Math.floor(clip.height / 2)));
    }
    const success = model.pasteClipboard(tx, ty);
    if (success) {
      report('Pasted from clipboard at cursor. Drag selection to position, press Enter to commit.');
    } else {
      report('Clipboard is empty. Select and copy something first.', true);
    }
  });

  $('btn-sel-del')?.addEventListener('click', () => {
    if (model.selection) {
      model.deleteSelection();
      report('Selection deleted.');
    } else {
      report('No active selection to delete.', true);
    }
  });

  $('btn-sel-commit')?.addEventListener('click', () => {
    if (model.selection) {
      model.commitSelection();
      report('Selection committed.');
    }
  });

  // --- PATTERN BRUSH PRESETS & INPUT ---
  const PATTERN_PRESETS = {
    diagonal: '/\\\n\\/',
    blocks: '░▒\n▓█',
    bricks: '+---+\n|   |\n+---+',
    crosses: '+ * +\n* + *',
    dots: '. : .\n: . :',
    waves: '~-~-\n-~-~'
  };

  const patternPresets = $('pattern-presets');
  const patternInput = $('pattern-input');

  if (patternPresets && patternInput) {
    patternPresets.onchange = event => {
      const val = event.target.value;
      if (PATTERN_PRESETS[val]) {
        patternInput.value = PATTERN_PRESETS[val];
        gestures.setPattern(PATTERN_PRESETS[val]);
      }
    };

    patternInput.oninput = event => {
      gestures.setPattern(event.target.value);
      if (patternPresets.value !== 'custom') {
        patternPresets.value = 'custom';
      }
    };
  }

  // --- REFERENCE IMAGE TRACING WIRING ---
  const toggleRefSec = $('toggle-ref-section');
  const secRefImage = $('sec-ref-image');
  const refFoldIcon = $('ref-fold-icon');

  if (toggleRefSec && secRefImage) {
    toggleRefSec.onclick = () => {
      const isCollapsed = secRefImage.classList.toggle('collapsed');
      if (refFoldIcon) refFoldIcon.textContent = isCollapsed ? '▶' : '▼';
    };
  }

  const btnLoadRef = $('btn-load-ref');
  const refFileInput = $('ref-image-input');
  const refOpacity = $('ref-opacity');
  const refOpacityVal = $('ref-opacity-val');
  const refFit = $('ref-fit');
  const refVisible = $('ref-visible');
  const btnClearRef = $('btn-clear-ref');
  const refBadge = $('ref-status-badge');

  if (btnLoadRef && refFileInput) {
    btnLoadRef.onclick = () => refFileInput.click();

    refFileInput.onchange = event => {
      const file = event.target.files[0];
      event.target.value = '';
      if (!file) return;

      const reader = new FileReader();
      reader.onload = ev => {
        const img = new Image();
        img.onload = () => {
          const op = (Number(refOpacity?.value) || 40) / 100;
          const fit = refFit?.value || 'contain';
          view.setReferenceImage(img, op, fit, true);
          if (refBadge) refBadge.textContent = `${img.naturalWidth}×${img.naturalHeight}`;
          if (refVisible) refVisible.checked = true;
          if (secRefImage && secRefImage.classList.contains('collapsed')) {
            secRefImage.classList.remove('collapsed');
            if (refFoldIcon) refFoldIcon.textContent = '▼';
          }
          report(`Loaded reference image (${img.naturalWidth}×${img.naturalHeight}px). Ready for tracing.`);
        };
        img.src = ev.target.result;
      };
      reader.readAsDataURL(file);
    };
  }

  if (refOpacity) {
    refOpacity.oninput = event => {
      const pct = Number(event.target.value);
      view.setReferenceOpacity(pct / 100);
      if (refOpacityVal) refOpacityVal.textContent = `${pct}%`;
    };
  }

  if (refFit) {
    refFit.onchange = event => {
      view.setReferenceFit(event.target.value);
    };
  }

  if (refVisible) {
    refVisible.onchange = event => {
      view.setReferenceVisible(event.target.checked);
    };
  }

  if (btnClearRef) {
    btnClearRef.onclick = () => {
      view.setReferenceImage(null);
      if (refBadge) refBadge.textContent = 'None';
      if (secRefImage) {
        secRefImage.classList.add('collapsed');
        if (refFoldIcon) refFoldIcon.textContent = '▶';
      }
      report('Reference image removed.');
    };
  }

  // --- DOCUMENT SYNCHRONIZATION ---
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
    document.title = `${model.dirty ? '• ' : ''}${name} — JavE SKA editor`;
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
    report('New animation. Select a tool and create.');
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
      report(`Exported ${name} · ${bytes.length.toLocaleString()} bytes.`);
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

  // Color bulk operations (in English)
  $('apply-colors-all')?.addEventListener('click', () => {
    const updated = model.applyColorsToAllFrames();
    report(`${updated} frame(s) updated with new colors.`);
  });

  $('apply-colors-next')?.addEventListener('click', () => {
    const updated = model.applyColorsToNextFrames();
    report(`${updated} subsequent frame(s) updated.`);
  });

  $('replace-color')?.addEventListener('click', () => {
    const char = model.brush.char || '@';
    const updated = model.replaceColor(char, model.brush.fg, model.brush.bg);
    report(`${updated} occurrence(s) of "${char}" recolored.`);
  });

  window.editorApp = { model, view, gestures, timeline };
})();
