package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.item.Quality;
import main.java.scene.LocationScene;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class RedwoodWoodlands extends LocationScene {
    private final Chest chest;
    private final NPC ranger;

    public RedwoodWoodlands(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("redwood_woodlands_chest_1", "Hollow Redwood Cache", Quality.RARE);

        ranger = new NPC("redwood_ranger", "Ranger Thalia", NPCType.ELF, this);
        ranger.addDialogItem(new DialogItem("Halt your pace, traveler. You step upon sacred moss."));
        ranger.addDialogItem(new DialogItem("The towering redwoods have seen empires rise and crumble to dust."));
        ranger.addDialogItem(new DialogItem("Take this Sparrow Feather — let your footfalls land softly on the forest floor.", () -> {
            gameContext.addItem(ItemType.COMMON_SWIFT_FEATHER.create());
        }));
        ranger.addInteractedDialog(new DialogItem("May the redwoods shelter you from tempest and malice."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, ranger);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Exowerin", "Leave Forest, back to Exowerin", Exowerin::new);
    }

    @Override
    public String getName() {
        return "Redwood Woodlands";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
