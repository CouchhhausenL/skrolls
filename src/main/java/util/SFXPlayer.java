package main.java.util;

public class SFXPlayer extends AudioPlayer {

    private static final SFXPlayer INSTANCE = new SFXPlayer();

    static {
        // Preload UI sound effects in the background so menu actions play instantly without latency
        INSTANCE.preloadCategory("/sfx/Menu Sounds/move");
        INSTANCE.preloadCategory("/sfx/Menu Sounds/select");
        INSTANCE.preloadCategory("/sfx/General Sounds/Buttons");
        INSTANCE.preloadCategory("/sfx/General Sounds/Interactions");
        INSTANCE.preloadCategory("/sfx/Npc");
    }

    private SFXPlayer() {
        super("SFXPlayer-Thread", 0.40f);
    }

    public static SFXPlayer getInstance() {
        return INSTANCE;
    }

    private static void playSfx(String folder) {
        INSTANCE.playRandom(folder, false);
    }

    public static void playDeathScream() { playSfx("/sfx/Death Screams"); }
    public static void playButton()      { playSfx("/sfx/General Sounds/Buttons"); }
    public static void playCoin()        { playSfx("/sfx/General Sounds/Coins"); }
    public static void playImpact()      { playSfx("/sfx/General Sounds/Impacts"); }
    public static void playInteraction() { playSfx("/sfx/General Sounds/Interactions"); }
    public static void playNegative()    { playSfx("/sfx/General Sounds/Negative Sounds"); }
    public static void playNeutral()     { playSfx("/sfx/General Sounds/Neutral Sounds"); }
    public static void playMenuMove()    { playSfx("/sfx/Menu Sounds/move"); }
    public static void playMenuSelect()  { playSfx("/sfx/Menu Sounds/select"); }
    public static void playSpellBuff()   { playSfx("/sfx/Spell/buff"); }
    public static void playSpellDamage() { playSfx("/sfx/Spell/damage"); }
    public static void playMove()        { playSfx("/sfx/Movement/Move"); }
    public static void playTeleport()    { playSfx("/sfx/Movement/Teleport"); }
    public static void playMenuChange()  {playSfx("/sfx/General Sounds/Fanfares");}

    public static void playNpc() {
        playSfx("/sfx/Npc");
    }

}
