package main.java;

public enum MagicType {
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
    private static final double[][] EFFECTIVENESS = {
            //  KIN  ELE  PUR  ABY  NEC  DRA  ARC  CHA  INF
            {   1.0, 1.0, 0.5, 1.0, 0.5, 0.5, 0.5, 0.5, 0.5 }, // KINETIC
            {   2.0, 1.0, 2.0, 1.0, 2.0, 0.5, 1.0, 1.0, 1.0 }, // ELEMENTAL
            {   1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 0.5, 1.0 }, // PURITY
            {   1.0, 1.0, 1.0, 0.5, 1.0, 1.0, 2.0, 1.0, 1.0 }, // ABYSSAL
            {   2.0, 2.0, 1.0, 2.0, 1.0, 0.5, 0.5, 2.0, 1.0 }, // NECROTIC
            {   1.0, 2.0, 1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 1.0 }, // DRAGONIC
            {   2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0 }, // ARCANE
            {   2.0, 1.0, 2.0, 1.0, 2.0, 1.0, 2.0, 1.0, 1.0 }, // CHAOS
            {   2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0 }  // INFINITY
    };
    public double getEffectivenessAgainst(MagicType defender) {
        return EFFECTIVENESS[this.ordinal()][defender.ordinal()];
    }
    public static double getEffectiveness(MagicType attacker, MagicType defender) {
        return EFFECTIVENESS[attacker.ordinal()][defender.ordinal()];
    }

    MagicType(String symbol, String name, int color) {
        this.symbol = symbol;
        this.name = name;
        this.color = color;
    }

    public String getSymbol() {return symbol;}
    public String getDisplayName() { return name; }
    public int getColor() { return color; }
}