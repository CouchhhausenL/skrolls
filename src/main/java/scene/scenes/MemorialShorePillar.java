package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.item.Quality;
import main.java.scene.Direction;
import main.java.scene.LocationScene;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class MemorialShorePillar extends LocationScene {
    private final Chest chest;
    private final NPC keeper;

    public MemorialShorePillar(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("memorial_pillar_chest_1", "Memorial Shore Urn", Quality.LEGENDARY);

        keeper = new NPC("memorial_pillar_keeper", "Keeper Orin", NPCType.ELF, this);
        keeper.addDialogItem(new DialogItem("You stand before the Memorial Shore Pillar, raised for heroes lost to the abyssal deep."));
        keeper.addDialogItem(new DialogItem("Only the most steadfast wizards journey this far along the treacherous coastal passages."));
        keeper.addDialogItem(new DialogItem("May the spirits of our forebears grant you agility and grace in the battles ahead.", () -> {
            gameContext.addItem(ItemType.LEGENDARY_PHASE_SHIFT_TALISMAN.create());
        }));
        keeper.addInteractedDialog(new DialogItem("The pillar stands as an eternal vigil over the crashing surf."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, keeper);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.EAST, "Little Passage", LittlePassage::new);
    }

    @Override
    public String getName() {
        return "Memorial Shore Pillar";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.HIGH;
    }
}
