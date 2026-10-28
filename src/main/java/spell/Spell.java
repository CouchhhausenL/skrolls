package main.java.spell;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;

import java.util.List;

public abstract class Spell {
    private final String name;
    private final String description;
    protected final MagicType magicType;

    public Spell(String name, String description, MagicType magicType) {
        this.name = name;
        this.description = description;
        this.magicType = magicType;

    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void cast(GameContext context);

    public MagicType getMagicType() {
        return magicType;
    }

    public abstract String getAnimationName();
    public int getBaseDamage(){
        return 0;
    }
    public List<CombatEffect> getPlayerEffects(){
        return List.of();
    }
    public List<CombatEffect> getEnemyEffects(){
        return List.of();
    }
    public abstract int getManaCost();

}
