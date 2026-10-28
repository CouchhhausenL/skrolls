package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

import java.util.List;

public class MindShatter extends Spell {
    public MindShatter() {
        super("Mind Shatter", "Fractures the foe's mental consciousness with shattering arcane glyphs. Deals 120 Arcane damage, induces severe Confusion (60% self-harm chance for 3 rounds), and reduces enemy Attack (-2 for 3 rounds). Costs 90 Mana.", MagicType.ARCANE);
    }

    @Override
    public int getBaseDamage() {
        return 120;
    }

    @Override
    public int getManaCost() {
        return 90;
    }

    @Override
    public List<CombatEffect> getEnemyEffects() {
        return List.of(
                new CombatEffect(CombatEffectType.CONFUSED, 0.6, 3),
                new CombatEffect(CombatEffectType.WEAKNESS, 2, 3)
        );
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(getBaseDamage(), this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.CONFUSED, 0.6, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.WEAKNESS, 2, 3));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
