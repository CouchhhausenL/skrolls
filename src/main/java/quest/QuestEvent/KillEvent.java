package main.java.quest.QuestEvent;

import main.java.entity.monster.MonsterType;

public record KillEvent(MonsterType monsterType) implements QuestEvent {}
