package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class AdrenalineRush extends Spell {
    public AdrenalineRush() {
        super("Adrenaline Rush", "Surges your body with pure kinetic adrenaline. Increases Speed (+3 for 3 rounds), Mana Regeneration (+10/round for 3 rounds), and Focus (+1 for 3 rounds). Costs 10 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.SPEED, 3, 3),
                new CombatEffect(CombatEffectType.MANA_REGEN, 10, 3),
                new CombatEffect(CombatEffectType.FOCUS, 1, 3)
        );
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
        return "attack_buff.ska";
    }
}
