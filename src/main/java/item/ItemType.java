package main.java.item;



import main.java.spell.spells.*;
import main.java.item.items.Scroll;
import main.java.MagicType;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.item.effects.ItemEffect;
import main.java.item.effects.ItemEffectType;
import main.java.item.items.Armor;
import main.java.item.items.Artifact;
import main.java.item.items.Potion;
import main.java.item.items.Staff;

import java.util.List;
import java.util.function.Supplier;

public enum ItemType {

    // ==========================================
    // COMMON (Difficulty NONE - Level 5)
    // Kinetic is prominent; Elemental & Purity basic tools
    // ==========================================
    COMMON_STAFF(() -> new Staff("Old Wooden Cane", "A well-worn walking cane. Has assisted many journeys and occasionally deflected a stray goblin.", Quality.COMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 10),
            new ItemEffect(ItemEffectType.DAMAGE, 1.05)
    ))),
    COMMON_HEAVY_STICK(() -> new Staff("Weighted Cudgel", "A sturdy oak cudgel wrapped in leather grip, designed for direct impact.", Quality.COMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 14)
    ))),
    COMMON_FIRE_WAND(() -> new Staff("Apprentice Spark Wand", "A dry birch wand carved with simple fire runes. Prone to singeing the caster's cuffs.", Quality.COMMON, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 12),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.05)
    ))),
    COMMON_PURITY_ROD(() -> new Staff("Novice Prayer Rod", "A slender rod that emits a faint, reassuring glow when held aloft.", Quality.COMMON, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 10),
            new ItemEffect(ItemEffectType.MAX_MANA, 15)
    ))),

    COMMON_ARMOUR(() -> new Armor("Leather Tunic", "Stitched from cured cowhide. Doesn't stop much, but beats adventuring in ordinary clothes.", Quality.COMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 10),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 15)
    ))),
    COMMON_ELEMENTAL_ROBE(() -> new Armor("Cloth of Warmth", "Lined with wool and mild elemental insulation to ward off chill and embers.", Quality.COMMON, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 8),
            new ItemEffect(ItemEffectType.RESISTANCE_ELEMENTAL, 1.10)
    ))),
    COMMON_PURITY_CLOAK(() -> new Armor("Novice Cowl", "A humble linen cowl blessed by a village chaplain. Keeps the rain and minor hexes at bay.", Quality.COMMON, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 8),
            new ItemEffect(ItemEffectType.MAX_MANA, 10)
    ))),

    COMMON_HEALTH_AMULET(() -> new Artifact("Health Amulet", "A polished river stone set in copper wire that gently pulses with vital warmth.", Quality.COMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.MAX_HEALTH, 20),
            new ItemEffect(ItemEffectType.REGENERATION, 1)
    ))),
    COMMON_WARRIOR_RING(() -> new Artifact("Iron Ring", "A simple iron band that lends a firm weight to every strike.", Quality.COMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 12),
            new ItemEffect(ItemEffectType.DAMAGE, 1.05)
    ))),
    COMMON_GUARD_CHARM(() -> new Artifact("Buckler Charm", "A miniature steel plate said to deflect misfortune and blunt hits.", Quality.COMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 12),
            new ItemEffect(ItemEffectType.RESISTANCE_KINETIC, 1.10)
    ))),
    COMMON_PRAYER_BEADS(() -> new Artifact("Prayer Beads", "Smooth wooden beads that steady the mind and restore focus.", Quality.COMMON, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.MAX_MANA, 20),
            new ItemEffect(ItemEffectType.MANA_REGENERATION, 1)
    ))),
    COMMON_SWIFT_FEATHER(() -> new Artifact("Sparrow Feather", "Enchanted with light wind magic, making footwork feel effortless.", Quality.COMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.SPEED, 15),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 5)
    ))),

    COMMON_HEALTH_POTION(() -> new Potion("Minor Health Potion", "A bitter herbal tonic in a small corked vial. Tastes terrible, but knits minor cuts.", Quality.COMMON, MagicType.KINETIC, List.of(
            new CombatEffect(CombatEffectType.HEALING, 30, 0)
    ))),
    COMMON_MANA_POTION(() -> new Potion("Minor Mana Draught", "A cool, shimmering blue liquid that clears the head and restores magical reserves.", Quality.COMMON, MagicType.PURITY, List.of(
            new CombatEffect(CombatEffectType.MANA_RESTORE, 25, 0)
    ))),


    // ==========================================
    // UNCOMMON (Difficulty LOW - Level 15)
    // Targets: Axe Rogue (Kinetic), Zombie (Necrotic)
    // ==========================================
    UNCOMMON_KINETIC_STAFF(() -> new Staff("Ironwood Quarterstaff", "A dense quarterstaff carved from aged ironwood, balanced for spell-casting and blunt persuasion.", Quality.UNCOMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 20),
            new ItemEffect(ItemEffectType.DAMAGE, 1.08)
    ))),
    UNCOMMON_EMBER_STAFF(() -> new Staff("Ember Staff", "Smolders with persistent heat, useful for campfires and scorching the undead.", Quality.UNCOMMON, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 22),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.10)
    ))),
    UNCOMMON_DAWN_SCEPTRE(() -> new Staff("Dawnlight Sceptre", "Channeled with morning light. Its radiance makes undead fiends hesitate.", Quality.UNCOMMON, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 18),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.10),
            new ItemEffect(ItemEffectType.MAX_MANA, 25)
    ))),
    UNCOMMON_BONE_WAND(() -> new Staff("Bone Wand", "Carved from weathered graveyard bones, humming with a lingering necrotic chill.", Quality.UNCOMMON, MagicType.NECROTIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 24),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 5)
    ))),

    UNCOMMON_CHAINMAIL(() -> new Armor("Reinforced Chainmail", "Interlocking steel rings backed by padded gambeson. Reliable against slicing weapons.", Quality.UNCOMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 22),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 30),
            new ItemEffect(ItemEffectType.RESISTANCE_KINETIC, 1.15)
    ))),
    UNCOMMON_GRAVE_SHROUD(() -> new Armor("Gravekeeper Shroud", "Treated fabric steeped in preservative oils, insulating the wearer from decay.", Quality.UNCOMMON, MagicType.NECROTIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 18),
            new ItemEffect(ItemEffectType.RESISTANCE_NECROTIC, 1.25),
            new ItemEffect(ItemEffectType.MAX_MANA, 20)
    ))),
    UNCOMMON_SALAMANDER_VEST(() -> new Armor("Salamander Hide Vest", "Tough, heat-resistant scales harvested from fiery swamp lizards.", Quality.UNCOMMON, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 20),
            new ItemEffect(ItemEffectType.RESISTANCE_ELEMENTAL, 1.20),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 25)
    ))),

    UNCOMMON_RUBY_PENDANT(() -> new Artifact("Ruby Heart Pendant", "Warm red gem that quickens the heartbeat and lends extra force to aggressive spells.", Quality.UNCOMMON, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.MAX_HEALTH, 45),
            new ItemEffect(ItemEffectType.ATTACK, 20),
            new ItemEffect(ItemEffectType.DAMAGE, 1.08)
    ))),
    UNCOMMON_STEEL_BANGLE(() -> new Artifact("Hardened Steel Bangle", "A heavy bracer that shields the forearm and helps turn aside physical strikes.", Quality.UNCOMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 22),
            new ItemEffect(ItemEffectType.RESISTANCE_KINETIC, 1.15)
    ))),
    UNCOMMON_PURITY_MEDALLION(() -> new Artifact("Silver Sun Medallion", "Engraved with a solar crest, providing sacred warding against rotting necrotic curses.", Quality.UNCOMMON, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 18),
            new ItemEffect(ItemEffectType.RESISTANCE_NECROTIC, 1.25),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.10)
    ))),
    UNCOMMON_ZEPHYR_CHARM(() -> new Artifact("Zephyr Charm", "A smooth talisman infused with hill winds, giving an agile edge in combat.", Quality.UNCOMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.SPEED, 30),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 8),
            new ItemEffect(ItemEffectType.ATTACK, 16)
    ))),
    UNCOMMON_WARDING_TALISMAN(() -> new Artifact("Warding Talisman", "Etched with protective defensive runes that help blunt hard incoming blows.", Quality.UNCOMMON, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 20),
            new ItemEffect(ItemEffectType.MAX_MANA, 35),
            new ItemEffect(ItemEffectType.RESISTANCE_KINETIC, 1.15)
    ))),

    UNCOMMON_HEALTH_POTION(() -> new Potion("Health Potion", "A standard crimson brew widely favored by adventurers for closing moderate battle wounds.", Quality.UNCOMMON, MagicType.KINETIC, List.of(
            new CombatEffect(CombatEffectType.HEALING, 60, 0)
    ))),
    UNCOMMON_STRENGTH_POTION(() -> new Potion("Brawny Brew", "A spicy elixir that temporarily tightens sinew and muscle, boosting physical output.", Quality.UNCOMMON, MagicType.KINETIC, List.of(
            new CombatEffect(CombatEffectType.STRENGTH, 1, 3)
    ))),


    // ==========================================
    // RARE (Difficulty INTERMEDIATE - Level 25)
    // Targets: Fire Sprite, Giant Spider, Ork, Skeleton
    // ==========================================
    RARE_BLAZING_STAFF(() -> new Staff("Blazing Firestaff", "A ruby staff head that channels surging flames, illuminating dark caverns with fierce heat.", Quality.RARE, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 30),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.15),
            new ItemEffect(ItemEffectType.DAMAGE, 1.10)
    ))),
    RARE_ABYSS_ROD(() -> new Staff("Deep Trench Rod", "Drawn from dark oceanic depths, casting damp abyssal magic with heavy pressure.", Quality.RARE, MagicType.ABYSSAL, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 32),
            new ItemEffect(ItemEffectType.MAX_MANA, 40)
    ))),
    RARE_RADIANT_STAFF(() -> new Staff("Radiant Lightstaff", "A polished ivory staff that casts blinding beams of sacred purity.", Quality.RARE, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 28),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.15),
            new ItemEffect(ItemEffectType.MAX_MANA, 35)
    ))),
    RARE_WAR_PIKE(() -> new Staff("Ironclad War Staff", "Reinforced with cold-forged iron bands, delivering crushing kinetic force.", Quality.RARE, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 35),
            new ItemEffect(ItemEffectType.DAMAGE, 1.12)
    ))),

    RARE_KNIGHT_PLATE(() -> new Armor("Knight's Steelplate", "Solid plate armor fitted by veteran smiths. Turns sharp blade thrusts into hollow clinks.", Quality.RARE, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 32),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 50),
            new ItemEffect(ItemEffectType.RESISTANCE_KINETIC, 1.20)
    ))),
    RARE_MAGMA_ROBE(() -> new Armor("Magma Silk Robe", "Woven from geothermal mineral fibers, dissipating intense scorching temperatures.", Quality.RARE, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 26),
            new ItemEffect(ItemEffectType.RESISTANCE_ELEMENTAL, 1.30),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 40)
    ))),
    RARE_CHITIN_ARMOR(() -> new Armor("Abyssal Chitin Carapace", "Exoskeleton plating harvested from deep-sea beasts, impervious to corrosive brines.", Quality.RARE, MagicType.ABYSSAL, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 28),
            new ItemEffect(ItemEffectType.RESISTANCE_ABYSSAL, 1.25),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 45)
    ))),

    RARE_BERSERKER_CREST(() -> new Artifact("Berserker's Crest", "An aggressive sigil that fuels adrenaline, boosting attack and landing critical strikes.", Quality.RARE, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 34),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 12),
            new ItemEffect(ItemEffectType.DAMAGE, 1.15)
    ))),
    RARE_AEGIS_PENDANT(() -> new Artifact("Aegis Pendant", "A sturdy sacred medallion that reinforces stamina and deflects elemental breath.", Quality.RARE, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 32),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 75),
            new ItemEffect(ItemEffectType.RESISTANCE_ELEMENTAL, 1.25)
    ))),
    RARE_ELEMENTAL_CORE(() -> new Artifact("Elemental Heartstone", "A crystallized elemental spark that resonates deeply with pyromancy and elemental arts.", Quality.RARE, MagicType.ELEMENTAL, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 30),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.18),
            new ItemEffect(ItemEffectType.RESISTANCE_ELEMENTAL, 1.30)
    ))),
    RARE_SOUL_GEM(() -> new Artifact("Gravebinder Gem", "Traps lingering graveyard miasma, shielding the bearer from rotting necrotic forces.", Quality.RARE, MagicType.NECROTIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 30),
            new ItemEffect(ItemEffectType.MAX_MANA, 50),
            new ItemEffect(ItemEffectType.RESISTANCE_NECROTIC, 1.35)
    ))),
    RARE_QUICKSILVER_TALISMAN(() -> new Artifact("Quicksilver Talisman", "Contains liquid mercury that flows counter to momentum, granting rapid reflexes.", Quality.RARE, MagicType.KINETIC, List.of(
            new ItemEffect(ItemEffectType.SPEED, 45),
            new ItemEffect(ItemEffectType.ATTACK, 30),
            new ItemEffect(ItemEffectType.DAMAGE, 1.10)
    ))),

    RARE_GREATER_HEALTH_POTION(() -> new Potion("Greater Health Flask", "A potent crimson distillation that quickly sets fractured bones and mends heavy flesh wounds.", Quality.RARE, MagicType.KINETIC, List.of(
            new CombatEffect(CombatEffectType.HEALING, 120, 0)
    ))),
    RARE_STONEFORM_ELIXIR(() -> new Potion("Liquid Granite", "Hardens skin into a stone-like texture, sharply dampening incoming enemy attacks.", Quality.RARE, MagicType.KINETIC, List.of(
            new CombatEffect(CombatEffectType.RESISTANCE, 2, 4)
    ))),


    // ==========================================
    // EPIC (Difficulty MEDIUM - Level 35)
    // Arcane & Chaos introduced here; Dragonic, Purity, Abyssal
    // ==========================================
    EPIC_ARCHON_SCEPTER(() -> new Staff("Celestial Archon Scepter", "Consecrated in high sanctuaries, releasing focused radiant purity that dispels corruption.", Quality.EPIC, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 40),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.18),
            new ItemEffect(ItemEffectType.MAX_MANA, 50)
    ))),
    EPIC_NETHER_STAFF(() -> new Staff("Netherflame Staff", "Infused with brimstone and deep underworld embers, channeling caustic abyssal magic.", Quality.EPIC, MagicType.ABYSSAL, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 44),
            new ItemEffect(ItemEffectType.DAMAGE, 1.15)
    ))),
    EPIC_WYRMCLAW_ROD(() -> new Staff("Wyrmclaw Rod", "Crafted around a drake's razor talon, pulsing with ancient dragonic vitality.", Quality.EPIC, MagicType.DRAGONIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 42),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 10),
            new ItemEffect(ItemEffectType.DAMAGE, 1.12)
    ))),
    EPIC_WARPING_CADUCEUS(() -> new Staff("Warping Caduceus", "Twisted twin serpents that warp probability and project erratic waves of chaos.", Quality.EPIC, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 45),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.20)
    ))),
    EPIC_ASTRAL_PILLAR(() -> new Staff("Astral Prism Staff", "Cut from crystal infused with starlight, focusing pure arcane energy with piercing acuity.", Quality.EPIC, MagicType.ARCANE, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 42),
            new ItemEffect(ItemEffectType.DAMAGE, 1.15),
            new ItemEffect(ItemEffectType.MAX_MANA, 60)
    ))),

    EPIC_WYRMSCALE_MAIL(() -> new Armor("Ancient Wyrmscale Mail", "Gleaming drake scales that have weathered generations of warfare and dragon breath.", Quality.EPIC, MagicType.DRAGONIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 40),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 75),
            new ItemEffect(ItemEffectType.RESISTANCE_DRAGONIC, 1.35)
    ))),
    EPIC_VOID_SHROUD(() -> new Armor("Null-Void Shroud", "Dark ceremonial cloth that absorbs dark hexes and dampens abyssal pressure.", Quality.EPIC, MagicType.ABYSSAL, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 36),
            new ItemEffect(ItemEffectType.RESISTANCE_ABYSSAL, 1.35),
            new ItemEffect(ItemEffectType.RESISTANCE_NECROTIC, 1.30)
    ))),
    EPIC_SANCTIFIED_CUIRASS(() -> new Armor("Sanctified Aegis Cuirass", "Burnished mirror-plate armor blessed by cardinals, reflecting sinister magic back onto attackers.", Quality.EPIC, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 42),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 80),
            new ItemEffect(ItemEffectType.RESISTANCE_PURITY, 1.35)
    ))),
    EPIC_CHAOS_MANTLE(() -> new Armor("Entropic Mantle", "Shimmering silk that blurs the wearer's outline, scattering chaotic disturbances.", Quality.EPIC, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 38),
            new ItemEffect(ItemEffectType.RESISTANCE_CHAOS, 1.35),
            new ItemEffect(ItemEffectType.MAX_MANA, 50)
    ))),

    EPIC_DRAGON_EYE(() -> new Artifact("Eye of the Wyrm", "An amber gemstone possessing the predatory focus and devastating ferocity of a dragon.", Quality.EPIC, MagicType.DRAGONIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 45),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 16),
            new ItemEffect(ItemEffectType.DAMAGE, 1.20)
    ))),
    EPIC_NETHER_BULWARK_HEART(() -> new Artifact("Abyssal Bulwark Heart", "A fossilized abyssal core that thickens personal wards against dark strikes.", Quality.EPIC, MagicType.ABYSSAL, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 44),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 120),
            new ItemEffect(ItemEffectType.RESISTANCE_ABYSSAL, 1.40)
    ))),
    EPIC_SERAPH_FEATHER(() -> new Artifact("Seraph's Pristine Quill", "A brilliant plume dropped by an angelic messenger, warding off deathly corruption.", Quality.EPIC, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.20),
            new ItemEffect(ItemEffectType.RESISTANCE_PURITY, 1.40),
            new ItemEffect(ItemEffectType.RESISTANCE_NECROTIC, 1.35)
    ))),
    EPIC_RIFT_HOURGLASS(() -> new Artifact("Hourglass of the Rift", "Contains shimmering sands that bend localized time, granting faster incantations and strikes.", Quality.EPIC, MagicType.ARCANE, List.of(
            new ItemEffect(ItemEffectType.SPEED, 65),
            new ItemEffect(ItemEffectType.ATTACK, 38),
            new ItemEffect(ItemEffectType.MANA_REGENERATION, 5),
            new ItemEffect(ItemEffectType.DAMAGE, 1.15)
    ))),
    EPIC_BLOODSTONE_CHOKER(() -> new Artifact("Bloodstone Choker", "Channels deep regenerative warmth directly into the veins, sustaining physical vitality.", Quality.EPIC, MagicType.ABYSSAL, List.of(
            new ItemEffect(ItemEffectType.MAX_HEALTH, 140),
            new ItemEffect(ItemEffectType.DEFENSE, 40),
            new ItemEffect(ItemEffectType.REGENERATION, 6)
    ))),
    EPIC_CHAOS_EMBLEM(() -> new Artifact("Fractured Prism", "A prism vibrating with entropy, amplifying erratic chaotic spell output.", Quality.EPIC, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 42),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.25),
            new ItemEffect(ItemEffectType.RESISTANCE_CHAOS, 1.35)
    ))),

    EPIC_SUPERIOR_HEALTH_POTION(() -> new Potion("Superior Rejuvenation Brew", "A shimmering restorative draft brewed from sanctified herbs, mending severe bodily injuries.", Quality.EPIC, MagicType.PURITY, List.of(
            new CombatEffect(CombatEffectType.HEALING, 200, 0),
            new CombatEffect(CombatEffectType.REGENERATION, 20, 3)
    ))),
    EPIC_CLEANSING_ELIXIR(() -> new Potion("Miracle Cleansing Tonic", "An immaculate solution that purges foul hexes and normalizes disrupted combat abilities.", Quality.EPIC, MagicType.PURITY, List.of(
            new CombatEffect(CombatEffectType.CLEANSE, 0, 1)
    ))),


    // ==========================================
    // LEGENDARY (Difficulty HIGH - Level 45)
    // Master Arcane, Chaos, and Dragonic artifacts
    // ==========================================
    LEGENDARY_ASTRAL_STAFF(() -> new Staff("Staff of the Cosmos", "Starlight gathers around its crystalline tip, bending ancient constellations to the wielder's command.", Quality.LEGENDARY, MagicType.ARCANE, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 55),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.25),
            new ItemEffect(ItemEffectType.DAMAGE, 1.20),
            new ItemEffect(ItemEffectType.MAX_MANA, 80)
    ))),
    LEGENDARY_CHAOS_HERALD(() -> new Staff("Entropy's Herald", "A twisting staff forged from broken laws of physics, discharging cascading cataclysms of chaos.", Quality.LEGENDARY, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 60),
            new ItemEffect(ItemEffectType.DAMAGE, 1.25),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 15)
    ))),
    LEGENDARY_SUNWYRM_IGNITION(() -> new Staff("Sunwyrm's Ignition", "Contains the concentrated fiery core of an elder wyvern, incinerating enemy bulwarks.", Quality.LEGENDARY, MagicType.DRAGONIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 58),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.22),
            new ItemEffect(ItemEffectType.DAMAGE, 1.20)
    ))),
    LEGENDARY_ABYSSAL_SOVEREIGN(() -> new Staff("Abyssal Sovereign Scepter", "Dredged from the blackest oceanic rift, wielding the crushing pressure of the depths.", Quality.LEGENDARY, MagicType.ABYSSAL, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 56),
            new ItemEffect(ItemEffectType.MAX_MANA, 90),
            new ItemEffect(ItemEffectType.DAMAGE, 1.18)
    ))),

    LEGENDARY_GRAND_MAGUS_ROBE(() -> new Armor("Robes of the Grand Magus", "Woven with intricate ley-threads that disperse hostile arcane energy harmlessly into the ether.", Quality.LEGENDARY, MagicType.ARCANE, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 50),
            new ItemEffect(ItemEffectType.RESISTANCE_ARCANE, 1.45),
            new ItemEffect(ItemEffectType.MAX_MANA, 75)
    ))),
    LEGENDARY_CHAOTIC_WARP_ARMOR(() -> new Armor("Chaotic Warp Armor", "Constantly phases between dimensional folds, causing enemy blows to glance into empty space.", Quality.LEGENDARY, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 52),
            new ItemEffect(ItemEffectType.RESISTANCE_CHAOS, 1.45),
            new ItemEffect(ItemEffectType.SPEED, 25)
    ))),
    LEGENDARY_TITAN_BULWARK(() -> new Armor("Dread Titan Bulwark", "Formed from molten dragon ore, capable of withstanding siege-level kinetic impacts.", Quality.LEGENDARY, MagicType.DRAGONIC, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 58),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 120),
            new ItemEffect(ItemEffectType.RESISTANCE_DRAGONIC, 1.45)
    ))),

    LEGENDARY_VOID_SINGULARITY_HEART(() -> new Artifact("Heart of the Void Singularity", "An entropic fragment of collapsed space that concentrates destructive power into every spell.", Quality.LEGENDARY, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 58),
            new ItemEffect(ItemEffectType.DAMAGE, 1.30),
            new ItemEffect(ItemEffectType.RESISTANCE_CHAOS, 1.50)
    ))),
    LEGENDARY_ASTRAL_COMPASS(() -> new Artifact("Astral Guiding Compass", "Calibrates the caster's flow with the great celestial grid, empowering arcane magic.", Quality.LEGENDARY, MagicType.ARCANE, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 52),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.30),
            new ItemEffect(ItemEffectType.RESISTANCE_ARCANE, 1.50)
    ))),
    LEGENDARY_DRAKE_MOTHER_HEART(() -> new Artifact("Heart of the Drake Mother", "Radiates the primordial vitality of the first brood, granting immense endurance.", Quality.LEGENDARY, MagicType.DRAGONIC, List.of(
            new ItemEffect(ItemEffectType.MAX_HEALTH, 180),
            new ItemEffect(ItemEffectType.DEFENSE, 56),
            new ItemEffect(ItemEffectType.RESISTANCE_DRAGONIC, 1.50)
    ))),
    LEGENDARY_CROWN_OF_MIGHT(() -> new Artifact("Crown of Endless Might", "A diademed relic of conquering kings, inspiring precision and martial supremacy.", Quality.LEGENDARY, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 54),
            new ItemEffect(ItemEffectType.DEFENSE, 52),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 20)
    ))),
    LEGENDARY_PHASE_SHIFT_TALISMAN(() -> new Artifact("Phase-Shift Talisman", "Permits instantaneous spatial micro-shifts, out-pacing hostile projectile strikes.", Quality.LEGENDARY, MagicType.ARCANE, List.of(
            new ItemEffect(ItemEffectType.SPEED, 100),
            new ItemEffect(ItemEffectType.DEFENSE, 50),
            new ItemEffect(ItemEffectType.DAMAGE, 1.25)
    ))),

    LEGENDARY_ARCH_SAGE_ELIXIR(() -> new Potion("Panacea of the Arch-Sage", "Refined over centuries by arch-mages, granting total restoration and spell focus.", Quality.LEGENDARY, MagicType.ARCANE, List.of(
            new CombatEffect(CombatEffectType.HEALING, 300, 0),
            new CombatEffect(CombatEffectType.FOCUS, 1, 5)
    ))),
    LEGENDARY_AEGIS_ELIXIR(() -> new Potion("Adamantine Draft", "Temporarily hardens the bearer's constitution to diamond-like resilience while heightening critical focus.", Quality.LEGENDARY, MagicType.PURITY, List.of(
            new CombatEffect(CombatEffectType.RESISTANCE, 3, 3),
            new CombatEffect(CombatEffectType.CRITICAL_FOCUS, 2, 3)
    ))),


    // ==========================================
    // MYTHIC (Difficulty EXTREME - Level 65)
    // Target: Infinity Dragon (Infinity is exclusive to Mythic)
    // ==========================================
    MYTHIC_INFINITY_STAFF(() -> new Staff("Ouroboros - Staff of Eternity", "The beginning and the end intertwined. A cosmic conduit capable of channeling pure infinity magic.", Quality.MYTHIC, MagicType.INFINITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 80),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.35),
            new ItemEffect(ItemEffectType.DAMAGE, 1.30),
            new ItemEffect(ItemEffectType.MAX_MANA, 120)
    ))),
    MYTHIC_RAGNAROK_SCEPTER(() -> new Staff("Ragnarok Scepter", "A staff born from cosmic twilight, commanding unbridled primordial chaos to unseat gods.", Quality.MYTHIC, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 85),
            new ItemEffect(ItemEffectType.DAMAGE, 1.35),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 20)
    ))),
    MYTHIC_GENESIS_LANCE(() -> new Staff("Genesis - Spark of Creation", "A blazing sacred lance of divine origin that purges corruption with blinding light.", Quality.MYTHIC, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 78),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.35),
            new ItemEffect(ItemEffectType.RESISTANCE_CHAOS, 1.40)
    ))),

    MYTHIC_ETERNAL_VESTMENT(() -> new Armor("Vestment of the Eternal Weaver", "Spun from the very fabric of reality, impervious to earthly blade and element alike.", Quality.MYTHIC, MagicType.INFINITY, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 72),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 180),
            new ItemEffect(ItemEffectType.SPEED, 40),
            new ItemEffect(ItemEffectType.REGENERATION, 10)
    ))),
    MYTHIC_COSMIC_AEGIS(() -> new Armor("Cosmic Bastion Cuirass", "Forged in the dense furnace of collapsed stars, absorbing catastrophic kinetic impacts.", Quality.MYTHIC, MagicType.CHAOS, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 78),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 200),
            new ItemEffect(ItemEffectType.RESISTANCE_CHAOS, 1.50)
    ))),

    MYTHIC_INFINITY_ORB(() -> new Artifact("Orb of the Endless Cycle", "Resonates with infinite cyclical energy, multiplying destructive magic to monumental heights.", Quality.MYTHIC, MagicType.INFINITY, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 76),
            new ItemEffect(ItemEffectType.DAMAGE, 1.45),
            new ItemEffect(ItemEffectType.OWN_MAGIC, 1.45)
    ))),
    MYTHIC_AEON_PRISM(() -> new Artifact("Prism of Unbroken Time", "Refracts hostile chronomancy, granting supreme defensive durability and fluid speed.", Quality.MYTHIC, MagicType.INFINITY, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 72),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 250),
            new ItemEffect(ItemEffectType.SPEED, 110)
    ))),
    MYTHIC_WORLD_RENDER_HORN(() -> new Artifact("Horn of the World-Render", "A draconic relic capable of sundering the iron scales of ancient world-serpents.", Quality.MYTHIC, MagicType.DRAGONIC, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 74),
            new ItemEffect(ItemEffectType.CRIT_CHANCE, 25),
            new ItemEffect(ItemEffectType.DAMAGE, 1.35)
    ))),
    MYTHIC_DIVINE_HALO(() -> new Artifact("Halo of Omnipotence", "A sacred celestial coronet that repels corrupting chaotic onslaughts.", Quality.MYTHIC, MagicType.PURITY, List.of(
            new ItemEffect(ItemEffectType.DEFENSE, 68),
            new ItemEffect(ItemEffectType.MAX_HEALTH, 220),
            new ItemEffect(ItemEffectType.RESISTANCE_CHAOS, 1.60)
    ))),
    MYTHIC_SINGULARITY_SHARD(() -> new Artifact("Shard of the First Big Bang", "Radiates the primordial energy that sparked the universe into existence.", Quality.MYTHIC, MagicType.ARCANE, List.of(
            new ItemEffect(ItemEffectType.ATTACK, 75),
            new ItemEffect(ItemEffectType.SPEED, 120),
            new ItemEffect(ItemEffectType.MAX_MANA, 150)
    ))),

    MYTHIC_AMBROSIA(() -> new Potion("Ambrosia of the Cosmos", "Divine nectar of celestial beings. Restores health and mana while bestowing divine strength.", Quality.MYTHIC, MagicType.INFINITY, List.of(
            new CombatEffect(CombatEffectType.HEALING, 500, 0),
            new CombatEffect(CombatEffectType.MANA_RESTORE, 300, 0),
            new CombatEffect(CombatEffectType.STRENGTH, 3, 5)
    ))),

                // ==========================================
    // GENERATED SCROLLS (tools/generate_scroll_items.py)
    // ==========================================
    SCROLL_ABYSSAL_GRASP(() -> new Scroll(new AbyssalGrasp(), Quality.RARE)),
    SCROLL_ADRENALINE_RUSH(() -> new Scroll(new AdrenalineRush(), Quality.COMMON)),
    SCROLL_AETHER_STREAM(() -> new Scroll(new AetherStream(), Quality.EPIC)),
    SCROLL_ARCANE_CATACLYSM(() -> new Scroll(new ArcaneCataclysm(), Quality.EPIC)),
    SCROLL_AURA_OF_RENEWAL(() -> new Scroll(new AuraOfRenewal(), Quality.UNCOMMON)),
    SCROLL_BLOOD_OFFERING(() -> new Scroll(new BloodOffering(), Quality.RARE)),
    SCROLL_CHAIN_LIGHTNING(() -> new Scroll(new ChainLightning(), Quality.UNCOMMON)),
    SCROLL_CHAOS_VORTEX(() -> new Scroll(new ChaosVortex(), Quality.LEGENDARY)),
    SCROLL_CHRONO_SHIFT(() -> new Scroll(new ChronoShift(), Quality.EPIC)),
    SCROLL_CONCUSSIVE_BLOW(() -> new Scroll(new ConcussiveBlow(), Quality.COMMON)),
    SCROLL_COSMIC_OBLIVION(() -> new Scroll(new CosmicOblivion(), Quality.MYTHIC)),
    SCROLL_DEATHBLOOM(() -> new Scroll(new Deathbloom(), Quality.RARE)),
    SCROLL_DOOM_GRAVE(() -> new Scroll(new DoomGrave(), Quality.RARE)),
    SCROLL_DRACONIC_BREATH(() -> new Scroll(new DraconicBreath(), Quality.EPIC)),
    SCROLL_DRAGON_ROAR(() -> new Scroll(new DragonRoar(), Quality.EPIC)),
    SCROLL_DRAGONHEART_AWAKENING(() -> new Scroll(new DragonheartAwakening(), Quality.EPIC)),
    SCROLL_ENTROPY_OVERLOAD(() -> new Scroll(new EntropyOverload(), Quality.LEGENDARY)),
    SCROLL_ETERNAL_ASCENSION(() -> new Scroll(new EternalAscension(), Quality.MYTHIC)),
    SCROLL_FIREBALL(() -> new Scroll(new Fireball(), Quality.UNCOMMON)),
    SCROLL_FLURRY_OF_BLOWS(() -> new Scroll(new FlurryOfBlows(), Quality.COMMON)),
    SCROLL_FROST_NOVA(() -> new Scroll(new FrostNova(), Quality.UNCOMMON)),
    SCROLL_GLACIAL_SPIKE(() -> new Scroll(new GlacialSpike(), Quality.UNCOMMON)),
    SCROLL_HARD_SHOVE(() -> new Scroll(new HardShove(), Quality.COMMON)),
    SCROLL_HEAVY_SHIELD_BASH(() -> new Scroll(new HeavyShieldBash(), Quality.COMMON)),
    SCROLL_HOLY_CLEANSING(() -> new Scroll(new HolyCleansing(), Quality.UNCOMMON)),
    SCROLL_INFERNO_BURST(() -> new Scroll(new InfernoBurst(), Quality.UNCOMMON)),
    SCROLL_INFINITY_BEAM(() -> new Scroll(new InfinityBeam(), Quality.MYTHIC)),
    SCROLL_IRON_FORTRESS(() -> new Scroll(new IronFortress(), Quality.COMMON)),
    SCROLL_JUDGMENT_GLARES(() -> new Scroll(new JudgmentGlares(), Quality.UNCOMMON)),
    SCROLL_LIGHT_RAY(() -> new Scroll(new LightRay(), Quality.UNCOMMON)),
    SCROLL_MADDENING_ABYSS(() -> new Scroll(new MaddeningAbyss(), Quality.RARE)),
    SCROLL_MIND_SHATTER(() -> new Scroll(new MindShatter(), Quality.EPIC)),
    SCROLL_MOLTEN_SURGE(() -> new Scroll(new MoltenSurge(), Quality.UNCOMMON)),
    SCROLL_PANDEMONIUM_HEX(() -> new Scroll(new PandemoniumHex(), Quality.LEGENDARY)),
    SCROLL_PLAGUE_HEX(() -> new Scroll(new PlagueHex(), Quality.RARE)),
    SCROLL_RADIANT_SMITE(() -> new Scroll(new RadiantSmite(), Quality.UNCOMMON)),
    SCROLL_REST(() -> new Scroll(new Rest(), Quality.COMMON)),
    SCROLL_SANCTUARY(() -> new Scroll(new Sanctuary(), Quality.UNCOMMON)),
    SCROLL_SEISMIC_STOMP(() -> new Scroll(new SeismicStomp(), Quality.COMMON)),
    SCROLL_SHADOW_SIPHON(() -> new Scroll(new ShadowSiphon(), Quality.RARE)),
    SCROLL_SHATTER_STRIKE(() -> new Scroll(new ShatterStrike(), Quality.COMMON)),
    SCROLL_SHOCKWAVE_LANCE(() -> new Scroll(new ShockwaveLance(), Quality.COMMON)),
    SCROLL_SINGULARITY(() -> new Scroll(new Singularity(), Quality.MYTHIC)),
    SCROLL_THUNDER_SPARK(() -> new Scroll(new ThunderSpark(), Quality.UNCOMMON)),
    SCROLL_TITAN_GRIT(() -> new Scroll(new TitanGrit(), Quality.COMMON)),
    SCROLL_VOID_CURSE(() -> new Scroll(new VoidCurse(), Quality.RARE)),
    SCROLL_WARP_DELIRIUM(() -> new Scroll(new WarpDelirium(), Quality.LEGENDARY)),
    SCROLL_WYRM_REND(() -> new Scroll(new WyrmRend(), Quality.EPIC));
    // END GENERATED SCROLLS

    private final Supplier<Item> supplier;

    ItemType(Supplier<Item> supplier) {
        this.supplier = supplier;
    }

    public Item create() {
        return supplier.get();
    }

    public static ItemType getItemTypeByQuality(Quality quality) {
        List<ItemType> valids = switch (quality) {
            case COMMON -> List.of(
                    COMMON_STAFF, COMMON_HEAVY_STICK, COMMON_FIRE_WAND, COMMON_PURITY_ROD,
                    COMMON_ARMOUR, COMMON_ELEMENTAL_ROBE, COMMON_PURITY_CLOAK,
                    COMMON_HEALTH_AMULET, COMMON_WARRIOR_RING, COMMON_GUARD_CHARM, COMMON_PRAYER_BEADS, COMMON_SWIFT_FEATHER,
                    COMMON_HEALTH_POTION, COMMON_MANA_POTION,
                    // SCROLLS_START
                    SCROLL_ADRENALINE_RUSH,
                    SCROLL_CONCUSSIVE_BLOW,
                    SCROLL_FLURRY_OF_BLOWS,
                    SCROLL_HARD_SHOVE,
                    SCROLL_HEAVY_SHIELD_BASH,
                    SCROLL_IRON_FORTRESS,
                    SCROLL_REST,
                    SCROLL_SEISMIC_STOMP,
                    SCROLL_SHATTER_STRIKE,
                    SCROLL_SHOCKWAVE_LANCE,
                    SCROLL_TITAN_GRIT
                    // SCROLLS_END
            );
            case UNCOMMON -> List.of(
                    UNCOMMON_KINETIC_STAFF, UNCOMMON_EMBER_STAFF, UNCOMMON_DAWN_SCEPTRE, UNCOMMON_BONE_WAND,
                    UNCOMMON_CHAINMAIL, UNCOMMON_GRAVE_SHROUD, UNCOMMON_SALAMANDER_VEST,
                    UNCOMMON_RUBY_PENDANT, UNCOMMON_STEEL_BANGLE, UNCOMMON_PURITY_MEDALLION, UNCOMMON_ZEPHYR_CHARM, UNCOMMON_WARDING_TALISMAN,
                    UNCOMMON_HEALTH_POTION, UNCOMMON_STRENGTH_POTION,
                    // SCROLLS_START
                    SCROLL_AURA_OF_RENEWAL,
                    SCROLL_CHAIN_LIGHTNING,
                    SCROLL_FIREBALL,
                    SCROLL_FROST_NOVA,
                    SCROLL_GLACIAL_SPIKE,
                    SCROLL_HOLY_CLEANSING,
                    SCROLL_INFERNO_BURST,
                    SCROLL_JUDGMENT_GLARES,
                    SCROLL_LIGHT_RAY,
                    SCROLL_MOLTEN_SURGE,
                    SCROLL_RADIANT_SMITE,
                    SCROLL_SANCTUARY,
                    SCROLL_THUNDER_SPARK
                    // SCROLLS_END
            );
            case RARE -> List.of(
                    RARE_BLAZING_STAFF, RARE_ABYSS_ROD, RARE_RADIANT_STAFF, RARE_WAR_PIKE,
                    RARE_KNIGHT_PLATE, RARE_MAGMA_ROBE, RARE_CHITIN_ARMOR,
                    RARE_BERSERKER_CREST, RARE_AEGIS_PENDANT, RARE_ELEMENTAL_CORE, RARE_SOUL_GEM, RARE_QUICKSILVER_TALISMAN,
                    RARE_GREATER_HEALTH_POTION, RARE_STONEFORM_ELIXIR,
                    // SCROLLS_START
                    SCROLL_ABYSSAL_GRASP,
                    SCROLL_BLOOD_OFFERING,
                    SCROLL_DEATHBLOOM,
                    SCROLL_DOOM_GRAVE,
                    SCROLL_MADDENING_ABYSS,
                    SCROLL_PLAGUE_HEX,
                    SCROLL_SHADOW_SIPHON,
                    SCROLL_VOID_CURSE
                    // SCROLLS_END
            );
            case EPIC -> List.of(
                    EPIC_ARCHON_SCEPTER, EPIC_NETHER_STAFF, EPIC_WYRMCLAW_ROD, EPIC_WARPING_CADUCEUS, EPIC_ASTRAL_PILLAR,
                    EPIC_WYRMSCALE_MAIL, EPIC_VOID_SHROUD, EPIC_SANCTIFIED_CUIRASS, EPIC_CHAOS_MANTLE,
                    EPIC_DRAGON_EYE, EPIC_NETHER_BULWARK_HEART, EPIC_SERAPH_FEATHER, EPIC_RIFT_HOURGLASS, EPIC_BLOODSTONE_CHOKER, EPIC_CHAOS_EMBLEM,
                    EPIC_SUPERIOR_HEALTH_POTION, EPIC_CLEANSING_ELIXIR,
                    // SCROLLS_START
                    SCROLL_AETHER_STREAM,
                    SCROLL_ARCANE_CATACLYSM,
                    SCROLL_CHRONO_SHIFT,
                    SCROLL_DRACONIC_BREATH,
                    SCROLL_DRAGON_ROAR,
                    SCROLL_DRAGONHEART_AWAKENING,
                    SCROLL_MIND_SHATTER,
                    SCROLL_WYRM_REND
                    // SCROLLS_END
            );
            case LEGENDARY -> List.of(
                    LEGENDARY_ASTRAL_STAFF, LEGENDARY_CHAOS_HERALD, LEGENDARY_SUNWYRM_IGNITION, LEGENDARY_ABYSSAL_SOVEREIGN,
                    LEGENDARY_GRAND_MAGUS_ROBE, LEGENDARY_CHAOTIC_WARP_ARMOR, LEGENDARY_TITAN_BULWARK,
                    LEGENDARY_VOID_SINGULARITY_HEART, LEGENDARY_ASTRAL_COMPASS, LEGENDARY_DRAKE_MOTHER_HEART, LEGENDARY_CROWN_OF_MIGHT, LEGENDARY_PHASE_SHIFT_TALISMAN,
                    LEGENDARY_ARCH_SAGE_ELIXIR, LEGENDARY_AEGIS_ELIXIR,
                    // SCROLLS_START
                    SCROLL_CHAOS_VORTEX,
                    SCROLL_ENTROPY_OVERLOAD,
                    SCROLL_PANDEMONIUM_HEX,
                    SCROLL_WARP_DELIRIUM
                    // SCROLLS_END
            );
            case MYTHIC -> List.of(
                    MYTHIC_INFINITY_STAFF, MYTHIC_RAGNAROK_SCEPTER, MYTHIC_GENESIS_LANCE,
                    MYTHIC_ETERNAL_VESTMENT, MYTHIC_COSMIC_AEGIS,
                    MYTHIC_INFINITY_ORB, MYTHIC_AEON_PRISM, MYTHIC_WORLD_RENDER_HORN, MYTHIC_DIVINE_HALO, MYTHIC_SINGULARITY_SHARD,
                    MYTHIC_AMBROSIA,
                    // SCROLLS_START
                    SCROLL_COSMIC_OBLIVION,
                    SCROLL_ETERNAL_ASCENSION,
                    SCROLL_INFINITY_BEAM,
                    SCROLL_SINGULARITY
                    // SCROLLS_END
            );
        };
        int random = (int) (Math.random() * (valids.size()));
        return valids.get(random);
    }

    public static Item getItemByQuality(Quality quality) {
        ItemType type = getItemTypeByQuality(quality);
        return type != null ? type.create() : null;
    }

    public static Item getItemQuality(Quality quality) {
        return getItemByQuality(quality);
    }
}
