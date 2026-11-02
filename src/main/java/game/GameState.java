package main.java.game;

import main.java.DifficultyLevel;
import main.java.Interactable.NPC;
import main.java.Interactable.NPCType;
import main.java.PlayerLevel;
import main.java.entity.monster.MonsterType;
import main.java.entity.player.Player;
import main.java.item.*;
import main.java.item.items.*;
import main.java.quest.Quest;
import main.java.quest.QuestReward;
import main.java.quest.impl.GiveItemsQuest;
import main.java.quest.impl.KillQuest;
import main.java.scene.Scene;
import main.java.util.AudioPlayer;
import main.java.util.MusicPlayer;

import java.lang.reflect.Constructor;
import java.util.*;

public class GameState {

    // --- Player Data ---
    public String playerName;
    public PlayerLevel playerLevel;
    public int health;
    public int mana;
    public int magicShards;

    // --- Inventory ---
    public List<ItemType> backpack = new ArrayList<>();
    public ItemType equippedStaff;
    public ItemType equippedArmor;
    public List<ItemType> equippedArtifacts = new ArrayList<>();
    public List<ItemType> equippedScrolls = new ArrayList<>();

    // --- Scene & Interactions ---
    public String sceneClassName;
    public List<String> visitedSceneClassNames = new ArrayList<>();
    public Set<String> interactedIds = new HashSet<>();

    // --- Messages ---
    public List<String> messages = new ArrayList<>();

    // --- Quests (supports any number of active quests) ---
    public List<QuestData> quests = new ArrayList<>();

    public float volume;

    public static class QuestData {
        public String questType; // "KILL" or "GIVE_ITEMS"
        public String id;
        public String title;
        public int required;
        public int progress;
        public int rewardMagicShards;
        public List<ItemType> rewardItemTypes = new ArrayList<>();

        // NPC info
        public String npcId;
        public String npcName;
        public NPCType npcType;
        public String npcLocationName;

        // Type-specific data
        public MonsterType monsterType; // for KillQuest
        public ItemType giveItemType;   // for GiveItemsQuest
    }

    public GameState() {}

    public GameState(Game game) {
        volume = MusicPlayer.getInstance().getVolume();
        // 1. Player & Inventory
        Player player = game.player;
        if (player != null) {
            this.playerName = player.getName();
            this.playerLevel = player.getPlayerLevel();
            this.health = player.getHealth();
            this.mana = player.getMana();
            this.magicShards = player.getMagicShards();

            Inventory inv = player.getInventory();

            // Backpack
            for (Item item : inv.getBackpackItems()) {
                if (item instanceof AbstractItem abstractItem && abstractItem.getItemType() != null) {
                    this.backpack.add(abstractItem.getItemType());
                }
            }

            // Equipped items
            if (inv.getStaff() != null && inv.getStaff().getItemType() != null) {
                this.equippedStaff = inv.getStaff().getItemType();
            }
            if (inv.getArmor() != null && inv.getArmor().getItemType() != null) {
                this.equippedArmor = inv.getArmor().getItemType();
            }
            for (Artifact artifact : inv.getArtifacts()) {
                if (artifact.getItemType() != null) {
                    this.equippedArtifacts.add(artifact.getItemType());
                }
            }
            for (Scroll scroll : inv.getScrolls()) {
                if (scroll.getItemType() != null) {
                    this.equippedScrolls.add(scroll.getItemType());
                }
            }
        }

        // 2. Current Scene & Visited Scenes
        if (game.scene != null) {
            this.sceneClassName = game.scene.getClass().getName();
        }
        for (Scene vs : game.getVisitedScenes()) {
            this.visitedSceneClassNames.add(vs.getClass().getName());
        }

        // 3. Interacted IDs
        this.interactedIds = new HashSet<>(game.getInteractedIds());

        // 4. Messages
        this.messages = new ArrayList<>(game.getMessages());

        // 5. Quests
        for (Quest q : game.getQuests()) {
            QuestData qd = new QuestData();
            qd.id = q.getId();
            qd.title = q.getTitle();
            qd.required = q.getRequired();
            qd.progress = q.getProgress();

            if (q.getReward() != null) {
                qd.rewardMagicShards = q.getReward().magicShards();
                for (Item item : q.getReward().items()) {
                    if (item instanceof AbstractItem ai && ai.getItemType() != null) {
                        qd.rewardItemTypes.add(ai.getItemType());
                    }
                }
            }

            if (q.getNpc() != null) {
                qd.npcId = q.getNpc().getId();
                qd.npcName = q.getNpc().getName();
                qd.npcType = q.getNpc().getNpcType();
                if (q.getNpc().getLocation() != null) {
                    qd.npcLocationName = q.getNpc().getLocation().getName();
                }
            }

            if (q instanceof KillQuest kq) {
                qd.questType = "KILL";
                qd.monsterType = kq.getMonsterType();
            } else if (q instanceof GiveItemsQuest giq) {
                qd.questType = "GIVE_ITEMS";
                if (giq.getItem() instanceof AbstractItem ai) {
                    qd.giveItemType = ai.getItemType();
                }
            }

            this.quests.add(qd);
        }
    }

    public void applyTo(Game game) {
        MusicPlayer.getInstance().setVolume(volume);
        // 1. Recreate Inventory
        Inventory inventory = new Inventory();

        if (backpack != null) {
            for (ItemType type : backpack) {
                if (type != null) {
                    inventory.addItem(type.create());
                }
            }
        }

        // 2. Recreate equipped items
        if (equippedStaff != null) {
            Item staff = equippedStaff.create();
            inventory.addItem(staff);
            inventory.equipItem(staff);
        }
        if (equippedArmor != null) {
            Item armor = equippedArmor.create();
            inventory.addItem(armor);
            inventory.equipItem(armor);
        }
        if (equippedArtifacts != null) {
            for (ItemType type : equippedArtifacts) {
                if (type != null) {
                    Item art = type.create();
                    inventory.addItem(art);
                    inventory.equipItem(art);
                }
            }
        }
        if (equippedScrolls != null) {
            for (ItemType type : equippedScrolls) {
                if (type != null) {
                    Item scr = type.create();
                    inventory.addItem(scr);
                    inventory.equipItem(scr);
                }
            }
        }

        // 3. Recreate Player
        Player restoredPlayer = new Player(playerName, playerLevel, inventory);
        restoredPlayer.setMagicShards(magicShards);

        int missingHealth = restoredPlayer.getMaxHealth() - health;
        if (missingHealth > 0) {
            restoredPlayer.takeDamage(missingHealth);
        }
        int missingMana = restoredPlayer.getMaxMana() - mana;
        if (missingMana > 0) {
            restoredPlayer.spendMana(missingMana);
        }

        game.setPlayer(restoredPlayer);

        // 4. Current Scene
        if (sceneClassName != null) {
            Scene restoredScene = instantiateScene(sceneClassName, game);
            if (restoredScene != null) {
                game.setScene(restoredScene);
            }
        }

        // 5. Visited Scenes
        game.clearVisitedScenes();
        if (visitedSceneClassNames != null) {
            for (String className : visitedSceneClassNames) {
                Scene s = instantiateScene(className, game);
                if (s != null) {
                    game.visitScene(s);
                }
            }
        }

        // 6. Interacted IDs
        if (interactedIds != null) {
            game.setInteractedIds(new HashSet<>(interactedIds));
        }

        // 7. Messages
        if (messages != null) {
            game.setMessages(new ArrayList<>(messages));
        }

        // 8. Quests (restore all active quests)
        game.clearQuests();
        if (quests != null) {
            for (QuestData qd : quests) {
                List<Item> rewardItems = new ArrayList<>();
                if (qd.rewardItemTypes != null) {
                    for (ItemType it : qd.rewardItemTypes) {
                        if (it != null) {
                            rewardItems.add(it.create());
                        }
                    }
                }
                QuestReward reward = new QuestReward(qd.rewardMagicShards, rewardItems);

                Scene dummyLocation = new Scene(game) {
                    @Override
                    protected void setupExits() {}

                    @Override
                    public DifficultyLevel getDifficulty() {
                        return DifficultyLevel.NONE;
                    }

                    @Override
                    public String getName() {
                        return qd.npcLocationName != null ? qd.npcLocationName : "";
                    }
                };

                NPC npc = new NPC(
                        qd.npcId != null ? qd.npcId : "npc",
                        qd.npcName != null ? qd.npcName : "Quest Giver",
                        qd.npcType != null ? qd.npcType : NPCType.QUEEN,
                        dummyLocation
                );

                Quest restoredQuest = null;
                if ("KILL".equals(qd.questType) && qd.monsterType != null) {
                    restoredQuest = new KillQuest(qd.id, qd.title, qd.required, reward, npc, qd.monsterType);
                } else if ("GIVE_ITEMS".equals(qd.questType)) {
                    Item giveItem = qd.giveItemType != null ? qd.giveItemType.create() : null;
                    restoredQuest = new GiveItemsQuest(qd.id, qd.title, qd.required, reward, npc, giveItem);
                } else {
                    // Fallback so quest is never lost
                    restoredQuest = new KillQuest(
                            qd.id,
                            qd.title,
                            qd.required,
                            reward,
                            npc,
                            qd.monsterType != null ? qd.monsterType : MonsterType.AXE_ROGUE
                    );
                }

                restoredQuest.setProgress(qd.progress);
                game.addQuest(restoredQuest);
            }
        }
    }

    /**
     * Instantiates a Scene using reflection, supporting both public and protected constructors.
     */
    private static Scene instantiateScene(String className, Game game) {
        if (className == null || className.isBlank()) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);

            // 1. Try constructor taking GameContext (public, protected or package-private)
            try {
                Constructor<?> ctor = clazz.getDeclaredConstructor(GameContext.class);
                ctor.setAccessible(true);
                return (Scene) ctor.newInstance(game);
            } catch (NoSuchMethodException ignored) {}

            // 2. Try constructor taking Game (public, protected or package-private)
            try {
                Constructor<?> ctor = clazz.getDeclaredConstructor(Game.class);
                ctor.setAccessible(true);
                return (Scene) ctor.newInstance(game);
            } catch (NoSuchMethodException ignored) {}

            // 3. Try no-arg constructor
            try {
                Constructor<?> ctor = clazz.getDeclaredConstructor();
                ctor.setAccessible(true);
                return (Scene) ctor.newInstance();
            } catch (NoSuchMethodException ignored) {}

            // 4. Try any 1-argument constructor accepting GameContext or Game
            for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
                if (ctor.getParameterCount() == 1 && ctor.getParameterTypes()[0].isAssignableFrom(game.getClass())) {
                    ctor.setAccessible(true);
                    return (Scene) ctor.newInstance(game);
                }
            }

            throw new NoSuchMethodException("No suitable constructor found for scene: " + className);
        } catch (Exception e) {
            System.err.println("Error loading scene " + className + ": " + e.getMessage());
            return null;
        }
    }
}
