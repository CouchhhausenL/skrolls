package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class AdrenalineRush extends Spell {
    public AdrenalineRush() {
        super("Adrenaline Rush", "Surges your body with pure kinetic vitality, increasing speed, mana regeneration, and focus.", MagicType.KINETIC);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.SPEED, 3, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_REGEN, 10, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.FOCUS, 1, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "magic.ska";
    }
}
