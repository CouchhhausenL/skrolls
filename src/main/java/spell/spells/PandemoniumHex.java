package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class PandemoniumHex extends Spell {
    public PandemoniumHex() {
        super("Pandemonium Hex", "Unleashes mutating chaotic blight that poisons the enemy while eroding their maximum life and mana with a dark curse.", MagicType.CHAOS);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(125, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.POISONED, 20, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 15, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
