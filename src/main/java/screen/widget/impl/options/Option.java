package main.java.screen.widget.impl.options;

import main.java.screen.widget.impl.LabelWidget;
import main.java.util.Color;

public class Option{
    private String name;
    private Runnable runnable;
    private LabelWidget labelWidget;

    private int color;
    public Option(String name, Runnable runnable) {
        this.name = name;
        this.runnable = runnable;
        this.labelWidget = new LabelWidget(0,0,name);
        this.color = Color.WHITE;
    }
    public Option(String name, int color, Runnable runnable){
        this.name = name;
        this.runnable = runnable;
        this.labelWidget = new LabelWidget(0,0,name, color);
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public LabelWidget getLabelWidget() {
        return labelWidget;
    }

}