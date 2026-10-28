package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class VoidCurse extends Spell {
    public VoidCurse() {
        super("Void Curse", "Afflicts the foe with the withering void of the deep abyss. Deals 60 Abyssal damage, curses the enemy (draining 10% max HP and max Mana for 4 rounds), and leaves them Vulnerable (+2 Defense reduction for 3 rounds). Costs 50 Mana.", MagicType.ABYSSAL);
    }

    @Override
    public int getBaseDamage() {
        return 60;
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CURSED, 10, 4),
                new CombatEffect(CombatEffectType.Vulnerability, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 10, 4));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
