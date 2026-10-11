package main.java;

public enum MagicType {
    KINETIC("»", "Kinetic", 0xc9c9c9, null),
    ELEMENTAL("▲", "Elemental", 0xfa6234, EffectType.BURN),
    PURITY("*", "Purity", 0xfff78d, EffectType.BLESSING),
    ABYSSAL("~", "Abyssal", 0x9b0000, EffectType.CURSE),
    NECROTIC("✞", "Necrotic", 0x6200ff, EffectType.LIFE_STEAL),
    DRAGONIC("^", "Dragonic", 0xff8800, EffectType.ROUGH),
    ARCANE("⚝", "Arcane", 0x4aaeff, EffectType.ARCANE_FOCUS),
    CHAOS("※", "Chaos", 0xac039e, EffectType.PURE_CHAOS),
    INFINITY("∞", "Infinity", 0xc9c9c9, EffectType.INFINITE);

    private final String symbol;
    private final String name;
    private final int color;
    private final EffectType exclusiveEffectType;

    MagicType(String symbol, String name, int color, EffectType exclusiveEffectType) {
        this.symbol = symbol;
        this.name = name;
        this.color = color;
        this.exclusiveEffectType = exclusiveEffectType;
    }

    public String getSymbol() {return symbol;}
    public String getDisplayName() { return name; }
    public int getColor() { return color; }
    public EffectType getExclusiveEffectType(){return exclusiveEffectType;}
}