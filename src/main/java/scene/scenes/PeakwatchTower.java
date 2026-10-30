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

public class PeakwatchTower extends LocationScene {
    private final Chest chest;
    private final NPC lookout;

    public PeakwatchTower(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("peakwatch_chest_1", "Watchtower Supply Chest", Quality.UNCOMMON);

        lookout = new NPC("peakwatch_lookout", "Owen the Lookout", NPCType.ADVENTURER, this);
        lookout.addDialogItem(new DialogItem("From up here, you can see the summit ridges for leagues around."));
        lookout.addDialogItem(new DialogItem("High winds can knock a careless wizard right off the edge."));
        lookout.addDialogItem(new DialogItem("Here, take this Warding Talisman to steady your defense against sudden gusts.", () -> {
            gameContext.addItem(ItemType.UNCOMMON_WARDING_TALISMAN.create());
        }));
        lookout.addInteractedDialog(new DialogItem("Keep your eyes on the horizon!"));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, lookout);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.EAST, "Summit Ribbon", SummitRibbon::new);
        exits.addExit(Direction.WEST, "Summit Scarp", SummitScarp::new);
    }

    @Override
    public String getName() {
        return "Peakwatch Tower";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.LOW;
    }
}
