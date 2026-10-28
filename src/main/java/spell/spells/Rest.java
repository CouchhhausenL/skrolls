package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class Rest extends Spell {
    public Rest() {
        super("Rest", "Takes a brief rest during battle to catch your breath and recover. Restores 20 Health. Costs 0 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.HEALING, 20, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 20, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
