def mirror_ascii_art(text: str) -> str:
    mirror_map = {
        "/": "\\",
        "\\": "/",
        "(": ")",
        ")": "(",
        "[": "]",
        "]": "[",
        "{": "}",
        "}": "{",
        "<": ">",
        ">": "<",
        "`": "'",
        "'": "`",
    }

    lines = text.splitlines()
    if not lines:
        return ""

    # 1. Maximale Zeilenlänge ermitteln
    max_len = max(len(line) for line in lines)

    mirrored_lines = []

    for line in lines:
        # 2. Zeile rechts mit Leerzeichen auffüllen, damit alle gleich lang sind
        padded_line = line.ljust(max_len)

        # 3. Umkehren und Zeichen spiegeln
        mirrored_line = "".join(
            mirror_map.get(char, char) for char in reversed(padded_line)
        )
        mirrored_lines.append(mirrored_line)

    return "\n".join(mirrored_lines)


# Eingabe-Art
ascii_art = r"""	 .      .
     |\____/|
    (\|----|/)
     \ 0  0 /
      |    |
   ___/\../\____
  /     --       \
 /  \         /   \
|    \___/___/(   |
\   /|  }{   | \  )
 \  ||__}{__|  |  |
  \  |;;;;;;;\  \ / \_______
   \ /;;;;;;;;| [,,[|======'
     |;;;;;;/ |     /
     ||;;|\   |
     ||;;/|   /
     \_|:||__|
      \ ;||  /
snd   |= || =|
      |= /\ =|
      /_/  \_\ """

print(mirror_ascii_art(ascii_art))