#!/usr/bin/env python3
"""
tools/generate_scroll_items.py

Scans all Spell classes in src/main/java/spell/spells/ and generates
corresponding Scroll enum constants for ItemType.java.

Usage:
  python3 tools/generate_scroll_items.py           # Prints generated Java code to stdout
  python3 tools/generate_scroll_items.py --apply   # Automatically updates ItemType.java with enum constants
  python3 tools/generate_scroll_items.py --apply --loot # Also adds scrolls to getItemTypeByQuality loot pools
"""

import os
import re
import sys
from pathlib import Path

# Mapping of MagicType to Quality
MAGIC_TYPE_QUALITY_MAP = {
    "KINETIC": "COMMON",
    "ELEMENTAL": "UNCOMMON",
    "PURITY": "UNCOMMON",
    "ABYSSAL": "RARE",
    "NECROTIC": "RARE",
    "DRAGONIC": "EPIC",
    "ARCANE": "EPIC",
    "CHAOS": "LEGENDARY",
    "INFINITY": "MYTHIC",
}

# Optional overrides for specific spells if you want custom qualities
SPELL_QUALITY_OVERRIDES = {
    # e.g. "TitanGrit": "UNCOMMON",
}


def to_snake_case(name: str) -> str:
    s = re.sub(r'([A-Z]+)', r'_\1', name)
    return s.strip('_').upper()


def find_project_root() -> Path:
    current = Path(__file__).resolve().parent
    while current != current.parent:
        if (current / "src" / "main" / "java").exists():
            return current
        current = current.parent
    raise FileNotFoundError("Could not locate project root containing src/main/java")


def collect_spells(spells_dir: Path):
    spells = []
    for file_path in sorted(spells_dir.glob("*.java")):
        class_name = file_path.stem
        content = file_path.read_text(encoding="utf-8")

        # Skip abstract classes or non-Spell files
        if "abstract class" in content or "class " + class_name not in content:
            continue

        # Extract MagicType from super constructor call
        magic_match = re.search(r'MagicType\.([A-Z]+)', content)
        magic_type = magic_match.group(1) if magic_match else "KINETIC"

        # Determine Quality
        quality = SPELL_QUALITY_OVERRIDES.get(class_name, MAGIC_TYPE_QUALITY_MAP.get(magic_type, "COMMON"))

        enum_name = f"SCROLL_{to_snake_case(class_name)}"

        spells.append({
            "class_name": class_name,
            "magic_type": magic_type,
            "quality": quality,
            "enum_name": enum_name,
        })
    return spells


def generate_scroll_lines(spells, is_last_block=True):
    lines = []
    for i, s in enumerate(spells):
        is_last = (i == len(spells) - 1)
        terminator = ";" if (is_last and is_last_block) else ","
        lines.append(f"    {s['enum_name']}(() -> new Scroll(new {s['class_name']}(), Quality.{s['quality']}))" + terminator)
    return lines


def generate_enum_block(spells) -> str:
    lines = [
        "    // ==========================================",
        "    // GENERATED SCROLLS (tools/generate_scroll_items.py)",
        "    // =========================================="
    ]
    lines.extend(generate_scroll_lines(spells, is_last_block=False))
    return "\n".join(lines)


def apply_to_item_type(item_type_path: Path, spells, update_loot=False):
    content = item_type_path.read_text(encoding="utf-8")

    # 1. Ensure imports
    required_imports = [
        "import main.java.item.items.Scroll;",
        "import main.java.spell.spells.*;"
    ]
    for imp in required_imports:
        if imp not in content:
            content = re.sub(r'(package\s+main\.java\.item;\s*\n)', r'\1\n' + imp + '\n', content, count=1)

    # 2. Build enum constants code block
    scroll_lines = generate_scroll_lines(spells, is_last_block=True)
    scrolls_code = (
        f"    // ==========================================\n"
        f"    // GENERATED SCROLLS (tools/generate_scroll_items.py)\n"
        f"    // ==========================================\n"
        + "\n".join(scroll_lines)
        + f"\n    // END GENERATED SCROLLS"
    )

    start_tag = "// ==========================================\n    // GENERATED SCROLLS (tools/generate_scroll_items.py)"
    end_tag = "// END GENERATED SCROLLS"

    if start_tag in content:
        # Replace existing block
        pattern = re.compile(re.escape(start_tag) + r".*?" + re.escape(end_tag), re.DOTALL)
        content = pattern.sub(scrolls_code, content)
    else:
        # Insert before supplier declaration
        pattern = re.compile(r'(\n\s*private\s+final\s+Supplier<Item>\s+supplier;)')
        match = pattern.search(content)
        if not match:
            print("Error: Could not find 'private final Supplier<Item> supplier;' in ItemType.java")
            return False

        pos = match.start()
        last_semi = content.rfind(";", 0, pos)
        if last_semi == -1:
            print("Error: Could not locate end of enum constants in ItemType.java")
            return False

        new_content = (
            content[:last_semi]
            + ",\n\n"
            + scrolls_code
            + "\n"
            + content[last_semi + 1:]
        )
        content = new_content

    # 3. Optional: update loot table in getItemTypeByQuality
    if update_loot:
        qualities = ["COMMON", "UNCOMMON", "RARE", "EPIC", "LEGENDARY", "MYTHIC"]
        for q in qualities:
            matching = [s["enum_name"] for s in spells if s["quality"] == q]
            if not matching:
                continue
            case_pattern = re.compile(rf'(case\s+{q}\s*->\s*List\.of\([^)]*?)(\s*\);)', re.DOTALL)
            match = case_pattern.search(content)
            if match:
                inner = match.group(1)
                # Remove previously added scrolls block if present
                clean_inner = re.sub(r',\s*// SCROLLS_START.*?// SCROLLS_END', '', inner, flags=re.DOTALL)
                scroll_items = ",\n                    // SCROLLS_START\n                    " + ",\n                    ".join(matching) + "\n                    // SCROLLS_END"
                content = content[:match.start()] + clean_inner + scroll_items + match.group(2) + content[match.end():]

    item_type_path.write_text(content, encoding="utf-8")
    return True


def main():
    root = find_project_root()
    spells_dir = root / "src" / "main" / "java" / "spell" / "spells"
    item_type_path = root / "src" / "main" / "java" / "item" / "ItemType.java"

    spells = collect_spells(spells_dir)
    print(f"Found {len(spells)} spells in {spells_dir}")

    if "--apply" in sys.argv:
        update_loot = "--loot" in sys.argv
        success = apply_to_item_type(item_type_path, spells, update_loot=update_loot)
        if success:
            loot_str = " and loot tables" if update_loot else ""
            print(f"Successfully updated {item_type_path} with {len(spells)} Scroll enum entries{loot_str}!")
    else:
        print("\n--- Generated Enum Entries for ItemType.java ---\n")
        print(generate_enum_block(spells))
        print("\n--- By Quality (for getItemTypeByQuality switch-case) ---\n")
        qualities = ["COMMON", "UNCOMMON", "RARE", "EPIC", "LEGENDARY", "MYTHIC"]
        for q in qualities:
            matching = [s["enum_name"] for s in spells if s["quality"] == q]
            print(f"// {q}:")
            print("    " + ", ".join(matching))
        print("\n--- How to use ---")
        print("1. Update ItemType.java with all Scroll enum constants:")
        print("     python3 tools/generate_scroll_items.py --apply")
        print("2. (Optional) Also include all scrolls into the getItemTypeByQuality loot pools:")
        print("     python3 tools/generate_scroll_items.py --apply --loot\n")


if __name__ == "__main__":
    main()
