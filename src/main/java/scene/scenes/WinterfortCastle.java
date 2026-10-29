package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Chest;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.Quality;
import main.java.scene.LocationScene;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.List;

public class WinterfortCastle extends LocationScene {
    private Chest chest;
    private NPC queen;

    public WinterfortCastle(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("winterfort_castle_chest_1","Chest", Quality.COMMON);
        queen = new NPC("winterfort_castle_queen","Queen of Winterfort", NPCType.QUEEN);
        queen.addDialogItem(new DialogItem("Hello, dear aspiring wizard's apprentice"));
        queen.addDialogItem(new DialogItem("The Kingdom of Winterfort needs your help!\n Our territories are being overrun by monsters and we can't defend ourselves."));

    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, queen);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Winterfort", "Leave Winterfort Castle", Winterfort::new);
    }

    @Override
    public String getName() {
        return "Winterforst Castle";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.NONE;
    }

}
