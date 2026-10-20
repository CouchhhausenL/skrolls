package main.java.screen.widget.impl.container;

import main.java.console.CellBuffer;
import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;

// Vertically stacked container with a scroll range.
// scroll is <= 0; 0 shows the top, more negative values move down.
// A scrollbar track is drawn on the right edge in onPostDraw.
public class ScrollableContainerWidget extends VerticalContainerWidget {
    private int scroll = 0;
    private int allHeight = 0;

    public ScrollableContainerWidget(int x, int y, int width, int height, int gap) {
        super(x, y, width, height, gap);
    }

    @Override
    protected void onPostDraw(DrawContextView context) {
        // Draw the scrollbar track
        context.fill(getWidth() - 1, 0, getWidth(), getHeight(), 0x202020);
        // Map the scroll position onto the track height
        int divide = -allHeight + 1;
        if (divide == 0 || allHeight == 0) return;
        int scrollerY = scroll * getHeight() / divide;

        // Thumb is 3 cells tall; keep it fully inside the track at the edges
        final int diff = 1;
        int startY = scrollerY - diff;
        int endY = scrollerY + diff;

        if (startY < 0) {
            startY += diff;
            endY += diff;
        }
        if (endY > getHeight()) {
            startY -= diff;
            endY -= diff;
        }

        context.fill(getWidth() - 1, startY, getWidth(), endY, '│', 0xA0A0A0, CellBuffer.TRANSPARENT_BACKGROUND);
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        super.onKeyInput(event);
        if (event.isArrow(KeyInputEvent.Direction.UP)) scroll++;
        if (event.isArrow(KeyInputEvent.Direction.DOWN)) scroll--;
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
        if (event.type() == MouseInputEvent.Type.SCROLL_UP) scroll++;
        if (event.type() == MouseInputEvent.Type.SCROLL_DOWN) scroll--;

        // Clicks on the scrollbar track are swallowed (scrubbing not implemented)
        if (event.x() >= getWidth() - 1 && event.x() <= getWidth()) {
            //TODO: Add scrubbing scroll bar with mouse
            return;
        }
        super.onMouseInput(event);
    }

    @Override
    protected void updateWidgetPositions() {
        // Total content height including gaps between widgets
        allHeight = 0;
        for (Widget widget : widgets) allHeight += widget.getHeight() + gap;
        if (allHeight > 0) allHeight -= gap; // no gap after the last widget

        // Clamp scroll to [-(contentHeight - 1), 0]
        if (scroll > 0) scroll = 0;
        if (scroll < -allHeight + 1) scroll = -allHeight + 1;

        int y = scroll;
        for (Widget widget : widgets) {
            widget.setPosition(0, y);
            y += widget.getHeight();
            y += gap;
        }
    }
}
