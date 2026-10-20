package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class VoidCurse extends Spell {
    public VoidCurse() {
        super("Void Curse", "Afflicts the foe with the deep vacuum of the void, withering their max health and rendering them vulnerable.", MagicType.ABYSSAL);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(60, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 10, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
