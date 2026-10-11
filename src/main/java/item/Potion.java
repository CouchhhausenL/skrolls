package main.java.item;

import main.java.Effect;
import main.java.MagicType;

import java.util.List;

public class Potion extends MagicItem implements Useable{
    public final int duration;



    public Potion(String name, String description, Quality quality, MagicType magicType, List<Effect> effects, int duration){
        super(name,description,quality, magicType, effects);
        this.duration = duration;
    }

    @Override
    public void use() {

    }
}
