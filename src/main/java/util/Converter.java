package main.java.util;


import main.java.DifficultyLevel;
import main.java.PlayerLevel;
import main.java.item.Quality;

public class Converter {
    public static DifficultyLevel QualityToDifficultyLevel(Quality quality){
        return DifficultyLevel.values()[quality.ordinal()];
    }
    public static Quality DifficultyLevelToQuality(DifficultyLevel difficultyLevel){
        return Quality.values()[difficultyLevel.ordinal()];
    }
    public static DifficultyLevel PlayerLevelToDifficultyLevel(PlayerLevel playerLevel){
        return DifficultyLevel.values()[playerLevel.ordinal()];
    }
}
