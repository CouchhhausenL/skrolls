package main.java.screen.widget.impl.container;

import main.java.screen.widget.impl.LabelWidget;

public class KeyValueWidget extends BasicContainerWidget {


    public KeyValueWidget(String key, String value) {
        super(0,0, LabelWidget.calculateWidth(key)+LabelWidget.calculateWidth(value), Math.max(LabelWidget.calculateHeight(key), LabelWidget.calculateHeight(value)));
        LabelWidget keyWidget = new LabelWidget(key);
        addChild(keyWidget);
        keyWidget.setPosition(0,0);
        LabelWidget valueWidget = new LabelWidget(value);
        addChild(valueWidget);
        valueWidget.setPosition(keyWidget.getWidth(), 0);
    }
    public KeyValueWidget(String key, int keyColor, String value) {
        super(0,0, LabelWidget.calculateWidth(key)+LabelWidget.calculateWidth(value), Math.max(LabelWidget.calculateHeight(key), LabelWidget.calculateHeight(value)));
        LabelWidget keyWidget = new LabelWidget(key, keyColor);
        addChild(keyWidget);
        keyWidget.setPosition(0,0);
        LabelWidget valueWidget = new LabelWidget(value);
        addChild(valueWidget);
        valueWidget.setPosition(keyWidget.getWidth(), 0);
    }
    public KeyValueWidget(String key, int keyColor, String value, int valueColor) {
        super(0,0, LabelWidget.calculateWidth(key)+LabelWidget.calculateWidth(value), Math.max(LabelWidget.calculateHeight(key), LabelWidget.calculateHeight(value)));
        LabelWidget keyWidget = new LabelWidget(key, keyColor);
        addChild(keyWidget);
        keyWidget.setPosition(0,0);
        LabelWidget valueWidget = new LabelWidget(value, valueColor);
        addChild(valueWidget);
        valueWidget.setPosition(keyWidget.getWidth(), 0);
    }
    public KeyValueWidget(int x, int y, LabelWidget key, LabelWidget value) {
        super(x, y, key.getWidth()+value.getWidth(), Math.max(key.getHeight(), value.getHeight()));
        addChild(key);
        key.setPosition(0,0);
        addChild(value);
        value.setPosition(key.getWidth(), 0);
    }

}
