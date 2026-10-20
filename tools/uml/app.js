(function () {
  "use strict";

  var BG = "#0e1116";
  var EXPORT_SCALE = 3;
  var EXPORT_PAD = 28;

  var els = {
    fileInput: document.getElementById("file-input"),
    dropzone: document.getElementById("dropzone"),
    status: document.getElementById("status"),
    stage: document.getElementById("stage"),
    diagram: document.getElementById("diagram"),
    source: document.getElementById("mermaid-source"),
    sourcePanel: document.getElementById("source-panel"),
    copySource: document.getElementById("copy-source"),
    btnSvg: document.getElementById("btn-svg"),
    btnPng: document.getElementById("btn-png"),
    btnJpg: document.getElementById("btn-jpg"),
    zoomIn: document.getElementById("zoom-in"),
    zoomOut: document.getElementById("zoom-out"),
    zoomReset: document.getElementById("zoom-reset"),
    zoomLabel: document.getElementById("zoom-label")
  };

  var zoom = 1;
  var hasDiagram = false;
  var renderSeq = 0;

  /* ---------- Mermaid setup ---------- */

  if (window.mermaid) {
    window.mermaid.initialize({
      startOnLoad: false,
      securityLevel: "loose",
      theme: "dark",
      darkMode: true,
      layout: "dagre",
      // Native SVG text labels (no <foreignObject>) so the diagram can be
      // rasterised to a canvas for PNG/JPEG export.
      htmlLabels: false,
      fontFamily: "ui-sans-serif, system-ui, -apple-system, 'Segoe UI', Roboto, sans-serif",
      themeVariables: {
        darkMode: true,
        background: BG,
        primaryColor: "#151b23",
        primaryTextColor: "#e6edf3",
        primaryBorderColor: "#3b4a5a",
        secondaryColor: "#151b23",
        tertiaryColor: "#0f151c",
        lineColor: "#7d8590",
        textColor: "#e6edf3",
        mainBkg: "#151b23",
        nodeBorder: "#3b4a5a",
        clusterBkg: "#0f151c",
        clusterBorder: "#30363d",
        titleColor: "#e6edf3",
        edgeLabelBackground: BG,
        attributeBackgroundColorOdd: "#151b23",
        attributeBackgroundColorEven: "#12171e"
      },
      "class": { htmlLabels: false, useMaxWidth: false, padding: 14, hideEmptyMembersBox: false },
      flowchart: { htmlLabels: false, useMaxWidth: false },
      sequence: { useMaxWidth: false },
      state: { useMaxWidth: false }
    });
  }

  /* ---------- Status helpers ---------- */

  function setStatus(message, kind) {
    els.status.textContent = message || "";
    els.status.className = "status" + (kind ? " " + kind : "");
  }

  /* ---------- File handling ---------- */

  function handleFile(file) {
    if (!file) return;
    if (!/\.zip$/i.test(file.name)) {
      setStatus("Please choose a .zip archive.", "error");
      return;
    }
    if (typeof JSZip === "undefined") {
      setStatus("JSZip failed to load. Check your internet connection.", "error");
      return;
    }
    setStatus("Reading " + file.name + "\u2026", "busy");

    JSZip.loadAsync(file)
      .then(function (zip) {
        var entries = [];
        zip.forEach(function (path, entry) {
          if (!entry.dir && /\.java$/i.test(path)) entries.push(entry);
        });
        if (!entries.length) {
          throw new Error("No .java files found in the archive.");
        }
        return Promise.all(
          entries.map(function (entry) {
            return entry.async("string").then(function (content) {
              return { name: entry.name, content: content };
            });
          })
        );
      })
      .then(function (files) {
        var result = window.UmlParser.parseAll(files);
        if (!result.classes.length) {
          throw new Error("No Java types (class / interface / enum) were found.");
        }
        els.source.textContent = result.mermaid;
        els.sourcePanel.open = true;
        return render(result.mermaid).then(function () {
          setStatus(
            "Parsed " + result.classes.length + " type" +
              (result.classes.length === 1 ? "" : "s") +
              " from " + files.length + " file" +
              (files.length === 1 ? "" : "s") + ".",
            "ok"
          );
        });
      })
      .then(null, function (err) {
        console.error(err);
        setStatus(err && err.message ? err.message : "Could not read the archive.", "error");
      });
  }

  /* ---------- Rendering ---------- */

  function render(definition) {
    if (!window.mermaid) {
      setStatus("Mermaid failed to load. Check your internet connection.", "error");
      return Promise.resolve();
    }
    var id = "uml-" + ++renderSeq + "-" + Math.random().toString(36).slice(2, 8);
    return window.mermaid
      .render(id, definition)
      .then(function (out) {
        els.diagram.innerHTML = out.svg;
        hasDiagram = true;
        els.stage.classList.add("has-diagram");
        els.btnSvg.disabled = false;
        els.btnPng.disabled = false;
        els.btnJpg.disabled = false;
        setZoom(1);
        els.stage.scrollTop = 0;
        els.stage.scrollLeft = 0;
      })
      .then(null, function (err) {
        console.error(err);
        els.diagram.innerHTML = "";
        hasDiagram = false;
        els.stage.classList.remove("has-diagram");
        els.btnSvg.disabled = true;
        els.btnPng.disabled = true;
        els.btnJpg.disabled = true;
        setStatus("Could not render the diagram: " + (err && err.message ? err.message : err), "error");
      });
  }

  /* ---------- Zoom ---------- */

  function setZoom(value) {
    zoom = Math.min(4, Math.max(0.25, value));
    els.diagram.style.zoom = zoom;
    els.zoomLabel.textContent = Math.round(zoom * 100) + "%";
  }

  /* ---------- Export ---------- */

  function getSvgElement() {
    return els.diagram.querySelector("svg");
  }

  function svgViewBox(svg) {
    var vb = svg.viewBox && svg.viewBox.baseVal;
    if (vb && vb.width && vb.height) {
      return { x: vb.x, y: vb.y, width: vb.width, height: vb.height };
    }
    var box = svg.getBoundingClientRect();
    return { x: 0, y: 0, width: box.width, height: box.height };
  }

  function clampScale(width, height) {
    var scale = EXPORT_SCALE;
    var maxSide = 16384;
    var maxArea = 40000000;
    scale = Math.min(scale, maxSide / width, maxSide / height, Math.sqrt(maxArea / (width * height)));
    if (!isFinite(scale) || scale <= 0) scale = 1;
    return scale;
  }

  function downloadBlob(blob, filename) {
    var url = URL.createObjectURL(blob);
    var link = document.createElement("a");
    link.href = url;
    link.download = filename;
    link.style.display = "none";
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.setTimeout(function () { URL.revokeObjectURL(url); }, 60000);
  }

  function rasterize() {
    var svg = getSvgElement();
    if (!svg) return Promise.reject(new Error("Nothing to export yet."));

    var box = svgViewBox(svg);
    if (!box.width || !box.height) {
      return Promise.reject(new Error("Diagram has no measurable size."));
    }

    var scale = clampScale(box.width, box.height);
    var outW = Math.max(1, Math.round(box.width * scale));
    var outH = Math.max(1, Math.round(box.height * scale));
    var padPx = Math.round(EXPORT_PAD * scale);

    var clone = svg.cloneNode(true);
    // Give the SVG an explicit pixel size so the browser rasterises the vectors
    // at full export resolution instead of upscaling a small bitmap.
    clone.style.maxWidth = "none";
    clone.style.maxHeight = "none";
    clone.setAttribute("width", outW);
    clone.setAttribute("height", outH);
    clone.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    clone.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");

    var xml = new XMLSerializer().serializeToString(clone);
    var url = "data:image/svg+xml;charset=utf-8," + encodeURIComponent(xml);

    return new Promise(function (resolve, reject) {
      var img = new Image();
      img.decoding = "sync";
      img.onload = function () { resolve(img); };
      img.onerror = function () { reject(new Error("Failed to rasterise the SVG.")); };
      img.src = url;
    }).then(function (img) {
      var canvas = document.createElement("canvas");
      canvas.width = outW + padPx * 2;
      canvas.height = outH + padPx * 2;
      var ctx = canvas.getContext("2d");
      ctx.fillStyle = BG;
      ctx.fillRect(0, 0, canvas.width, canvas.height);
      ctx.drawImage(img, padPx, padPx, outW, outH);
      return canvas;
    });
  }

  function canvasToBlob(canvas, mime) {
    return new Promise(function (resolve, reject) {
      if (typeof canvas.toBlob === "function") {
        canvas.toBlob(function (blob) {
          if (blob) resolve(blob);
          else reject(new Error("Could not generate image data (the diagram may be too large)."));
        }, mime, 0.95);
        return;
      }
      try {
        resolve(dataUrlToBlob(canvas.toDataURL(mime, 0.95)));
      } catch (err) {
        reject(err);
      }
    });
  }

  function dataUrlToBlob(dataUrl) {
    var comma = dataUrl.indexOf(",");
    var meta = dataUrl.slice(0, comma);
    var body = dataUrl.slice(comma + 1);
    var mime = (meta.match(/:(.*?);/) || [, "image/png"])[1];
    var binary = atob(body);
    var bytes = new Uint8Array(binary.length);
    for (var i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i);
    return new Blob([bytes], { type: mime });
  }

  function exportImage(kind) {
    if (!hasDiagram) return;
    var isJpeg = kind === "jpeg";
    var mime = isJpeg ? "image/jpeg" : "image/png";
    var filename = "uml-class-diagram." + (isJpeg ? "jpg" : "png");
    setStatus("Preparing " + kind.toUpperCase() + "\u2026", "busy");

    rasterize()
      .then(function (canvas) {
        return canvasToBlob(canvas, mime);
      })
      .then(function (blob) {
        if (!blob || blob.size === 0) {
          throw new Error("The generated image was empty.");
        }
        downloadBlob(blob, filename);
        setStatus("Exported " + (isJpeg ? "JPEG" : "PNG") + " (" + Math.round(blob.size / 1024) + " KB).", "ok");
      })
      .then(null, function (err) {
        console.error(err);
        setStatus(err && err.message ? err.message : "Export failed.", "error");
      });
  }

  function exportSvg() {
    if (!hasDiagram) return;
    var svg = getSvgElement();
    if (!svg) return;

    var box = svgViewBox(svg);
    if (!box.width || !box.height) {
      setStatus("Diagram has no measurable size.", "error");
      return;
    }

    var pad = EXPORT_PAD;
    var vb = svg.viewBox && svg.viewBox.baseVal;
    var originX = vb ? vb.x : 0;
    var originY = vb ? vb.y : 0;
    var fullW = box.width + pad * 2;
    var fullH = box.height + pad * 2;

    var clone = svg.cloneNode(true);
    clone.style.maxWidth = "none";
    clone.style.maxHeight = "none";
    clone.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    clone.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
    clone.setAttribute("width", Math.round(fullW));
    clone.setAttribute("height", Math.round(fullH));
    clone.setAttribute("viewBox",
      (originX - pad) + " " + (originY - pad) + " " + fullW + " " + fullH);

    var rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
    rect.setAttribute("x", originX - pad);
    rect.setAttribute("y", originY - pad);
    rect.setAttribute("width", fullW);
    rect.setAttribute("height", fullH);
    rect.setAttribute("fill", BG);
    clone.insertBefore(rect, clone.firstChild);

    var xml = new XMLSerializer().serializeToString(clone);
    var blob = new Blob(
      ['<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n', xml],
      { type: "image/svg+xml;charset=utf-8" }
    );
    downloadBlob(blob, "uml-class-diagram.svg");
    setStatus("Exported SVG.", "ok");
  }

  /* ---------- Wiring ---------- */

  els.fileInput.addEventListener("change", function (e) {
    handleFile(e.target.files && e.target.files[0]);
    els.fileInput.value = "";
  });

  ["dragenter", "dragover"].forEach(function (type) {
    els.dropzone.addEventListener(type, function (e) {
      e.preventDefault();
      e.stopPropagation();
      els.dropzone.classList.add("dragover");
    });
  });

  ["dragleave", "drop"].forEach(function (type) {
    els.dropzone.addEventListener(type, function (e) {
      e.preventDefault();
      e.stopPropagation();
      els.dropzone.classList.remove("dragover");
    });
  });

  els.dropzone.addEventListener("drop", function (e) {
    var dt = e.dataTransfer;
    if (dt && dt.files && dt.files.length) handleFile(dt.files[0]);
  });

  window.addEventListener("dragover", function (e) { e.preventDefault(); });
  window.addEventListener("drop", function (e) { e.preventDefault(); });

  els.btnSvg.addEventListener("click", exportSvg);
  els.btnPng.addEventListener("click", function () { exportImage("png"); });
  els.btnJpg.addEventListener("click", function () { exportImage("jpeg"); });

  els.zoomIn.addEventListener("click", function () { setZoom(zoom + 0.15); });
  els.zoomOut.addEventListener("click", function () { setZoom(zoom - 0.15); });
  els.zoomReset.addEventListener("click", function () { setZoom(1); });

  els.stage.addEventListener("wheel", function (e) {
    if (!e.ctrlKey) return;
    e.preventDefault();
    setZoom(zoom + (e.deltaY < 0 ? 0.12 : -0.12));
  }, { passive: false });

  els.copySource.addEventListener("click", function (e) {
    e.preventDefault();
    e.stopPropagation();
    var text = els.source.textContent || "";
    if (!text) return;
    if (navigator.clipboard && navigator.clipboard.writeText) {
      navigator.clipboard.writeText(text).then(function () {
        setStatus("Mermaid source copied.", "ok");
      });
    }
  });

  document.addEventListener("keydown", function (e) {
    if ((e.ctrlKey || e.metaKey) && e.key === "0") {
      e.preventDefault();
      setZoom(1);
    }
  });
})();
