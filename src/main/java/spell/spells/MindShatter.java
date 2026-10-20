package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class MindShatter extends Spell {
    public MindShatter() {
        super("Mind Shatter", "Fractures the foe's mental consciousness with arcane glyphs, inducing heavy confusion and debilitating weakness.", MagicType.ARCANE);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(120, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.6, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
