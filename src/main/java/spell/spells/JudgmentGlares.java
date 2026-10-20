package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class JudgmentGlares extends Spell {
    public JudgmentGlares() {
        super("Judgment Glares", "Pierces the target with blinding divine rays, stripping away all ongoing beneficial effects.", MagicType.PURITY);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(85, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
