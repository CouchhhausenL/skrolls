package main.java.entity.monster;

import main.java.util.Converter;
import main.java.DifficultyLevel;
import main.java.MagicType;
import main.java.entity.Entity;
import main.java.item.Item;
import main.java.item.ItemType;

// A single monster instance. The same MagicType is used for both attacking
// and defending because monsters only have one affinity.
public class Monster extends Entity {
    private final String description;
    private final MagicType magicType;
    private final int attack;
    private final int defense;
    private final DifficultyLevel difficulty;
    private MonsterType monsterType;

    public Monster(String name, String description, int maxHealth, int speed, int attack, int defense, DifficultyLevel difficulty, MagicType magicType) {
        super(name, maxHealth, speed);
        this.description = description;
        this.magicType = magicType;
        this.attack = attack;
        this.defense = defense;
        this.difficulty = difficulty;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public int getLevel() {
        return difficulty.getLevel();
    }

    @Override
    public int getDefenseStat() {
        return defense;
    }

    @Override
    public int getAttackStat() {
        return attack;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    // Called by MonsterType.create() right after construction
    public void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    @Override
    public MagicType getDefendingMagicType() {
        return magicType;
    }

    @Override
    public MagicType getAttackingMagicType() {
        return magicType;
    }

    public Item getMobDrop() {
        return ItemType.getItemByQuality(Converter.DifficultyLevelToQuality(this.difficulty));
    }
}
