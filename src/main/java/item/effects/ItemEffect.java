package main.java.item.effects;


// A single stat modifier provided by an item.
// Flat stats use integer values; multipliers use fractions (e.g. 1.10).
public record ItemEffect(ItemEffectType itemEffectType, double effectStrength) {
}
