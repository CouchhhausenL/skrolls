package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.battle.Battle;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.entity.monster.MonsterType;
import main.java.entity.player.Player;
import main.java.game.GameContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.screen.widget.impl.LabelWidget;
import main.java.util.SkaParser;

import java.util.EnumMap;
import java.util.Map;

public class MainMenuScreen extends Screen {
    private final Map<Option, LabelWidget> optionWidgets = new EnumMap<>(Option.class);
    private final Map<Option, Runnable> optionListeners = new EnumMap<>(Option.class);
    private Option selectedOption;

    @Override
    protected void init(GameContext context) {
        Animation animation = SkaParser.parse("title.ska");
        addChild(new AnimationWidget(3, 2,  animation));

        optionListeners.put(Option.NEW_GAME, () -> {
            context.setBattle(new Battle(new Player("Test", 10, 10, null), MonsterType.AXE_ROGUE.create()));
            context.setScreen(new BattleScreenTransition());});
        optionListeners.put(Option.EXIT, context::stop);

        for (Option option : Option.values()) {
            LabelWidget widget = new LabelWidget(
                    3, 10 + (option.ordinal() * 2), option.getName()
            );
            optionWidgets.put(option, widget);
            addChild(widget);
        }

        addChild(new LabelWidget(3, 11 + Option.VALUE_COUNT * 2, "Press [SPACE] to select"));

        setSelectedOption(Option.NEW_GAME);
    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isArrow(KeyInputEvent.Direction.UP) || event.isChar(KeyInputEvent.Letter.W)) offsetSelection(-1);
        if (event.isArrow(KeyInputEvent.Direction.DOWN) || event.isChar(KeyInputEvent.Letter.S)) offsetSelection(1);
        if (event.isSpace()) optionListeners.get(selectedOption).run();
    }

    @Override
    public void onMouseEvent(MouseInputEvent event) {
        if (event.type() == MouseInputEvent.Type.SCROLL_UP) offsetSelection(-1);
        if (event.type() == MouseInputEvent.Type.SCROLL_DOWN) offsetSelection(1);
    }

    private void setSelectedOption(Option option) {
        for (Option currentOption : Option.values()) {
            LabelWidget optionWidget = optionWidgets.get(currentOption);
            if (currentOption == option) {
                optionWidget.setContent(" » " + currentOption.getName());
            } else {
                optionWidget.setContent("- " + currentOption.getName());
            }
            optionWidget.setWidth(optionWidget.getContent().length());
        }
        selectedOption = option;
    }

    private void offsetSelection(int offset) {
        int newOrdinal = selectedOption.ordinal() + offset;
        if (newOrdinal < 0) newOrdinal = 0;
        if (newOrdinal > Option.VALUE_COUNT - 1) newOrdinal = Option.VALUE_COUNT - 1;
        setSelectedOption(Option.fromOrdinal(newOrdinal));
    }

    @Override
    public void onResize() {

    }

    enum Option {
        NEW_GAME("New Game"),
        EXIT("Exit");

        private final String name;
        Option(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Option fromOrdinal(int ordinal) {
            for (Option option : Option.values()) {
                if (option.ordinal() == ordinal) return option;
            }
            return null;
        }

        public static final int VALUE_COUNT = Option.values().length;
    }
}
