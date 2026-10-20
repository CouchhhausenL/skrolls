package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class IronFortress extends Spell {
    public IronFortress() {
        super("Iron Fortress", "Braces into an immovable kinetic posture, boosting resistance, cleansing debuffs, and healing minor wounds.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 4, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CLEANSE, 0, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 25, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "magic.ska";
    }
}
