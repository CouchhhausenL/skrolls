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

public class ValorLookout extends LocationScene {
    private final Chest chest;
    private final NPC sentinel;

    public ValorLookout(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("valor_lookout_chest_1", "Lookout Scout Cache", Quality.RARE);

        sentinel = new NPC("valor_lookout_sentinel", "Sentinel Brand", NPCType.ADVENTURER, this);
        sentinel.addDialogItem(new DialogItem("Stand firm! Valor Lookout guards the boundary of the great woodlands."));
        sentinel.addDialogItem(new DialogItem("Redwood monsters strike fast and hard. Here, carry this Greater Health Flask with you.", () -> {
            gameContext.addItem(ItemType.RARE_GREATER_HEALTH_POTION.create());
        }));
        sentinel.addInteractedDialog(new DialogItem("Never lower your guard while the wind blows through the pines."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, sentinel);
    }

    @Override
    protected void setupExits() {
        exits.addExit(Direction.NORTH_WEST, "Redwood Connector", RedwoodConnector::new);
        exits.addExit(Direction.EAST, "Cathedral Lane", CathedralLane::new);
    }

    @Override
    public String getName() {
        return "Valor Lookout";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
