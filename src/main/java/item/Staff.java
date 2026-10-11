package main.java.item;

import main.java.Effect;
import main.java.MagicType;

import java.util.List;

public class Staff extends MagicItem implements Useable{

    public Staff(String name, String description, Quality quality, MagicType magicType, List<Effect> effects){
        super(name,description, quality, magicType, effects);

    }

    @Override
    public void use() {

    }


}
