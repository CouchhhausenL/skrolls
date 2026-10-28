package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class DragonheartAwakening extends Spell {
    public DragonheartAwakening() {
        super("Dragonheart Awakening", "Awakens the ancient draconic bloodline within. Increases Attack (+3 for 4 rounds), Resistance (+3 Defense for 4 rounds), grants Regeneration (20 HP/round for 3 rounds), and increases Focus (+2 for 3 rounds). Costs 75 Mana.", MagicType.DRAGONIC);
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 75;
    }

    @Override
    public List<CombatEffect> getPlayerEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.STRENGTH, 3, 4),
                new CombatEffect(CombatEffectType.RESISTANCE, 3, 4),
                new CombatEffect(CombatEffectType.REGENERATION, 20, 3),
                new CombatEffect(CombatEffectType.FOCUS, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.STRENGTH, 3, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 3, 4));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.REGENERATION, 20, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.FOCUS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buff.ska";
    }
}
