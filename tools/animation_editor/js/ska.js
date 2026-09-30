(() => {
  'use strict';
  const FRAME = '\x01', ROW = '\x02', PIXEL = '\x03', DATA = '\x04';
  const DEFAULT_FG = '#ffffff';
  const LIMITS = { width: 500, height: 300, frames: 2000, cells: 20000000, bytes: 320000000 };
  const HEX = /^#[0-9a-f]{6}$/i;
  const COLOR_DATA = /^[0-9a-f]{6}$/i;

  function validChar(char) {
    return typeof char === 'string' && Array.from(char).length === 1 &&
      !/[\u0000-\u001f\u007f-\u009f\u2028\u2029\ud800-\udfff]/u.test(char);
  }

  function validateHeader(doc) {
    if (!Number.isInteger(doc.width) || doc.width < 1 || doc.width > LIMITS.width ||
        !Number.isInteger(doc.height) || doc.height < 1 || doc.height > LIMITS.height) {
      throw new Error(`Canvas must be 1–${LIMITS.width} columns and 1–${LIMITS.height} rows.`);
    }
    if (!HEX.test(doc.background)) throw new Error('Canvas background must be a six-digit hex color.');
    if (!Number.isFinite(doc.fps) || doc.fps < 0.1 || doc.fps > 240) throw new Error('FPS must be between 0.1 and 240.');
  }

  function validateSize(width, height, count) {
    if (count < 1 || count > LIMITS.frames || width * height * count > LIMITS.cells) {
      throw new Error(`Limit: ${LIMITS.frames} frames and ${LIMITS.cells.toLocaleString()} total cells.`);
    }
  }

  function blankFrame(width, height, background) {
    return Array.from({ length: width * height }, () => ({ char: ' ', fg: DEFAULT_FG, bg: background }));
  }

  function decodePixel(payload, old, background) {
    if (!payload) return old;
    if (!payload.startsWith(DATA)) throw new Error('Pixel data must start with byte 04.');
    const fields = payload.slice(1).split(DATA);
    const next = { ...old };
    if (validChar(fields[0])) next.char = fields.shift();
    else if (fields.length === 3 && fields[0] === '') fields.shift();
    if (fields.length > 2) throw new Error('Too many color fields in a pixel.');
    for (let i = 0; i < fields.length; i++) {
      if (!fields[i]) continue;
      if (fields[i].toUpperCase() === 'NONE') {
        next[i === 0 ? 'fg' : 'bg'] = i === 0 ? DEFAULT_FG : background;
        continue;
      }
      if (!COLOR_DATA.test(fields[i])) throw new Error('Invalid pixel color; expected six hex digits.');
      next[i === 0 ? 'fg' : 'bg'] = '#' + fields[i].toLowerCase();
    }
    return next;
  }

  function decode(input) {
    const bytes = input instanceof Uint8Array ? input : new Uint8Array(input);
    if (bytes.byteLength > LIMITS.bytes) throw new Error('File exceeds the 32 MB editor limit.');
    const text = new TextDecoder('utf-8', { fatal: true }).decode(bytes);
    const firstFrame = text.indexOf(FRAME);
    if (firstFrame < 0) throw new Error('No SKA frame marker found.');
    const header = text.slice(0, firstFrame).split(',');
    if (header.length !== 4) throw new Error('Expected SKA header: width,height,#background,fps.');
    const doc = { width: Number(header[0]), height: Number(header[1]), background: header[2].toLowerCase(), fps: Number(header[3]), frames: [] };
    validateHeader(doc);
    const chunks = text.slice(firstFrame + 1).split(FRAME);
    validateSize(doc.width, doc.height, chunks.length);
    let previous = blankFrame(doc.width, doc.height, doc.background);
    chunks.forEach((chunk, index) => {
      try {
        const rows = chunk.split(ROW);
        if (rows.at(-1) === '') rows.pop();
        if (rows.length > doc.height) throw new Error('Too many rows.');
        const frame = previous.slice();
        rows.forEach((row, y) => {
          if (!row) return;
          if (!row.startsWith(PIXEL)) throw new Error('Row must start with byte 03.');
          const pixels = row.slice(1).split(PIXEL);
          if (pixels.length > doc.width) throw new Error('Too many pixels in a row.');
          pixels.forEach((payload, x) => {
            const cell = y * doc.width + x;
            frame[cell] = decodePixel(payload, previous[cell], doc.background);
          });
        });
        doc.frames.push(frame);
        previous = frame;
      } catch (error) {
        throw new Error(`Frame ${index + 1}: ${error.message}`);
      }
    });
    return doc;
  }

  function encodePixel(cell, old) {
    // Sanitize: fall back to the previous cell's known-valid values rather than crashing
    const char  = validChar(cell.char)  ? cell.char  : old.char;
    const fg    = HEX.test(cell.fg)     ? cell.fg    : old.fg;
    const bg    = HEX.test(cell.bg)     ? cell.bg    : old.bg;

    const diffChar = char !== old.char ? char : '';
    const diffFg   = fg.toLowerCase() !== old.fg.toLowerCase() ? fg.slice(1).toLowerCase() : '';
    const diffBg   = bg.toLowerCase() !== old.bg.toLowerCase() ? bg.slice(1).toLowerCase() : '';
    let pixel = PIXEL;
    if (diffChar) pixel += DATA + diffChar;
    if (diffFg || diffBg) pixel += DATA + diffFg;
    if (diffBg) pixel += DATA + diffBg;
    return pixel;
  }

  function encode(doc) {
    validateHeader(doc);
    validateSize(doc.width, doc.height, doc.frames.length);
    const chunks = [`${doc.width},${doc.height},${doc.background.toLowerCase()},${doc.fps}`];
    let previous = blankFrame(doc.width, doc.height, doc.background);
    for (const frame of doc.frames) {
      if (frame.length !== doc.width * doc.height) throw new Error('Frame size does not match the canvas.');
      chunks.push(FRAME);
      for (let y = 0; y < doc.height; y++) {
        const pixels = [];
        for (let x = 0; x < doc.width; x++) {
          const i = y * doc.width + x;
          pixels.push(encodePixel(frame[i], previous[i]));
        }
        while (pixels.at(-1) === PIXEL) pixels.pop();
        chunks.push(pixels.join(''), ROW);
      }
      previous = frame;
    }
    const bytes = new TextEncoder().encode(chunks.join(''));
    if (bytes.byteLength > LIMITS.bytes) throw new Error('Export exceeds the 32 MB editor limit.');
    return bytes;
  }

  globalThis.SKA = { DEFAULT_FG, LIMITS, HEX, validChar, validateHeader, validateSize, blankFrame, decode, encode };
})();
