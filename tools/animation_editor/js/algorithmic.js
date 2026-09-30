(() => {
  'use strict';

  // JavE 5.0 Structural ASCII-Art Engine
  // Reverse-engineered from Markus Gebhard's JavE config/convert.txt
  // Uses 3x4 subpixel rasterization and 192 exact JavE conversion rules

  const JAVE_CONVERT_RULES = [
    [1, 4094, " "],
    [2, 4093, " "],
    [4, 4091, " "],
    [8, 4087, " "],
    [16, 4079, " "],
    [32, 4063, " "],
    [64, 4031, " "],
    [128, 3967, " "],
    [256, 3839, " "],
    [512, 3583, " "],
    [1024, 3071, " "],
    [2048, 2047, " "],
    [5, 3544, "'"],
    [6, 2040, "'"],
    [3, 3568, "'"],
    [40, 3527, "-"],
    [40, 1991, "-"],
    [48, 4035, "-"],
    [24, 4039, "-"],
    [320, 3646, "."],
    [320, 3643, "."],
    [384, 575, "."],
    [192, 3639, "."],
    [2560, 507, "_"],
    [3072, 507, "_"],
    [1536, 510, "_"],
    [1280, 766, ","],
    [640, 127, ","],
    [1792, 254, ","],
    [1664, 383, "."],
    [1600, 443, "."],
    [3200, 383, "."],
    [3136, 443, "."],
    [1026, 2829, "|"],
    [1026, 2661, "|"],
    [513, 3366, "|"],
    [2052, 1611, "|"],
    [146, 365, "|"],
    [1168, 2920, "|"],
    [292, 731, "|"],
    [2336, 1753, "|"],
    [73, 2486, "|"],
    [584, 3508, "|"],
    [2305, 576, "\\"],
    [580, 2304, "/"],
    [2305, 36, "\\"],
    [580, 9, "/"],
    [660, 2336, "/"],
    [660, 9, "/"],
    [2193, 36, "\\"],
    [672, 2304, "/"],
    [2184, 576, "\\"],
    [84, 4000, "'"],
    [273, 3784, "`"],
    [2565, 56, ":"],
    [2565, 448, ":"],
    [2561, 452, ":"],
    [2564, 449, ":"],
    [3078, 456, ":"],
    [3075, 456, ":"],
    [3078, 120, ":"],
    [1539, 480, ":"],
    [1539, 312, ":"],
    [1542, 480, ":"],
    [2243, 568, ":"],
    [643, 2104, ":"],
    [1556, 449, ":"],
    [1553, 452, ":"],
    [2245, 568, ":"],
    [645, 2104, ":"],
    [2580, 449, ":"],
    [2577, 452, ":"],
    [2246, 568, ":"],
    [646, 2104, ":"],
    [3092, 449, ":"],
    [3089, 452, ":"],
    [2243, 568, ":"],
    [899, 2104, ":"],
    [2245, 568, ":"],
    [901, 2104, ":"],
    [2246, 568, ":"],
    [902, 2104, ":"],
    [21, 4032, "\""],
    [42, 4037, "^"],
    [42, 4032, "\""],
    [1609, 438, "L"],
    [3145, 310, "L"],
    [3364, 219, "J"],
    [1828, 91, "J"],
    [1389, 18, "V"],
    [3150, 432, "["],
    [3220, 873, "["],
    [2198, 873, "["],
    [1827, 216, "["],
    [1681, 2412, "["],
    [659, 2412, "["],
    [2196, 1867, "("],
    [657, 3430, ")"],
    [2206, 1601, "<"],
    [3284, 523, "<"],
    [691, 3332, ">"],
    [1937, 2086, ">"],
    [28, 4067, "-"],
    [49, 4046, "-"],
    [14, 2033, "'"],
    [35, 3548, "`"],
    [280, 3815, "-"],
    [112, 3983, "-"],
    [392, 3703, "."],
    [224, 3871, ","],
    [896, 3199, ","],
    [384, 3711, "."],
    [192, 1855, "."],
    [1280, 2303, ","],
    [1088, 959, "."],
    [1386, 144, "O"],
    [1194, 2565, "+"],
    [1362, 2565, "+"],
    [632, 3332, "+"],
    [9, 2038, "'"],
    [2305, 576, "\\"],
    [274, 1728, "\\"],
    [594, 3456, "/"],
    [448, 63, "."],
    [56, 3591, "-"],
    [2304, 1759, "."],
    [34, 3545, "`"],
    [10, 2036, "'"],
    [273, 1604, "\\"],
    [84, 3329, "/"],
    [50, 4045, "`"],
    [1592, 455, "="],
    [3128, 455, "="],
    [199, 3640, "="],
    [391, 3640, "="],
    [451, 3640, "="],
    [454, 3640, "="],
    [1188, 585, "|"],
    [2322, 585, "|"],
    [594, 2340, "|"],
    [1161, 2340, "|"],
    [1096, 550, "\\"],
    [1312, 2059, "/"],
    [290, 1604, "\\"],
    [74, 3329, "/"],
    [1184, 91, "/"],
    [624, 11, "/"],
    [393, 1600, "\\"],
    [228, 3328, "/"],
    [672, 27, "/"],
    [1160, 310, "\\"],
    [2184, 310, "\\"],
    [2328, 38, "\\"],
    [576, 3519, "."],
    [36, 4059, "'"],
    [1098, 945, "("],
    [1314, 2268, ")"],
    [82, 4004, "'"],
    [1408, 639, ","],
    [457, 3622, "\\"],
    [26, 4068, "'"],
    [1200, 591, ","],
    [228, 3584, "/"],
    [393, 3584, "\\"],
    [17, 4068, "`"],
    [1216, 2367, "."],
    [30, 4064, "'"],
    [51, 4040, "`"],
    [704, 3391, ","],
    [1, 1022, " "],
    [2, 509, " "],
    [4, 2555, " "],
    [512, 3577, " "],
    [1024, 3064, " "],
    [2048, 2044, " "],
    [72, 1971, " "],
    [288, 3294, " "],
    [2532, 1563, "J"],
    [3424, 671, "V"],
    [3904, 191, "."],
    [458, 3637, "L"],
    [1248, 2847, ","],
    [1161, 2930, "|"],
    [969, 3126, "L"],
    [1816, 2151, ">"],
    [904, 2151, ">"],
    [3184, 783, "<"],
    [2272, 783, "<"],
    [402, 3693, "`"],
    [75, 4020, "'"],
    [210, 3885, "'"],
    [3735, 360, "I"],
  ];

  function matchPattern(pattern) {
    if (pattern === 0) return ' ';
    for (let i = 0; i < JAVE_CONVERT_RULES.length; i++) {
      const r = JAVE_CONVERT_RULES[i];
      if ((pattern & r[0]) === r[0] && (pattern & r[1]) === 0) {
        return r[2];
      }
    }

    // JavE Fallback Heuristic
    const pixels = [];
    for (let row = 0; row < 4; row++) {
      for (let col = 0; col < 3; col++) {
        if (pattern & (1 << (row * 3 + col))) {
          pixels.push({ c: col, r: row });
        }
      }
    }

    // Filter isolated single pixel noise (same as JavE rules 0-11)
    if (pixels.length <= 1) return ' ';

    let minC = pixels[0].c, maxC = pixels[0].c;
    let minR = pixels[0].r, maxR = pixels[0].r;
    let sumR = 0;
    for (let i = 0; i < pixels.length; i++) {
      const p = pixels[i];
      if (p.c < minC) minC = p.c;
      if (p.c > maxC) maxC = p.c;
      if (p.r < minR) minR = p.r;
      if (p.r > maxR) maxR = p.r;
      sumR += p.r;
    }

    const dc = maxC - minC, dr = maxR - minR;
    if (dr === 0 || dc >= dr * 2) {
      const avgR = sumR / pixels.length;
      if (avgR < 1) return "'";
      if (avgR < 2) return '-';
      if (avgR < 3) return '.';
      return '_';
    }

    if (dc === 0 || dr >= dc * 2) return '|';

    const start = pixels[0], end = pixels[pixels.length - 1];
    return (end.c - start.c) * (end.r - start.r) < 0 ? '/' : '\\';
  }

  class SubpixelRaster {
    constructor() {
      this.cells = new Map(); // 'cx,cy' -> 12-bit integer bitmask
    }

    setPixel(sx, sy) {
      const cx = Math.floor(sx / 3);
      const cy = Math.floor(sy / 4);
      const lx = ((sx % 3) + 3) % 3;
      const ly = ((sy % 4) + 4) % 4;
      const bit = 1 << (ly * 3 + lx);
      const key = `${cx},${cy}`;
      this.cells.set(key, (this.cells.get(key) || 0) | bit);
    }

    drawLine(x1, y1, x2, y2) {
      const dx = Math.abs(x2 - x1), dy = -Math.abs(y2 - y1);
      const sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1;
      let err = dx + dy;
      let x = x1, y = y1;
      while (true) {
        this.setPixel(x, y);
        if (x === x2 && y === y2) break;
        const e2 = 2 * err;
        if (e2 >= dy) { err += dy; x += sx; }
        if (e2 <= dx) { err += dx; y += sy; }
      }
    }

    toAsciiCells() {
      const result = [];
      for (const [key, pattern] of this.cells.entries()) {
        const char = matchPattern(pattern);
        if (char !== ' ') {
          const parts = key.split(',');
          result.push({
            x: Number(parts[0]),
            y: Number(parts[1]),
            char
          });
        }
      }
      return result;
    }
  }

  class AlgorithmicAscii {
    constructor() {
      this.mode = 'ascii';
    }

    setMode(mode) {
      this.mode = 'ascii';
    }

    createSubpixelRaster() {
      return new SubpixelRaster();
    }

    generateLine(x1, y1, x2, y2) {
      const raster = new SubpixelRaster();
      const sx1 = x1 * 3 + 1, sy1 = y1 * 4 + 1;
      const sx2 = x2 * 3 + 1, sy2 = y2 * 4 + 1;
      raster.drawLine(sx1, sy1, sx2, sy2);
      return raster.toAsciiCells();
    }

    generateRectangle(x1, y1, x2, y2) {
      const raster = new SubpixelRaster();
      const minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
      const minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
      const sx1 = minX * 3 + 1, sy1 = minY * 4 + 1;
      const sx2 = maxX * 3 + 1, sy2 = maxY * 4 + 2;

      raster.drawLine(sx1, sy1, sx2, sy1);
      raster.drawLine(sx2, sy1, sx2, sy2);
      raster.drawLine(sx2, sy2, sx1, sy2);
      raster.drawLine(sx1, sy2, sx1, sy1);

      return raster.toAsciiCells();
    }

    generateEllipse(x1, y1, x2, y2) {
      const raster = new SubpixelRaster();
      const minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
      const minY = Math.min(y1, y2), maxY = Math.max(y1, y2);

      const scx = (minX + maxX) * 1.5 + 1;
      const scy = (minY + maxY) * 2 + 1.5;
      const srx = Math.max(1, (maxX - minX) * 1.5);
      const sry = Math.max(1, (maxY - minY) * 2);

      const steps = Math.max(36, Math.ceil(Math.PI * 2 * Math.max(srx, sry) * 2.2));
      for (let i = 0; i < steps; i++) {
        const theta = (i / steps) * Math.PI * 2;
        const sx = Math.round(scx + srx * Math.cos(theta));
        const sy = Math.round(scy + sry * Math.sin(theta));
        raster.setPixel(sx, sy);
      }

      return raster.toAsciiCells();
    }

    generateFreehand(points) {
      if (!points || points.length === 0) return [];
      const raster = new SubpixelRaster();

      if (points.length === 1) {
        const p = points[0];
        const sx = p.sx !== undefined ? p.sx : (p.x * 3 + 1);
        const sy = p.sy !== undefined ? p.sy : (p.y * 4 + 1);
        raster.drawLine(sx - 1, sy, sx + 1, sy);
        return raster.toAsciiCells();
      }

      for (let i = 0; i < points.length - 1; i++) {
        const p1 = points[i];
        const p2 = points[i + 1];
        const sx1 = p1.sx !== undefined ? p1.sx : (p1.x * 3 + 1);
        const sy1 = p1.sy !== undefined ? p1.sy : (p1.y * 4 + 1);
        const sx2 = p2.sx !== undefined ? p2.sx : (p2.x * 3 + 1);
        const sy2 = p2.sy !== undefined ? p2.sy : (p2.y * 4 + 1);
        raster.drawLine(sx1, sy1, sx2, sy2);
      }

      return raster.toAsciiCells();
    }

    generateBezier(controlPoints) {
      if (!controlPoints || controlPoints.length < 2) return [];
      const isCubic = controlPoints.length >= 4;
      const p0 = controlPoints[0];
      const p1 = controlPoints[1];
      const p2 = isCubic ? controlPoints[2] : null;
      const pEnd = isCubic ? controlPoints[3] : controlPoints[2];

      const s0 = { x: p0.x * 3 + 1, y: p0.y * 4 + 1 };
      const s1 = { x: p1.x * 3 + 1, y: p1.y * 4 + 1 };
      const s2 = isCubic ? { x: p2.x * 3 + 1, y: p2.y * 4 + 1 } : null;
      const sEnd = { x: pEnd.x * 3 + 1, y: pEnd.y * 4 + 1 };

      const estLength = Math.hypot(sEnd.x - s0.x, sEnd.y - s0.y) * 2;
      const steps = Math.max(30, Math.ceil(estLength * 3));
      const raster = new SubpixelRaster();

      let prevX = s0.x, prevY = s0.y;
      for (let i = 1; i <= steps; i++) {
        const t = i / steps;
        let curX, curY;
        if (isCubic) {
          const mt = 1 - t;
          curX = Math.round(mt * mt * mt * s0.x + 3 * mt * mt * t * s1.x + 3 * mt * t * t * s2.x + t * t * t * sEnd.x);
          curY = Math.round(mt * mt * mt * s0.y + 3 * mt * mt * t * s1.y + 3 * mt * t * t * s2.y + t * t * t * sEnd.y);
        } else {
          const mt = 1 - t;
          curX = Math.round(mt * mt * s0.x + 2 * mt * t * s1.x + t * t * sEnd.x);
          curY = Math.round(mt * mt * s0.y + 2 * mt * t * s1.y + t * t * sEnd.y);
        }
        raster.drawLine(prevX, prevY, curX, curY);
        prevX = curX;
        prevY = curY;
      }

      return raster.toAsciiCells();
    }

    generateGenericBezierPoints(controlPoints) {
      if (!controlPoints || controlPoints.length < 2) return [];
      const isCubic = controlPoints.length >= 4;
      const p0 = controlPoints[0];
      const p1 = controlPoints[1];
      const p2 = isCubic ? controlPoints[2] : null;
      const pEnd = isCubic ? controlPoints[3] : controlPoints[2];

      const estLength = Math.hypot(pEnd.x - p0.x, pEnd.y - p0.y) * 2;
      const steps = Math.max(20, Math.ceil(estLength * 3));
      const map = new Map();

      for (let i = 0; i <= steps; i++) {
        const t = i / steps;
        let x, y;
        if (isCubic) {
          const mt = 1 - t;
          x = mt * mt * mt * p0.x + 3 * mt * mt * t * p1.x + 3 * mt * t * t * p2.x + t * t * t * pEnd.x;
          y = mt * mt * mt * p0.y + 3 * mt * mt * t * p1.y + 3 * mt * t * t * p2.y + t * t * t * pEnd.y;
        } else {
          const mt = 1 - t;
          x = mt * mt * p0.x + 2 * mt * t * p1.x + t * t * pEnd.x;
          y = mt * mt * p0.y + 2 * mt * t * p1.y + t * t * pEnd.y;
        }
        const cellX = Math.round(x), cellY = Math.round(y);
        map.set(`${cellX},${cellY}`, { x: cellX, y: cellY });
      }
      return Array.from(map.values());
    }
  }

  globalThis.AlgorithmicAscii = AlgorithmicAscii;
  globalThis.SubpixelRaster = SubpixelRaster;
  globalThis.JAVE_CONVERT_RULES = JAVE_CONVERT_RULES;
})();
