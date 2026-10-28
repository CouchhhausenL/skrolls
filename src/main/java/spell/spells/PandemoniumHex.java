package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class PandemoniumHex extends Spell {
    public PandemoniumHex() {
        super("Pandemonium Hex", "Unleashes an unpredictable pandemic of chaotic corruption. Deals 125 Chaos damage, poisons the enemy (20% worsening damage/round for 4 rounds), and afflicts them with a dark Curse (drains 15% max HP and max Mana for 3 rounds). Costs 130 Mana.", MagicType.CHAOS);
    }

    @Override
    public int getBaseDamage() {
        return 125;
    }

    @Override
    public int getManaCost() {
        return 130;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.POISONED, 20, 4),
                new CombatEffect(CombatEffectType.CURSED, 15, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.POISONED, 20, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 15, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
