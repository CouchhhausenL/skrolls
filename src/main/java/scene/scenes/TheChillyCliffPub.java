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

public class TheChillyCliffPub extends LocationScene {
    private final Chest chest;
    private final NPC climber;

    public TheChillyCliffPub(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("chilly_cliff_chest_1", "Cliffside Tavern Barrel", Quality.UNCOMMON);

        climber = new NPC("chilly_cliff_climber", "Wulf the Climber", NPCType.ADVENTURER, this);
        climber.addDialogItem(new DialogItem("Brrr! The cliff winds howl fierce outside tonight."));
        climber.addDialogItem(new DialogItem("Before you tackle the sheer cliff routes, drink this Brawny Brew. It'll stiffen your grip and sinew!", () -> {
            gameContext.addItem(ItemType.UNCOMMON_STRENGTH_POTION.create());
        }));
        climber.addInteractedDialog(new DialogItem("Watch your footing out on the ledges, traveler!"));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, climber);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.EAST, "Seaside Route", SeasideRoute::new);
        exits.addExit(Direction.NORTH_WEST, "WinterLane", WinterLane::new);
    }

    @Override
    public String getName() {
        return "The Chilly Cliff Pub";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }
}
