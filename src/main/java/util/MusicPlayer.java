package main.java.util;

public class MusicPlayer extends AudioPlayer {

    private static final MusicPlayer INSTANCE = new MusicPlayer();

    private MusicPlayer() {
        super("MusicPlayer-Thread", 0.15f);
    }

    public static MusicPlayer getInstance() {
        return INSTANCE;
    }

    public void playRandomBattleIntro()     { playRandom("/music/battle/intro", false); }
    public void playRandomBattleTrack()     { playRandom("/music/battle/main", true); }
    public void playRandomSettlementScene() { playRandom("/music/scenes/settlement", true); }
    public void playRandomLocationScene()   { playRandom("/music/scenes/location", true); }
    public void playRandomPathScene()       { playRandom("/music/scenes/path", true); }
}
