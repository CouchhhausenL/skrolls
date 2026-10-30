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

public class BrassMapleWood extends LocationScene {
    private final Chest chest;
    private final NPC scout;

    public BrassMapleWood(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("brass_maple_chest_1", "Mossy Maple Trunk Cache", Quality.UNCOMMON);

        scout = new NPC("brass_maple_scout", "Finn the Scout", NPCType.ELF, this);
        scout.addDialogItem(new DialogItem("Golden leaves fall like brass coins in this ancient wood."));
        scout.addDialogItem(new DialogItem("Lumberway is fraught with wandering beasts. Carry this Minor Health Potion with you.", () -> {
            gameContext.addItem(ItemType.COMMON_HEALTH_POTION.create());
        }));
        scout.addInteractedDialog(new DialogItem("Walk softly beneath the brass canopies."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, scout);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.WEST, "Lumberway", Lumberway::new);
        exits.addExit(Direction.EAST, "Lumberhighs Approach", LumberhighsApproach::new);
    }

    @Override
    public String getName() {
        return "Brass Maple Wood";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }
}
