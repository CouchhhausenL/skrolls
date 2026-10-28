package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class ArcaneCataclysm extends Spell {
    public ArcaneCataclysm() {
        super("Arcane Cataclysm", "Detonates a dense nexus of pure arcane energy that shatters defensive magic. Deals 130 Arcane damage, dispels all enemy periodic buffs, and inflicts Vulnerability (+3 Defense reduction for 3 rounds). Costs 100 Mana.", MagicType.ARCANE);
    }

    @Override
    public int getBaseDamage() {
        return 130;
    }

    @Override
    public int getManaCost() {
        return 100;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.Vulnerability, 3, 3),
                new CombatEffect(CombatEffectType.DISPEL, 0, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 3, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
