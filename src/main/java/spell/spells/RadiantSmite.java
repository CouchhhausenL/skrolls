package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class RadiantSmite extends Spell {
    public RadiantSmite() {
        super("Radiant Smite", "Calls down holy retribution on the foe while blessing yourself. Deals 80 Purity damage, reduces enemy Attack (-2 for 3 rounds), dispels enemy periodic buffs, and restores 20 Health. Costs 40 Mana.", MagicType.PURITY);
    }

    @Override
    public int getBaseDamage() {
        return 80;
    }

    @Override
    public int getManaCost() {
        return 40;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.WEAKNESS, 2, 3),
                new CombatEffect(CombatEffectType.DISPEL, 0, 0)
        );
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
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
            battleContext.applyCombatEffectYourself(new CombatEffect(CombatEffectType.HEALING, 20, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_star.ska";
    }
}
