package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class EternalAscension extends Spell {
    public EternalAscension() {
        super("Eternal Ascension", "Attains infinite oneness with the cosmos, instigating supreme regeneration, profound mana restoration, cleanse, and heightened spell focus.", MagicType.INFINITY);
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
        return "magic.ska";
    }
}
