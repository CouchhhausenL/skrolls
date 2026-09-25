'use strict';
const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
require('../js/ska.js');
require('../js/model.js');
require('../js/gestures.js');
const { encode, decode, blankFrame } = SKA;
const pixel = (char = ' ', fg = '#ffffff', bg = '#171a21') => ({ char, fg, bg });
const doc = frames => ({ width: 1, height: 1, background: '#171a21', fps: 12, frames });
const text = bytes => new TextDecoder().decode(bytes);
const bytes = value => new TextEncoder().encode(value);
const header = '1,1,#171a21,12\x01';

for (const [label, cell, expected] of [
  ['unchanged', pixel(), ''],
  ['character only', pixel('X'), '\x03\x04X'],
  ['foreground only', pixel(' ', '#aabbcc'), '\x03\x04aabbcc'],
  ['background only', pixel(' ', '#ffffff', '#112233'), '\x03\x04\x04112233'],
  ['character and foreground', pixel('X', '#aabbcc'), '\x03\x04X\x04aabbcc'],
  ['character and background', pixel('X', '#ffffff', '#112233'), '\x03\x04X\x04\x04112233'],
  ['foreground and background', pixel(' ', '#aabbcc', '#112233'), '\x03\x04aabbcc\x04112233'],
  ['all components', pixel('X', '#aabbcc', '#112233'), '\x03\x04X\x04aabbcc\x04112233']
]) {
  test(`converter-compatible bytes: ${label}`, () => {
    const source = doc([[cell]]);
    assert.equal(text(encode(source)), header + expected + '\x02');
    assert.deepEqual(decode(encode(source)), source);
  });
}

test('all component changes against a non-default previous frame', () => {
  const initial = pixel('Q', '#102030', '#405060');
  for (let mask = 0; mask < 8; mask++) {
    const next = pixel(mask & 1 ? ' ' : 'Q', mask & 2 ? '#ffffff' : '#102030', mask & 4 ? '#171a21' : '#405060');
    const source = doc([[initial], [next]]);
    assert.deepEqual(decode(encode(source)), source);
  }
});

test('default foreground/background resets are explicit deltas', () => {
  const source = doc([[pixel('X', '#102030', '#405060')], [pixel()]]);
  assert.ok(text(encode(source)).endsWith('\x01\x03\x04 \x04ffffff\x04171a21\x02'));
});

test('Unicode, spaces, literal delimiters, and six hex-looking characters', () => {
  const chars = ['█', '🙂', '|', ',', ' ', 'a', '0', 'é', '界'];
  const source = { ...doc([]), width: chars.length, frames: [chars.map(char => pixel(char))] };
  assert.deepEqual(decode(encode(source)), source);
});

test('leading unchanged pixels preserved; trailing unchanged pixels omitted', () => {
  const source = { ...doc([]), width: 4, height: 2, frames: [blankFrame(4, 2, '#171a21')] };
  source.frames[0][2] = pixel('!');
  assert.equal(text(encode(source)), '4,2,#171a21,12\x01\x03\x03\x03\x04!\x02\x02');
  assert.deepEqual(decode(encode(source)), source);
});

test('empty frames retain their duration and previous visual state', () => {
  const source = doc([[pixel('x')], [pixel('x')], [pixel('x')]]);
  assert.deepEqual(decode(encode(source)), source);
  assert.equal(decode(bytes(header + '\x02\x01\x02')).frames.length, 2);
});

test('deterministic randomized animation round-trip', () => {
  let seed = 421;
  const random = n => { seed = (seed * 1664525 + 1013904223) >>> 0; return seed % n; };
  const colors = ['#ffffff', '#171a21', '#ff0099', '#000000', '#aabbcc'];
  const chars = [' ', 'a', '█', '│', '🙂', '|'];
  const source = { width: 6, height: 4, background: '#171a21', fps: 23.976, frames: [] };
  for (let f = 0; f < 40; f++) {
    source.frames.push(Array.from({ length: 24 }, () => pixel(chars[random(chars.length)], colors[random(colors.length)], colors[random(colors.length)])));
  }
  assert.deepEqual(decode(encode(source)), source);
  assert.deepEqual(encode(decode(encode(source))), encode(source));
});

for (const [label, content] of [
  ['no frames', '1,1,#171a21,12'],
  ['zero size', '0,1,#171a21,12\x01\x02'],
  ['fractional size', '1.5,1,#171a21,12\x01\x02'],
  ['bad background', '1,1,red,12\x01\x02'],
  ['bad fps', '1,1,#171a21,0\x01\x02'],
  ['too many rows', header + '\x02\x02'],
  ['too many columns', header + '\x03\x03\x02'],
  ['missing pixel marker', header + 'X\x02'],
  ['missing data marker', header + '\x03X\x02'],
  ['invalid color', header + '\x03\x04zzzzzz\x02'],
  ['too many fields', header + '\x03\x04X\x04ffffff\x04ffffff\x04ffffff\x02']
]) test(`reject malformed file: ${label}`, () => assert.throws(() => decode(bytes(content))));

test('invalid UTF-8 and control characters rejected', () => {
  assert.throws(() => decode(new Uint8Array([255])));
  for (const char of ['\x01', '\x02', '\x03', '\x04', '\n', '\t', '\r', '\u2028', '\ud800', '', 'ab']) assert.equal(SKA.validChar(char), false);
  assert.equal(SKA.validChar('🙂'), true);
});

test('partial pencil, None, eraser, and picker', () => {
  const model = new EditorModel();
  model.brush = { char: null, fg: '#ff0000', bg: null };
  model.paint([0]); model.commit();
  assert.deepEqual(model.frame[0], pixel(' ', '#ff0000'));
  model.brush = { char: null, fg: null, bg: null };
  const previous = model.doc;
  model.fill();
  assert.equal(model.doc, previous);
  model.brush.char = '@';
  model.pick(0, true);
  assert.equal(model.brush.char, '@');
  model.pick(0, false);
  assert.equal(model.brush.char, ' ');
  model.paint([0], true); model.commit();
  assert.deepEqual(model.frame[0], pixel());
});

test('one stroke is one undo transaction; redo and saved identity', () => {
  const model = new EditorModel();
  model.paint([0]); model.paint([1]); model.commit();
  assert.equal(model.undoStack.length, 1);
  assert.equal(model.dirty, true);
  model.markSaved();
  model.travel();
  assert.equal(model.frame[0].char, ' ');
  assert.equal(model.dirty, true);
  model.travel(true);
  assert.equal(model.frame[0].char, '█');
  assert.equal(model.frame[1].char, '█');
  assert.equal(model.dirty, false);
});

test('duplicated and subsequent frames do not change when another is edited', () => {
  const model = new EditorModel();
  model.paint([0]); model.commit();
  model.insert(true);
  model.select(0);
  model.brush.char = 'X';
  model.paint([0]); model.commit();
  assert.equal(model.frame[0].char, 'X');
  assert.equal(model.doc.frames[1][0].char, '█');
  assert.deepEqual(decode(encode(model.doc)), model.doc);
});

test('frame insert/delete and resize are undoable', () => {
  const model = new EditorModel();
  model.insert();
  assert.equal(model.index, 1);
  model.remove();
  assert.equal(model.doc.frames.length, 1);
  model.travel();
  assert.equal(model.doc.frames.length, 2);
  model.resize(3, 2);
  assert.equal(model.frame.length, 6);
  model.travel();
  assert.equal(model.doc.width, 40);
  assert.equal(model.frame.length, 800);
});

test('changing default background preserves custom colors; undo restores all', () => {
  const model = new EditorModel();
  model.brush = { char: null, fg: null, bg: '#123456' };
  model.paint([0]); model.commit();
  model.setBackground('#000000');
  assert.equal(model.frame[0].bg, '#123456');
  assert.equal(model.frame[1].bg, '#000000');
  model.travel();
  assert.equal(model.frame[1].bg, '#171a21');
  assert.throws(() => model.resize(0, 10));
  assert.equal(model.doc.width, 40);
});

test('new edit discards redo; loading resets history', () => {
  const model = new EditorModel();
  model.paint([0]); model.commit(); model.travel();
  model.paint([1]); model.commit();
  assert.equal(model.redoStack.length, 0);
  model.load(model.createDocument());
  assert.equal(model.undoStack.length, 0);
  assert.equal(model.dirty, false);
});

test('line interpolation covers endpoints in both directions', () => {
  assert.deepEqual(lineCells({ x: 0, y: 0 }, { x: 3, y: 0 }, 10), [0, 1, 2, 3]);
  assert.deepEqual(lineCells({ x: 3, y: 3 }, { x: 0, y: 0 }, 10), [33, 22, 11, 0]);
});

for (const path of process.argv.slice(2)) {
  test(`existing converter asset round-trip: ${path}`, () => {
    const source = decode(fs.readFileSync(path));
    assert.deepEqual(decode(encode(source)), source);
  });
}


test('legacy NONE resets resolve to foreground and canvas defaults', () => {
  const source = '1,1,#171a21,12\x01\x03\x04X\x04102030\x04405060\x02\x01\x03\x04NONE\x04NONE\x02';
  const decoded = decode(bytes(source));
  assert.deepEqual(decoded.frames[1][0], pixel('X'));
  assert.equal(text(encode(decoded)).includes('NONE'), false);
});

test('legacy explicit empty character slot keeps the previous character', () => {
  const source = '1,1,#171a21,12\x01\x03\x04X\x02\x01\x03\x04\x04aabbcc\x04112233\x02';
  const decoded = decode(bytes(source));
  assert.deepEqual(decoded.frames[1][0], pixel('X', '#aabbcc', '#112233'));
  assert.deepEqual(decode(encode(decoded)), decoded);
});
