package main.java.Interactable;

import main.java.game.GameContext;
import main.java.item.Item;
import main.java.item.ItemType;
import main.java.item.Quality;
import main.java.screen.impl.Transitions.ChestOpenScreen;
import main.java.util.SFXPlayer;
import main.java.util.SplashText;

public class Chest extends Interactable{
    Quality quality;

    public Chest(String id, String name, Quality quality){
        super(id, name,"loot");
        this.quality = quality;
    }
    @Override
    public void onInteract(GameContext context){
        if(context.hasInteracted(getId())){
            context.addMessage(SplashText.getNo());
            context.addMessage("This chest is already empty!");
            SFXPlayer.playNegative();
            return;
        }
        context.setScreen(new ChestOpenScreen());
        context.markInteracted(getId());
        Item item = ItemType.getItemByQuality(quality);
        context.addItem(item);
        context.addMessage("You found the item "+item.getName()+" in the Chest.");
    };
}
