package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class IronFortress extends Spell {
    public IronFortress() {
        super("Iron Fortress", "Adopts an immovable fortress stance. Increases Resistance (+4 Defense for 3 rounds), cleanses negative stat stages, and restores 25 Health. Costs 25 Mana.", MagicType.KINETIC);
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
                new CombatEffect(CombatEffectType.RESISTANCE, 4, 3),
                new CombatEffect(CombatEffectType.CLEANSE, 0, 0),
                new CombatEffect(CombatEffectType.HEALING, 25, 0)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.RESISTANCE, 4, 3));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.CLEANSE, 0, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 25, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_buff.ska";
    }
}
