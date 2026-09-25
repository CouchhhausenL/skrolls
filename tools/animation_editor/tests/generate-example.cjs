'use strict';
const fs = require('node:fs');
const path = require('node:path');
require('../js/ska.js');
const doc = { width: 48, height: 22, background: '#171a21', fps: 8, frames: [] };
for (let f = 0; f < 12; f++) {
  const frame = SKA.blankFrame(doc.width, doc.height, doc.background);
  const put = (x, y, char, fg = '#a8e6b0') => {
    if (x >= 0 && y >= 0 && x < doc.width && y < doc.height) frame[y * doc.width + x] = { char, fg, bg: doc.background };
  };
  const write = (x, y, text, color) => Array.from(text).forEach((char, i) => put(x + i, y, char, color));
  write(3, 2, 'C O M E T   S T U D Y', '#a8e6b0');
  write(3, 19, '12 FRAMES  /  08 FPS  /  SKA', '#929cac');
  for (let i = 0; i < 26; i++) {
    const x = (i * 17 + 9) % 44 + 2, y = (i * 7 + 3) % 12 + 5;
    put(x, y, (i + f) % 9 === 0 ? '+' : '·', (i + f) % 9 === 0 ? '#f3cf83' : '#4a566d');
  }
  ['   .─────.   ', ' .´░▒▓▓▒░ `. ', '/░▒▓████▓▒░\\', '│▒▓██████▓▒│', '\\░▒▓████▓▒░/', ' `.░▒▓▓▒░.´ ', '   `─────´   '].forEach((row, y) => write(29, 8 + y, row, '#8eaff0'));
  const x = 8 + f, y = 12 - Math.round(f / 3);
  write(x - 5, y + 1, '··░▒', '#536a85');
  write(x - 4, y, '─░▒▓', '#a8e6b0');
  put(x, y, '◆', '#f3cf83');
  put(x, y - 1, '✦', '#ffffff');
  doc.frames.push(frame);
}
const folder = path.join(__dirname, '..', 'examples');
fs.mkdirSync(folder, { recursive: true });
fs.writeFileSync(path.join(folder, 'comet.ska'), SKA.encode(doc));
console.log('Generated examples/comet.ska');
