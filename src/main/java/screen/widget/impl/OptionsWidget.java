package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.impl.MainMenuScreen;
import main.java.screen.widget.Widget;
import org.jline.builtins.Options;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class OptionsWidget extends Widget {
    private int color;

    private final List<OptionsWidget.Option> options = new ArrayList<Option>();

    private OptionsWidget.Option selectedOption;



    public OptionsWidget(int x, int y, int width, int height, int color, Map<String, Runnable> rawOptions) {
        super(x, y, width, height);
        this.color = color;
        int yIncr = 0;
        for(Map.Entry<String, Runnable> rawOption : rawOptions.entrySet()){
            LabelWidget widget = new LabelWidget(x, y + yIncr, rawOption.getKey());

            options.add(new Option(rawOption.getKey(), rawOption.getValue(), widget));
            yIncr++;
        }
        if (!options.isEmpty()) setSelectedOption(options.getFirst());

    }

    @Override
    public void onDraw(DrawContextView context) {
        int yOffset = 0;
        for (Option option : options) {
            context.drawString(0, yOffset, option.getLabelWidget().getContent(), color);
            yOffset += 2; // oder 1 je nach gewünschtem Zeilenabstand
        }
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (event.isArrow(KeyInputEvent.Direction.UP) || event.isChar(KeyInputEvent.Letter.W)) offsetSelection(-1);
        if (event.isArrow(KeyInputEvent.Direction.DOWN) || event.isChar(KeyInputEvent.Letter.S)) offsetSelection(1);
        if (event.isSpace()) selectedOption.getRunnable().run();
    }

    private void setSelectedOption(Option option) {
        for (Option currentOption : options) {
            LabelWidget optionWidget = currentOption.getLabelWidget();
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
        int newIndex = options.indexOf(selectedOption) + offset;
        if (newIndex < 0) newIndex= 0;
        if (newIndex > options.size() - 1) newIndex = options.size()- 1;
        setSelectedOption(options.get(newIndex));
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
        if (event.type() == MouseInputEvent.Type.SCROLL_UP) offsetSelection(-1);
        if (event.type() == MouseInputEvent.Type.SCROLL_DOWN) offsetSelection(1);
    }
    public class Option {



        private final String name;
        private final Runnable runnable;
        private final LabelWidget labelWidget;

        public Option(String name, Runnable runnable, LabelWidget labelWidget){
            this.name = name;
            this.runnable = runnable;
            this.labelWidget = labelWidget;
        }
        public String getName() {
            return name;
        }
        public Runnable getRunnable(){
            return runnable;
        };
        public LabelWidget getLabelWidget(){
            return labelWidget;
        }



    }
}