package main.java.item;

import main.java.MagicType;

public class Staff extends Item  {
    private MagicType damageType;
    private int power;

    public Staff(String name, String description, MagicType damageType, int power){
        super(name,description);
        this.damageType = damageType;
       this.power = power;
    }


    @Override
    public void use() {

    }
}
