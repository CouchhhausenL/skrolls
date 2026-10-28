package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class Sanctuary extends Spell {
    public Sanctuary() {
        super("Sanctuary", "Creates a sacred haven of protection. Grants Regeneration (25 HP/round for 4 rounds), Mana Regeneration (+15/round for 4 rounds), Resistance (+2 Defense for 3 rounds), and dispels all enemy periodic buffs. Costs 45 Mana.", MagicType.PURITY);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 45;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.DISPEL, 0, 0)
        );
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.REGENERATION, 25, 4),
                new CombatEffect(CombatEffectType.MANA_REGEN, 15, 4),
                new CombatEffect(CombatEffectType.RESISTANCE, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.REGENERATION, 25, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_REGEN, 15, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 2, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buff.ska";
    }
}
