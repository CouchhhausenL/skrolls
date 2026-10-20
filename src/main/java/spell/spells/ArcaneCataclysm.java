package main.java.spell.spells;

import main.java.MagicType;
import main.java.battle.BattleContext;
import main.java.game.GameContext;
import main.java.item.effects.CombatEffect;
import main.java.item.effects.CombatEffectType;
import main.java.spell.Spell;

public class ArcaneCataclysm extends Spell {
    public ArcaneCataclysm() {
        super("Arcane Cataclysm", "Detonates a dense nexus of pure arcane energy, stripping enemy enhancements and leaving them utterly vulnerable.", MagicType.ARCANE);
    }

    @Override
    public void cast(GameContext context) {
        if (context.getCurrentBattle() != null) {
            BattleContext battleContext = context.getCurrentBattle();
            battleContext.damageEnemy(130, this.magicType);
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.Vulnerability, 3, 3));
            battleContext.applyCombatEffectEnemy(new CombatEffect(CombatEffectType.DISPEL, 0, 0));
        }
    }

    @Override
    public String getAnimationName() {
        return "attack_hexagons.ska";
    }
}
