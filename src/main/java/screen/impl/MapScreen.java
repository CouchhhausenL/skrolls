package main.java.screen.impl;

import main.java.animation.Animation;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.game.GameContext;
import main.java.screen.DrawContext;
import main.java.screen.Screen;
import main.java.screen.widget.impl.AnimationWidget;
import main.java.util.Color;
import main.java.util.Point;
import main.java.util.SkaParser;

import java.util.EnumMap;
import java.util.Map;

public class MapScreen extends Screen {
    private GameContext context;

    private final Map<Zoom, AnimationWidget> widgetMap = new EnumMap<>(Zoom.class);

    private Zoom zoom;
    private Point mouseDownPoint;
    private int xOffset = 0;
    private int yOffset = 0;

    @Override
    protected void init(GameContext context) {
        this.context = context;

        for (Zoom zoom : Zoom.values()) {
            Animation animation = SkaParser.parse("map/map" + (zoom.ordinal() + 1) + ".ska");
            AnimationWidget widget = new AnimationWidget(0, 0, animation);
            widget.setLooping(true);
            widgetMap.put(zoom, widget);
        }

        positionWidgets();
        setZoom(Zoom.OUT);
    }

    @Override
    public void onKeyEvent(KeyInputEvent event) {
        if (event.isEscape())
            context.setScreen(new MainGameScreen());

        if (event.isChar('+')) zoomIn();
        if (event.isChar('-')) zoomOut();

        if (event.isArrow(KeyInputEvent.Direction.UP) || event.isChar(KeyInputEvent.Letter.W)) changeOffsets(0, 1);
        if (event.isArrow(KeyInputEvent.Direction.DOWN) || event.isChar(KeyInputEvent.Letter.S)) changeOffsets(0, -1);
        if (event.isArrow(KeyInputEvent.Direction.LEFT) || event.isChar(KeyInputEvent.Letter.A)) changeOffsets(2, 0);
        if (event.isArrow(KeyInputEvent.Direction.RIGHT) || event.isChar(KeyInputEvent.Letter.D)) changeOffsets(-2, 0);
    }

    @Override
    public void onMouseEvent(MouseInputEvent event) {
        Point clickPoint = new Point(event.x(), event.y());

        if (event.type() == MouseInputEvent.Type.SCROLL_UP) zoomIn();
        if (event.type() == MouseInputEvent.Type.SCROLL_DOWN) zoomOut();
        if (event.type() == MouseInputEvent.Type.PRESS) mouseDownPoint = clickPoint;
        if (event.type() == MouseInputEvent.Type.MOVE && mouseDownPoint != null) {
            drag(mouseDownPoint, clickPoint);
            mouseDownPoint = clickPoint;
        }
        if (event.type() == MouseInputEvent.Type.RELEASE) mouseDownPoint = null;
    }

    private void drag(Point start, Point end) {
        changeOffsets(end.x() - start.x(), end.y() - start.y());
    }

    private void setZoom(Zoom zoom) {
        for (Zoom currentZoom : Zoom.values()) {
            AnimationWidget widget = widgetMap.get(currentZoom);

            if (currentZoom == zoom) {
                addChild(widget);
            } else {
                removeChild(widget);
            }
        }


        if (this.zoom != null) {
            double offsetFactor =
                    (double) zoom.getFactor() / this.zoom.getFactor();

            xOffset *= offsetFactor;
            yOffset *= offsetFactor;
        }

        this.zoom = zoom;

        positionWidgets();
    }

    private void zoomIn() {
        switch (zoom) {
            case OUT -> setZoom(Zoom.MID);
            case MID -> setZoom(Zoom.IN);
        }
    }

    private void zoomOut() {
        switch (zoom) {
            case IN -> setZoom(Zoom.MID);
            case MID -> setZoom(Zoom.OUT);
        }
    }

    private void changeOffsets(int xChange, int yChange) {
        xOffset += xChange;
        yOffset += yChange;
        positionWidgets();
    }

    private void positionWidgets() {
        for (Zoom zoom : Zoom.values()) {
            AnimationWidget widget = widgetMap.get(zoom);

            final int width = widget.getWidth();
            final int height = widget.getHeight();
            final int screenWidth = getWidth();
            final int screenHeight = getHeight();

            widget.setPosition(
                    (screenWidth - width) / 2 + xOffset,
                    (screenHeight - height) / 2 + yOffset
            );
        }
    }

    @Override
    public void onResize() {
        positionWidgets();
    }

    @Override
    protected void onPostDraw(DrawContext context) {
        context.drawString(1, getHeight() - 3, "Use mouse / arrow to move and +/- or scroll to zoom.", Color.WHITE);
        context.drawString(1, getHeight() - 2, "Press escape to exit", Color.WHITE);
    }

    enum Zoom {
        OUT(1),
        MID(2),
        IN(4);

        private final int factor;
        Zoom(int factor) {
            this.factor = factor;
        }

        public int getFactor() {
            return factor;
        }
    }
}
