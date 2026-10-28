package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class CosmicOblivion extends Spell {
    public CosmicOblivion() {
        super("Cosmic Oblivion", "Unleashes the annihilating collapse of dying galaxies. Deals 160 Infinity damage, dispels enemy periodic buffs, curses the foe (draining 20% max HP and max Mana for 3 rounds), and leaves them Vulnerable (+3 Defense reduction for 4 rounds). Costs 220 Mana.", MagicType.INFINITY);
    }

    @Override
    public int getBaseDamage() {
        return 160;
    }

    @Override
    public int getManaCost() {
        return 220;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.Vulnerability, 3, 4),
                new CombatEffect(CombatEffectType.CURSED, 20, 3),
                new CombatEffect(CombatEffectType.DISPEL, 0, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 3, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 20, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
