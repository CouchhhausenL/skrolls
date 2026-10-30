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

public class SoulObelisk extends LocationScene {
    private final Chest chest;
    private final NPC channeler;

    public SoulObelisk(GameContext gameContext) {
        super(gameContext);
        chest = new Chest("soul_obelisk_chest_1", "Obsidian Relic Chest", Quality.RARE);

        channeler = new NPC("soul_obelisk_channeler", "Vesper the Channeler", NPCType.ELF, this);
        channeler.addDialogItem(new DialogItem("Shh... Listen. The obelisk vibrates with departed whispers."));
        channeler.addDialogItem(new DialogItem("This ancient obsidian stone anchors wandering souls before they dissipate into the nether."));
        channeler.addDialogItem(new DialogItem("Take this Gravebinder Gem — it will shield your life force against necrotic rot.", () -> {
            gameContext.addItem(ItemType.RARE_SOUL_GEM.create());
        }));
        channeler.addInteractedDialog(new DialogItem("Walk gently around the obelisk; the veil is thin here."));
    }

    @Override
    public List<Interactable> getInteractables() {
        return List.of(chest, channeler);
    }

    @Override
    protected void setupExits() {
        exits.addExit("Ashwick", "Leave Soul Obelisk", Ashwick::new);
    }

    @Override
    public String getName() {
        return "Soul Obelisk";
    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.MEDIUM;
    }
}
