package main.java;

public enum DifficultyLevel {
    NONE(5),
    LOW(15),
    INTERMEDIATE(25),
    MEDIUM(35),
    HIGH(45),
    EXTREME(65);

    private final int level;

    DifficultyLevel(int level){
        this.level = level;
    }
    public int getLevel(){
        return level;
    }

}
