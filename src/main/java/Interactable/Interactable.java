package main.java.Interactable;

import main.java.game.GameContext;

public abstract class Interactable {
    private final String name;
    private final String verb;

    private final String preposition;
    private final String id;



    public Interactable(String id, String name, String verb){
        this.id = id;
        this.name = name;
        this.verb = verb;
        this.preposition = null;
    }
    public Interactable(String id, String name, String verb, String preposition){
        this.id = id;
        this.name = name;
        this.verb = verb;
        this.preposition = preposition;
    }

    public abstract void onInteract(GameContext context);

    public String getName() {
        return name;
    }

    public String getVerb() {
        return verb;
    }

    public String getId() {
        return id;
    }

    public String getPreposition(){
        return preposition;
    }
}
