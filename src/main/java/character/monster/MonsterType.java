package main.java.character.monster;

import main.java.MagicType;


import java.util.function.Supplier;

public enum MonsterType {
    ABSTRAY_ANGEL(() -> new Monster("Abstray Angel", "This Angel clearly left the holy path.", 100, 80, null, MagicType.PURITY )),
    AXE_ROGUE(()-> new Monster("Axe Rogue", "He loves his axe so much, he has married it.", 200, 60, null, MagicType.KINETIC)),
    BLADE_BRUTE(()-> new Monster("Blade Brute", "Straight from hell this brutal monster knows how to fight with an sword!", 150, 100, null, MagicType.ABYSSAL)),
    CHAOS_ABOMINATION(() -> new Monster("Chaos Abomination", "EVeN thIs deScription goT chAOt'c.", 200, 150, null, MagicType.CHAOS)),
    FIRE_SPRITE(() -> new Monster("Fire Sprite", "It can ignite flames with up to 100000°C temperature.", 130, 100, null, MagicType.ELEMENTAL)),
    GIANT_SPIDER(() -> new Monster("Giant Spider", "If you're scared of the spider in your wardrobe this is your Nightmare", 100,90, null, MagicType.NECROTIC )),
    GRIM_REAPER(() -> new Monster("Grim Reaper", "Straight from your worst nightmare!", 100, 120, null, MagicType.NECROTIC)),
    HALF_BLOOD_DEMON(() -> new Monster ("Half Blood Demon", "Half Demon, half Tiefling you don't want to upset this abomination of Hell", 170, 100, null, MagicType.ABYSSAL)),
    LESSER_WYVERN(() -> new Monster ("Lesser Wyvern", "Not the kind of dragon that can crush you under their feet, but you still don't want to mess with them.", 150, 200, null, MagicType.DRAGONIC)),
    FELID_SPELLWEAVER(() -> new Monster("Felid Spellweaver", "Once an honorable and High Priest of an Arcana Temple, this felid creature commited high treason and now bundles its frust into pure magic.", 180, 180, null, MagicType.ARCANE)),
    ORK (() -> new Monster("Ork", "Just the kind of Ork you know from the movies: Grumpy, Stinky and with a really hard smash", 150, 50, null, MagicType.KINETIC)),
    PURE_GRYPHON (() -> new Monster ("Pure Gryphon", "This ancient and holy creature is always in a bad mood and regularly turns his opponent to dust using a pure beam of magic.", 130, 130, null, MagicType.PURITY)),
    SKELETON (() -> new Monster ("Skeleton", "This leader of an skeleton regiment that rattles with every step.", 130, 100, null, MagicType.KINETIC)),
    TONGUE_CRAWLER(() -> new Monster ("Tongue Crawler", "Blind on both eyes it relies solely on his tongue for navigation.", 160, 100, null, MagicType.ABYSSAL)),
    ZOMBIE (() -> new Monster("Zombie", "Frankenstein's Monster is an abomination that feeds on brains and yet is pretty dumb", 120, 80, null, MagicType.NECROTIC)),
    FROST_JACKALOPE(()-> new Monster("Frost Jackalope", "Don't underestimate this bunny like, he can turn you into an cube of ice", 100, 160, null, MagicType.ELEMENTAL));

    private final Supplier<Monster> supplier;
    MonsterType(Supplier<Monster> supplier) {
        this.supplier = supplier;
    }

    public Monster create() {
        return supplier.get();
    }
}
