package main.java.item;

import main.java.Effect;
import main.java.MagicType;

import java.util.List;

public class Artifact extends MagicItem{


    

    public Artifact(String name, String description, Quality quality,  MagicType magicType , List<Effect> effects){
        super(name,description, quality, magicType, effects);
    }



}
