package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class MaddeningAbyss extends Spell {
    public MaddeningAbyss() {
        super("Maddening Abyss", "Exposes the enemy mind to eldritch horrors from the abyss. Deals 85 Abyssal damage, induces Confusion (50% self-harm chance for 3 rounds), and reduces enemy Attack (-2 for 2 rounds). Costs 60 Mana.", MagicType.ABYSSAL);
    }

    @Override
    public int getBaseDamage() {
        return 85;
    }

    @Override
    public int getManaCost() {
        return 60;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CONFUSED, 0.5, 3),
                new CombatEffect(CombatEffectType.WEAKNESS, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.5, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
