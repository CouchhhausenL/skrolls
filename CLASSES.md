# Classes
- Game
- Character - maxHealth, health, name, damage(), initative
    - Monster - description, attack() 
    - Player - cast(), move(), crystalizedMagic, DifficultyLevel
- Battle
    - BossBattle
- Story
- Scene - getExits: SceneExits, getDifficulty: Difficulty
- SceneExits - EnumMap<Direction, Function<Player, Scene>>
- Direction - enum 
- Difficulty - enum: NONE, LOW, MEDIUM, HIGH, EXTREME
- MagicType - enum


- Item
    - Scroll
    - Artifact
    - Potion
    - Armor
    - Equipable (Interface)
	- Armor
	- Staff - MagicType
- Inventory


- Screen - eigenes String
- ScreenHost - interface -> changeScreen
- Widgets mit Z - Index 
- Ascii Widget
- Label Widget
- Input Widget
- VerbInputWidget
- ContainerWidget
	- ScrollableContainerWidget

- Console - Cell buffer
- Cell
- AsciiAnimation - kann von .ska parsen


