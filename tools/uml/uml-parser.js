(function (root, factory) {
  var api = factory();
  if (typeof module !== "undefined" && module.exports) module.exports = api;
  if (root) root.UmlParser = api;
})(typeof globalThis !== "undefined" ? globalThis : this, function () {
  "use strict";

  var MODS = new Set([
    "public", "protected", "private", "static", "final", "abstract",
    "synchronized", "native", "transient", "volatile", "strictfp",
    "default", "sealed", "non-sealed"
  ]);

  var COLLECTION_BASES = new Set([
    "List", "ArrayList", "LinkedList", "Set", "HashSet", "TreeSet", "LinkedHashSet",
    "Collection", "Iterable", "Queue", "Deque", "ArrayDeque", "PriorityQueue",
    "Vector", "Stack", "SortedSet", "NavigableSet", "BlockingQueue",
    "CopyOnWriteArrayList", "ConcurrentLinkedQueue", "EnumSet"
  ]);

  var MAP_BASES = new Set([
    "Map", "HashMap", "TreeMap", "LinkedHashMap", "ConcurrentHashMap", "WeakHashMap",
    "SortedMap", "NavigableMap", "Hashtable", "Dictionary", "ConcurrentMap", "EnumMap"
  ]);

  var EXCEPTION_BASES = new Set([
    "Exception", "RuntimeException", "Throwable", "Error", "IOException", "SQLException",
    "IllegalArgumentException", "IllegalStateException", "NullPointerException",
    "NumberFormatException", "IndexOutOfBoundsException", "ArrayIndexOutOfBoundsException",
    "ClassNotFoundException", "InterruptedException", "UnsupportedOperationException",
    "ArithmeticException", "FileNotFoundException", "ParseException"
  ]);

  function stripCommentsAndStrings(src) {
    var out = "";
    var i = 0;
    var n = src.length;
    while (i < n) {
      var c = src.charAt(i);
      var c2 = src.charAt(i + 1);
      if (c === "/" && c2 === "/") {
        while (i < n && src.charAt(i) !== "\n") { out += " "; i++; }
      } else if (c === "/" && c2 === "*") {
        out += "  "; i += 2;
        while (i < n && !(src.charAt(i) === "*" && src.charAt(i + 1) === "/")) {
          out += src.charAt(i) === "\n" ? "\n" : " ";
          i++;
        }
        if (i < n) { out += "  "; i += 2; }
      } else if (c === '"' || c === "'") {
        var quote = c;
        out += c; i++;
        while (i < n) {
          if (src.charAt(i) === "\\") { out += "  "; i += 2; continue; }
          if (src.charAt(i) === quote) { out += quote; i++; break; }
          out += src.charAt(i) === "\n" ? "\n" : " ";
          i++;
        }
      } else {
        out += c; i++;
      }
    }
    return out;
  }

  function stripGenericSpaces(s) {
    var out = "";
    var depth = 0;
    for (var i = 0; i < s.length; i++) {
      var c = s.charAt(i);
      if (c === "<") { depth++; out += c; }
      else if (c === ">") { if (depth > 0) depth--; out += c; }
      else if (depth > 0 && /\s/.test(c)) { /* drop */ }
      else out += c;
    }
    return out;
  }

  function safeName(n) {
    return String(n).replace(/[^A-Za-z0-9_]/g, "_");
  }

  function sanitizeType(t) {
    if (!t) return "";
    var out = "";
    var depth = 0;
    for (var i = 0; i < t.length; i++) {
      var c = t.charAt(i);
      if (c === "<") depth++;
      else if (c === ">") { if (depth > 0) depth--; }
      else if (depth === 0) out += c;
    }
    return out.replace(/\[\]/g, "").replace(/\.\.\./g, "").trim();
  }

  function splitTopLevel(s) {
    var out = [];
    var depth = 0;
    var cur = "";
    for (var i = 0; i < s.length; i++) {
      var c = s.charAt(i);
      if (c === "<" || c === "(" || c === "[" || c === "{") depth++;
      else if (c === ">" || c === ")" || c === "]" || c === "}") { if (depth > 0) depth--; }
      if (c === "," && depth === 0) { out.push(cur); cur = ""; continue; }
      cur += c;
    }
    if (cur.trim()) out.push(cur);
    return out;
  }

  function findMatchingBrace(src, open) {
    var depth = 0;
    for (var i = open; i < src.length; i++) {
      var c = src.charAt(i);
      if (c === "{") depth++;
      else if (c === "}") { depth--; if (depth === 0) return i; }
    }
    return -1;
  }

  function extractMemberTexts(body) {
    var out = [];
    var i = 0;
    var n = body.length;
    while (i < n) {
      while (i < n && (/\s/.test(body.charAt(i)) || body.charAt(i) === ";")) i++;
      if (i >= n) break;
      if (body.charAt(i) === "}") { i++; continue; }
      var start = i;
      var paren = 0;
      var end = -1;
      var kind = null;
      while (i < n) {
        var c = body.charAt(i);
        if (c === "(") paren++;
        else if (c === ")") { if (paren > 0) paren--; }
        else if (c === "=" && paren === 0) {
          i++;
          var d = 0;
          while (i < n) {
            var cc = body.charAt(i);
            if (cc === "{") d++;
            else if (cc === "}") { if (d === 0) break; d--; }
            else if (cc === ";" && d === 0) break;
            i++;
          }
          kind = "field"; end = i; break;
        } else if (c === "{" && paren === 0) { kind = "method"; end = i; break; }
        else if (c === ";" && paren === 0) { kind = "field"; end = i; break; }
        i++;
      }
      if (kind === null) break;
      var text = body.slice(start, end).trim();
      if (kind === "method") {
        var dd = 0;
        while (i < n) {
          var ch = body.charAt(i);
          if (ch === "{") dd++;
          else if (ch === "}") { dd--; if (dd === 0) { i++; break; } }
          i++;
        }
      } else if (body.charAt(i) === ";") {
        i++;
      }
      if (text) out.push(text);
    }
    return out;
  }

  function isNestedTypeDecl(text) {
    var t = text.replace(/@[\w.]+(?:\([^)]*\))?/g, " ");
    t = t.replace(/^\s*(?:(?:public|protected|private|static|final|abstract|sealed|non-sealed|strictfp)\s+)*/, "");
    return /^(class|interface|enum)\s/.test(t);
  }

  function parseField(text) {
    var t = text.trim();
    var eq = t.indexOf("=");
    if (eq >= 0) t = t.slice(0, eq);
    t = t.replace(/;$/, "").trim();
    if (!t) return null;
    t = stripGenericSpaces(t);
    var parts = t.split(/\s+/).filter(Boolean);
    var mods = [];
    while (parts.length > 1 && MODS.has(parts[0])) mods.push(parts.shift());
    if (parts.length < 2) return null;
    var name = parts.pop();
    var type = parts.join(" ");
    var arr = name.match(/\[[^\]]*\]\s*$/);
    if (arr) { type += "[]"; name = name.slice(0, arr.index); }
    return { mods: mods, type: type, name: name };
  }

  function parseParam(p) {
    var t = stripGenericSpaces(p.trim());
    if (!t) return null;
    var parts = t.split(/\s+/).filter(Boolean);
    while (parts.length && MODS.has(parts[0])) parts.shift();
    if (!parts.length) return null;
    var name = "";
    var type = parts.join(" ");
    if (parts.length > 1) { name = parts.pop(); type = parts.join(" "); }
    var arr = name.match(/\[[^\]]*\]\s*$/);
    if (arr) { type += "[]"; name = name.slice(0, arr.index); }
    return { type: type, name: name };
  }

  function parseMethod(text, className) {
    var t = text.trim();
    var parenIdx = t.indexOf("(");
    if (parenIdx < 0) return null;
    var closeIdx = t.lastIndexOf(")");
    var head = t.slice(0, parenIdx).trim();
    var paramsRaw = closeIdx > parenIdx ? t.slice(parenIdx + 1, closeIdx) : "";
    var parts = stripGenericSpaces(head).split(/\s+/).filter(Boolean);
    if (!parts.length) return null;
    var name = parts.pop();
    var mods = [];
    while (parts.length && MODS.has(parts[0])) mods.push(parts.shift());
    var ret = parts.join(" ");
    var params = splitTopLevel(paramsRaw).map(parseParam).filter(Boolean);
    return { mods: mods, ret: ret, name: safeName(name), params: params, isCtor: name === className };
  }

  function pushTypeList(str, out) {
    var cleaned = str.replace(/\s+/g, " ").trim();
    var parts = splitTopLevel(cleaned);
    for (var i = 0; i < parts.length; i++) {
      var part = sanitizeType(parts[i]);
      if (!part) continue;
      var seg = part.split(".").pop().trim();
      var m = seg.match(/[A-Za-z_$][\w$]*/);
      if (m) out.push(safeName(m[0]));
    }
  }

  function splitEnumBody(body) {
    var depth = 0;
    for (var i = 0; i < body.length; i++) {
      var c = body.charAt(i);
      if (c === "(" || c === "{") depth++;
      else if (c === ")" || c === "}") { if (depth > 0) depth--; }
      else if (c === ";" && depth === 0) {
        return { constPart: body.slice(0, i), memberPart: body.slice(i + 1) };
      }
    }
    return { constPart: body, memberPart: "" };
  }

  function parseJava(source) {
    var clean = stripCommentsAndStrings(source);
    var results = [];
    var re = /\b(class|interface|enum)\s+([A-Za-z_$][\w$]*)/g;
    var m;
    while ((m = re.exec(clean)) !== null) {
      var kind = m[1];
      var rawName = m[2];
      var before = clean.slice(0, m.index);
      var boundary = Math.max(
        before.lastIndexOf(";"),
        before.lastIndexOf("{"),
        before.lastIndexOf("}")
      );
      var prefix = before.slice(boundary + 1);
      var isAbstract = /\babstract\b/.test(prefix);
      var isStatic = /\bstatic\b/.test(prefix);
      var nameEnd = m.index + m[0].length;
      var j = nameEnd;
      while (j < clean.length && /\s/.test(clean.charAt(j))) j++;
      if (clean.charAt(j) === "<") {
        var gd = 0;
        for (; j < clean.length; j++) {
          if (clean.charAt(j) === "<") gd++;
          else if (clean.charAt(j) === ">") { gd--; if (gd === 0) { j++; break; } }
        }
        nameEnd = j;
      }
      var braceOpen = -1;
      for (var k = nameEnd; k < clean.length; k++) {
        if (clean.charAt(k) === "{") { braceOpen = k; break; }
        if (clean.charAt(k) === ";") break;
      }
      if (braceOpen < 0) continue;
      var close = findMatchingBrace(clean, braceOpen);
      if (close < 0) continue;

      var body = clean.slice(braceOpen + 1, close);
      var header = clean.slice(nameEnd, braceOpen);

      var extendsList = [];
      var implementsList = [];
      var ext = header.match(/\bextends\s+([\s\S]+?)(?=\bimplements\b|$)/);
      if (ext) pushTypeList(ext[1], extendsList);
      var impl = header.match(/\bimplements\s+([\s\S]+)$/);
      if (impl) pushTypeList(impl[1], implementsList);

      var cls = {
        kind: kind,
        name: safeName(rawName),
        rawName: rawName,
        superclasses: extendsList,
        interfaces: implementsList,
        constants: [],
        fields: [],
        methods: [],
        isAbstract: isAbstract,
        isStatic: isStatic,
        isException: false,
        parentName: null,
        start: braceOpen,
        end: close
      };

      if (kind === "enum") {
        var split = splitEnumBody(body);
        var consts = splitTopLevel(split.constPart);
        for (var ci = 0; ci < consts.length; ci++) {
          var nm = consts[ci].split(/[({]/)[0].trim();
          if (/^[A-Za-z_$][\w$]*$/.test(nm)) cls.constants.push(nm);
        }
        body = split.memberPart;
      }

      var texts = extractMemberTexts(body);
      for (var ti = 0; ti < texts.length; ti++) {
        var text = texts[ti];
        if (isNestedTypeDecl(text)) continue;
        var parenIdx = text.indexOf("(");
        var eqIdx = text.indexOf("=");
        var isMethod = parenIdx >= 0 && (eqIdx < 0 || parenIdx < eqIdx);
        if (isMethod) {
          var mth = parseMethod(text, rawName);
          if (mth && mth.name) cls.methods.push(mth);
        } else {
          var fld = parseField(text);
          if (fld && fld.name) cls.fields.push(fld);
        }
      }
      results.push(cls);
    }

    for (var a = 0; a < results.length; a++) {
      var inner = results[a];
      var best = null;
      for (var b = 0; b < results.length; b++) {
        if (a === b) continue;
        var outer = results[b];
        if (outer.start < inner.start && inner.end < outer.end) {
          if (!best || outer.start > best.start) best = outer;
        }
      }
      if (best) inner.parentName = best.name;
      inner.isException = isExceptionClass(inner);
    }
    return results;
  }

  function visibility(mods, fallback) {
    if (mods.indexOf("private") >= 0) return "-";
    if (mods.indexOf("protected") >= 0) return "#";
    if (mods.indexOf("public") >= 0) return "+";
    return fallback || "~";
  }

  function isExceptionClass(cls) {
    if (/Exception$|Error$|Throwable$/.test(cls.rawName)) return true;
    var i;
    for (i = 0; i < cls.superclasses.length; i++) {
      var s = cls.superclasses[i];
      if (EXCEPTION_BASES.has(s) || /Exception$|Error$|Throwable$/.test(s)) return true;
    }
    for (i = 0; i < cls.interfaces.length; i++) {
      if (/Exception$|Error$/.test(cls.interfaces[i])) return true;
    }
    return false;
  }

  function classAnnotations(cls) {
    var tags = [];
    if (cls.kind === "interface") tags.push("interface");
    else if (cls.kind === "enum") tags.push("enumeration");
    else {
      if (cls.isAbstract) tags.push("abstract");
      if (cls.isException) tags.push("exception");
    }
    if (cls.parentName) tags.push(cls.isStatic ? "nested" : "inner");
    return tags;
  }

  function associationInfo(rawType) {
    var t = (rawType || "").trim();
    if (!t) return { element: "", many: false };
    if (t.indexOf("[") >= 0) {
      return { element: sanitizeType(t.replace(/\[[^\]]*\]/g, "")), many: true };
    }
    var lt = t.indexOf("<");
    if (lt >= 0) {
      var base = sanitizeType(t.slice(0, lt)).split(".").pop();
      var inner = t.slice(lt + 1, t.lastIndexOf(">"));
      var args = splitTopLevel(inner);
      if (MAP_BASES.has(base) && args.length) {
        return { element: sanitizeType(args[args.length - 1]), many: true };
      }
      if (COLLECTION_BASES.has(base) && args.length) {
        return { element: sanitizeType(args[0]), many: true };
      }
      return { element: base, many: false };
    }
    return { element: sanitizeType(t), many: false };
  }

  function buildMermaid(classes) {
    var lines = ["classDiagram"];
    var known = new Set(classes.map(function (c) { return c.name; }));

    for (var ci = 0; ci < classes.length; ci++) {
      var cls = classes[ci];
      var tags = classAnnotations(cls);
      var members = [];

      for (var a = 0; a < cls.constants.length; a++) members.push("  " + cls.constants[a]);

      var isIface = cls.kind === "interface";
      for (var f = 0; f < cls.fields.length; f++) {
        var fld = cls.fields[f];
        var ft = sanitizeType(fld.type);
        if (!ft) continue;
        var fs = fld.mods.indexOf("static") >= 0 ? "$" : "";
        members.push("  " + visibility(fld.mods, isIface ? "+" : "~") + ft + " " + fld.name + fs);
      }

      for (var mi = 0; mi < cls.methods.length; mi++) {
        var mth = cls.methods[mi];
        var params = mth.params.map(function (p) {
          var pt = sanitizeType(p.type || "");
          return (pt + (p.name ? " " + p.name : "")).trim();
        }).join(", ");
        var ret = mth.isCtor ? "" : " " + (sanitizeType(mth.ret) || "void");
        var isAbstractMethod = mth.mods.indexOf("abstract") >= 0 ||
          (isIface && mth.mods.indexOf("default") < 0 && mth.mods.indexOf("static") < 0);
        var classifiers = (isAbstractMethod ? "*" : "") +
          (mth.mods.indexOf("static") >= 0 ? "$" : "");
        members.push("  " + visibility(mth.mods, isIface ? "+" : "~") +
          mth.name + "(" + params + ")" + ret + classifiers);
      }

      if (members.length || tags.length) {
        lines.push("  class " + cls.name + " {");
        if (tags.length) lines.push("  <<" + tags.join(" ") + ">>");
        for (var mm = 0; mm < members.length; mm++) lines.push(members[mm]);
        lines.push("  }");
      } else {
        lines.push("  class " + cls.name);
      }
    }

    var seen = new Set();
    var rels = [];
    function addRel(r) { if (!seen.has(r)) { seen.add(r); rels.push(r); } }

    for (var e = 0; e < classes.length; e++) {
      var c1 = classes[e];
      for (var pi = 0; pi < c1.superclasses.length; pi++) addRel(c1.superclasses[pi] + " <|-- " + c1.name);
      for (var ii = 0; ii < c1.interfaces.length; ii++) addRel(c1.interfaces[ii] + " <|.. " + c1.name);
    }

    for (var ai = 0; ai < classes.length; ai++) {
      var c2 = classes[ai];
      for (var fi = 0; fi < c2.fields.length; fi++) {
        var field = c2.fields[fi];
        var info = associationInfo(field.type);
        if (!info.element || !known.has(info.element)) continue;
        addRel(c2.name + ' "1" --> "' + (info.many ? "0..*" : "1") + '" ' +
          info.element + " : " + field.name);
      }
    }
    return lines.concat(rels).join("\n");
  }

  function parseAll(files) {
    var classes = [];
    var byName = new Map();
    for (var i = 0; i < files.length; i++) {
      var parsed = parseJava(files[i].content);
      for (var j = 0; j < parsed.length; j++) {
        var c = parsed[j];
        if (byName.has(c.name)) continue;
        byName.set(c.name, c);
        classes.push(c);
      }
    }
    return { classes: classes, mermaid: buildMermaid(classes) };
  }

  return {
    parseJava: parseJava,
    buildMermaid: buildMermaid,
    parseAll: parseAll,
    _internal: {
      stripCommentsAndStrings: stripCommentsAndStrings,
      extractMemberTexts: extractMemberTexts,
      parseField: parseField,
      parseMethod: parseMethod,
      sanitizeType: sanitizeType
    }
  };
});
