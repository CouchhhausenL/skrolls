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

public class TheHappyFlyPub extends LocationScene {
    private final Chest chest;
    private final NPC barkeep;

    public TheHappyFlyPub(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("happy_fly_chest_1", "Pub Cellar Cask", Quality.UNCOMMON);

        barkeep = new NPC("happy_fly_barkeep", "Barkeep Barnaby", NPCType.DWARF, this);
        barkeep.addDialogItem(new DialogItem("Welcome to The Happy Fly! Best mead and warm hearth this side of the mountains."));
        barkeep.addDialogItem(new DialogItem("You look weathered from the road, wizard. Here's a house draft on the house!", () -> {
            gameContext.addItem(ItemType.UNCOMMON_HEALTH_POTION.create());
        }));
        barkeep.addInteractedDialog(new DialogItem("Drink hearty and rest your bones before you head back into the cold!"));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, barkeep);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Gilded Nexus", GildedNexus::new);
        exits.addExit(Direction.WEST, "Gilded Approach", GildedApproach::new);
    }

    @Override
    public String getName() {
        return "The Happy Fly Pub";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }
}
