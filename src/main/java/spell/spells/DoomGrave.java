package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class DoomGrave extends Spell {
    public DoomGrave() {
        super("Doom Grave", "Beckons the cold doom of the grave upon the foe. Deals 90 Necrotic damage, curses the target (draining 15% max HP and max Mana for 3 rounds), and slows them (-2 Speed for 3 rounds). Costs 60 Mana.", MagicType.NECROTIC);
    }

    @Override
    public int getBaseDamage() {
        return 90;
    }

    @Override
    public int getManaCost() {
        return 60;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CURSED, 15, 3),
                new CombatEffect(CombatEffectType.SLOWNESS, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CURSED, 15, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_death.ska";
    }
}
