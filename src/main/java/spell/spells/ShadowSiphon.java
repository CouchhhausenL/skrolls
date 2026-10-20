package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ShadowSiphon extends Spell {
    public ShadowSiphon() {
        super("Shadow Siphon", "Draws dark energy from the abyss, leeching life and restoring mana from the foe.", MagicType.ABYSSAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            int damage = battleContext.damageEnemy(80, this.magicType);
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, damage * 0.3, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.MANA_RESTORE, 25, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buble.ska";
    }
}
