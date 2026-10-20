package main.java.entity.monster;

import main.java.DifficultyLevel;
import main.java.MagicType;

import java.util.List;
import java.util.function.Supplier;

// Monster catalog. Each entry uses a Supplier so every create() call returns
// a fresh Monster with independent health/stat state.
public enum MonsterType {

    AXE_ROGUE(() -> new Monster("Axe Rogue", "He loves his axe so much, he has married it.", 200, 60, 70, 60, DifficultyLevel.LOW, MagicType.KINETIC)),
    ABSTRAY_ANGEL(() -> new Monster("Abstray Angel", "This Angel clearly left the holy path.", 100, 80, 100, 80, DifficultyLevel.MEDIUM, MagicType.PURITY)),
    BLADE_BRUTE(() -> new Monster("Blade Brute", "Straight from hell this brutal monster knows how to fight with an sword!", 150, 100, 120, 90, DifficultyLevel.MEDIUM, MagicType.ABYSSAL)),
    CHAOS_ABOMINATION(() -> new Monster("Chaos Abomination", "EVeN thIs deScription goT chAOt'c.", 200, 150, 140, 130, DifficultyLevel.HIGH, MagicType.CHAOS)),
    FIRE_SPRITE(() -> new Monster("Fire Sprite", "It can ignite flames with up to 100000°C temperature.", 130, 100, 110, 80, DifficultyLevel.INTERMEDIATE, MagicType.ELEMENTAL)),
    GIANT_SPIDER(() -> new Monster("Giant Spider", "If you're scared of the spider in your wardrobe this is your Nightmare", 100, 130, 100, 70, DifficultyLevel.INTERMEDIATE, MagicType.NECROTIC)),
    GRIM_REAPER(() -> new Monster("Grim Reaper", "Straight from your worst nightmare!", 130, 120, 130, 80, DifficultyLevel.MEDIUM, MagicType.NECROTIC)),
    HALF_BLOOD_DEMON(() -> new Monster("Half Blood Demon", "Half Demon, half Tiefling you don't want to upset this abomination of Hell", 170, 100, 120, 80, DifficultyLevel.MEDIUM, MagicType.ABYSSAL)),
    LESSER_WYVERN(() -> new Monster("Lesser Wyvern", "Not the kind of dragon that can crush you under their feet, but you still don't want to mess with them.", 150, 200, 130, 120, DifficultyLevel.HIGH, MagicType.DRAGONIC)),
    FELID_SPELLWEAVER(() -> new Monster("Felid Spellweaver", "Once an honorable and High Priest of an Arcana Temple, this felid creature commited high treason and now bundles its frust into pure magic.", 180, 140, 150, 130, DifficultyLevel.HIGH, MagicType.ARCANE)),
    ORK(() -> new Monster("Ork", "Just the kind of Ork you know from the movies: Grumpy, Stinky and with a really hard smash", 150, 50, 80, 100, DifficultyLevel.INTERMEDIATE, MagicType.KINETIC)),
    PURE_GRYPHON(() -> new Monster("Pure Gryphon", "This ancient and holy creature is always in a bad mood and regularly turns his opponent to dust using a pure beam of magic.", 130, 130, 110, 110, DifficultyLevel.MEDIUM, MagicType.PURITY)),
    SKELETON(() -> new Monster("Skeleton", "This leader of an skeleton regiment that rattles with every step.", 130, 100, 90, 100, DifficultyLevel.INTERMEDIATE, MagicType.KINETIC)),
    TONGUE_CRAWLER(() -> new Monster("Tongue Crawler", "Blind on both eyes it relies solely on his tongue for navigation.", 160, 100, 100, 90, DifficultyLevel.MEDIUM, MagicType.ABYSSAL)),
    ZOMBIE(() -> new Monster("Zombie", "Frankenstein's Monster is an abomination that feeds on brains and yet is pretty dumb", 120, 80, 80, 80, DifficultyLevel.LOW, MagicType.NECROTIC)),
    FROST_JACKALOPE(() -> new Monster("Frost Jackalope", "Don't underestimate this quick bunny like, he can turn you into an cube of ice", 100, 160, 130, 60, DifficultyLevel.MEDIUM, MagicType.ELEMENTAL)),

    INFINITY_DRAGON(() -> new Monster("Infinity Dragon", "This ancient Creature is so powerful it can destroy the whole world with one tail-whip!", 300, 140, 200, 160, DifficultyLevel.EXTREME, MagicType.INFINITY));
    private final Supplier<Monster> supplier;

    MonsterType(Supplier<Monster> supplier) {
        this.supplier = supplier;
    }

    public Monster create() {
        return supplier.get();
    }


    // Picks a random monster from the pool matching the requested difficulty
    public static Monster getMonsterByDifficulty(DifficultyLevel difficulty) {
        List<MonsterType> valids = switch (difficulty) {
            case NONE -> null;
            case LOW -> List.of(MonsterType.AXE_ROGUE, MonsterType.ZOMBIE);
            case INTERMEDIATE ->
                    List.of(MonsterType.FIRE_SPRITE, MonsterType.GIANT_SPIDER, MonsterType.ORK, MonsterType.SKELETON);
            case MEDIUM ->
                    List.of(MonsterType.ABSTRAY_ANGEL, MonsterType.BLADE_BRUTE, MonsterType.GRIM_REAPER, MonsterType.TONGUE_CRAWLER, MonsterType.FROST_JACKALOPE, MonsterType.PURE_GRYPHON, MonsterType.HALF_BLOOD_DEMON);
            case HIGH ->
                    List.of(MonsterType.FELID_SPELLWEAVER, MonsterType.CHAOS_ABOMINATION, MonsterType.HALF_BLOOD_DEMON);
            case EXTREME -> List.of(MonsterType.INFINITY_DRAGON);
        };
        // NONE has no encounter pool
        if (valids == null) {
            return null;
        }
        int random = (int) (Math.random() * (valids.size()));


        return valids.get(random).create();
    }
}