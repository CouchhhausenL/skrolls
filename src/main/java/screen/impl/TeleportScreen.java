package main.java.screen.impl;

import main.java.game.GameContext;
import main.java.scene.Scene;
import main.java.screen.Screen;
import main.java.screen.widget.impl.OptionsWidget;
import main.java.screen.widget.impl.options.Option;
import main.java.util.Color;

import java.util.List;
import java.util.Set;

public class TeleportScreen extends Screen {

    private OptionsWidget optionsWidget;

    @Override
    protected void init(GameContext context) {
        Set<Scene> visitedScenes = context.getVisitedScenes();

        optionsWidget = new OptionsWidget(0, 0, 1, 1);

        if (visitedScenes.isEmpty()) {
            String string = "You haven't visited any Scenes";
            optionsWidget.addOption(new Option(string, () -> {
                context.setScreen(new MainGameScreen());

            }));
            optionsWidget.setDimensions(string.length() + 4, 3);
        } else {
            String question = "Where do you want to teleport to?";
            int maxLength = question.length();
            optionsWidget.addOption(new Option(question, Color.BEIGE, null ));

            for (Scene scene : visitedScenes) {
                String name = scene.getName();
                if (name.length() > maxLength) {
                    maxLength = name.length();
                }
                optionsWidget.addOption(new Option(name, () -> {
                    context.setScene(scene);
                    context.setScreen(new MainGameScreen());
                    context.addMessage("You teleported to "+name);
                }));
            }
            optionsWidget.setDimensions(maxLength + 4, (visitedScenes.size() + 1) * 2);
        }

        addChild(optionsWidget);
        autoPositionWidget(optionsWidget);
    }

    @Override
    public void onResize() {
        if (optionsWidget != null) {
            autoPositionWidget(optionsWidget);
        }
    }
}
