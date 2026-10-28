package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class TitanGrit extends Spell {
    public TitanGrit() {
        super("Titan Grit", "Channels titanic physical fortitude through your muscles. Increases Attack (+3 for 3 rounds) and Resistance (+2 Defense for 3 rounds). Costs 25 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 25;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.STRENGTH, 3, 3),
                new CombatEffect(CombatEffectType.RESISTANCE, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 3, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buff.ska";
    }
}
