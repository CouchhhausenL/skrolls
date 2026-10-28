package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class SeismicStomp extends Spell {
    public SeismicStomp() {
        super("Seismic Stomp", "Slams the ground with immense kinetic force, sending tremors through the foe. Deals 40 Kinetic damage, reduces enemy Speed (-2 for 3 rounds) and Attack (-2 for 2 rounds). Costs 15 Mana.", MagicType.KINETIC);
    }

    @Override
    public int getBaseDamage() {
        return 40;
    }

    @Override
    public int getManaCost() {
        return 15;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.SLOWNESS, 2, 3),
                new CombatEffect(CombatEffectType.WEAKNESS, 2, 2)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.SLOWNESS, 2, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 2));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_normal.ska";
    }
}
