public enum DamageType {
    KINETIC("»", "Kinetic", "#c9c9c9"),
    ELEMENTAL("▲", "Elemental", "#fa6234"),
    PURITY("*", "Purity", "#fff78d"),
    ABYSSAL("~", "Abyssal", "#9b0000"),
    NECROTIC("✞", "Necrotic", "#6200ff"),
    DRAGONIC("^", "Dragonic", "#ff8800"),
    ARCANE("⚝", "Arcane", "#4aaeff"),
    CHAOS("※", "Chaos", "#ac039e"),
    INFINITY("∞", "Infinity", "#c9c9c9");

    private final String symbol;
    private final String name;
    private final String color;

    DamageType(String symbol, String name, String color) {
        this.symbol = symbol;
        this.name = name;
        this.color = color;
    }

    public String getSymbol() { return symbol; }
    public String getDisplayName() { return name; }
    public Color getColor() { return color; }
}