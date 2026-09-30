package main.java;

public enum DamageType {
    KINETIC("»", "Kinetic", 0xc9c9c9),
    ELEMENTAL("▲", "Elemental", 0xfa6234),
    PURITY("*", "Purity", 0xfff78d),
    ABYSSAL("~", "Abyssal", 0x9b0000),
    NECROTIC("✞", "Necrotic", 0x6200ff),
    DRAGONIC("^", "Dragonic", 0xff8800),
    ARCANE("⚝", "Arcane", 0x4aaeff),
    CHAOS("※", "Chaos", 0xac039e),
    INFINITY("∞", "Infinity", 0xc9c9c9);

    private final String symbol;
    private final String name;
    private final int color;

    DamageType(String symbol, String name, int color) {
        this.symbol = symbol;
        this.name = name;
        this.color = color;
    }

    public String getSymbol() {return symbol;}
    public String getDisplayName() { return name; }
    public int getColor() { return color; }
}