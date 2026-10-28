package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class EternalAscension extends Spell {
    public EternalAscension() {
        super("Eternal Ascension", "Attains cosmic ascension, transcending physical limits. Cleanses negative stat debuffs, restores 60 Health and 50 Mana immediately, and grants Regeneration (25 HP/round for 4 rounds), Mana Regeneration (+20/round for 4 rounds), and Focus (+3 for 4 rounds). Costs 140 Mana.", MagicType.INFINITY);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 140;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.HEALING, 60, 0),
                new CombatEffect(CombatEffectType.MANA_RESTORE, 50, 0),
                new CombatEffect(CombatEffectType.REGENERATION, 25, 4),
                new CombatEffect(CombatEffectType.MANA_REGEN, 20, 4),
                new CombatEffect(CombatEffectType.FOCUS, 3, 4),
                new CombatEffect(CombatEffectType.CLEANSE, 0, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 60, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_RESTORE, 50, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.REGENERATION, 25, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_REGEN, 20, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.FOCUS, 3, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CLEANSE, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buff.ska";
    }
}
