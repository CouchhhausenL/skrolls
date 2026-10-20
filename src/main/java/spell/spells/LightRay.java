package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class LightRay extends Spell {
    public LightRay() {
        super("Light Ray", "A strong beam of Light that heals yourself", MagicType.PURITY);
    }
    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            int damage = battleContext.damageEnemy(70, MagicType.PURITY);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 30, 0));

        }
    }
    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
