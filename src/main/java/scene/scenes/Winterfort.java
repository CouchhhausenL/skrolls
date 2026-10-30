package main.java.scene.scenes;

import main.java.DifficultyLevel;
import main.java.Interactable.Interactable;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.game.GameContext;
import main.java.item.ItemType;
import main.java.scene.Direction;
import main.java.scene.SettlementScene;
import main.java.screen.widget.impl.DialogWidget;
import main.java.screen.widget.impl.dialog.AnswerType;
import main.java.screen.widget.impl.dialog.DialogItem;

import java.util.ArrayList;
import java.util.List;

public class Winterfort extends SettlementScene {
    private final NPC dario;
    private final NPC trader;

    @Override
    public List<Interactable> getInteractables() {
        return List.of(dario,trader);
    }
    protected Winterfort(GameContext gameContext) {
        super(gameContext);
        dario = new NPC("winterfort_npc_1","Dario the Backpacker",NPCType.BACKPACKER, this);
        dario.addDialogItem(new DialogItem("Hello"));
        dario.addDialogItem(new DialogItem("Whats Up?"));
        dario.addDialogItem(new DialogItem("Oh.. I see you are a new Adventurer!"));
        dario.addDialogItem(new DialogItem("Here, I'll give you a Item you might need", () -> {
            gameContext.addItem(ItemType.COMMON_MANA_POTION.create());

        }));
        dario.addInteractedDialog(new DialogItem("I have nothing more to tell to you."));

        trader = new NPC("winterfort_npc_2", "Salesman",NPCType.SALESMAN, this);
        trader.addDialogItem(new DialogItem("Hi"));
        trader.addDialogItem(new DialogItem("Do you want to buy a Sparrow Feather for 10 Magic Crystals?", AnswerType.BOOLEAN, ()->{
            if(trader.getEnteredBoolean()){
                if(gameContext.spendMagicShards(10)){
                gameContext.addItem(ItemType.COMMON_SWIFT_FEATHER.create());}else{
                    gameContext.markUnInteracted("winterfort_npc_2");
                }

            }else{
                gameContext.markUnInteracted("winterfort_npc_2");
            }
        }));
        trader.addDialogItem(new DialogItem(
                () -> trader.getEnteredBoolean()
                    ? "Thank you!"
                        : "Then, because of their stinginess, my children won't have anything to eat tonight.",

                AnswerType.NONE,
                ()->{}

                )
        );
        trader.addInteractedDialog(new DialogItem("It was nice doing business with you!"));

    }

    @Override
    protected void setupExits() {

        exits.addExit(Direction.NORTH_EAST, "Gopher Byway", GopherByway::new);
        exits.addExit(Direction.EAST, "Lumberway", Lumberway::new);
        exits.addExit(Direction.SOUTH_EAST, "Winter Lane", WinterLane::new);
        exits.addExit(Direction.SOUTH_WEST, "Merchant Avenue", MerchantAvenue::new);
        exits.addExit(Direction.NORTH_WEST, "Gilded Approach", GildedApproach::new);
        exits.addExit("WinterfortCastle", "Enter Winterfort Castle", WinterfortCastle::new);


    }

    @Override
    public DifficultyLevel getDifficulty() {
        return DifficultyLevel.NONE;
    }

    @Override
    public String getName() {
        return "Winterfort";
    }
}
