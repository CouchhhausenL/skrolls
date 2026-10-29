package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.console.input.MouseInputEvent;
import main.java.item.Item;
import main.java.screen.DrawContextView;
import main.java.screen.widget.Widget;
import main.java.screen.widget.impl.options.ItemOption;
import main.java.screen.widget.impl.options.Option;

import java.util.ArrayList;
import java.util.List;

// Vertical list of selectable options. The selection is highlighted and
// activated with SPACE; one option (the first) is selected by default.
public class OptionsWidget extends Widget {

    private List<Option> options = new ArrayList<Option>();

    private Option selectedOption;

    public OptionsWidget(int x, int y, int width, int height){
        super(x,y,width,height);
    }

    public int getVisibleLines() {
        return getHeight() / 2 + 1;
    }

    public void addOption(Option option){
        options.add(option);
        if (selectedOption == null) {
            selectFirstNavigable();
        } else {
            setSelectedOption(selectedOption);
        }
    }

    /** Selects the first option that has a non-null runnable. */
    private void selectFirstNavigable() {
        for (Option option : options) {
            if (option.getRunnable() != null) {
                setSelectedOption(option);
                return;
            }
        }
        selectedOption = null;
    }

    public void removeOption(Option option){
        options.removeIf(option1 -> option1 == option);
        if (!options.isEmpty()) {
            selectFirstNavigable();
        } else {
            selectedOption = null;
        }
    }

    public void removeByItem(Item item){
        options.removeIf(option -> option instanceof ItemOption io && io.getItem() == item);
        if (!options.isEmpty()) {
            selectFirstNavigable();
        } else {
            selectedOption = null;
        }
    }

    public void removeByName(String name){
        options.removeIf(option -> name.equals(option.getName()));
        if (!options.isEmpty()) {
            selectFirstNavigable();
        } else {
            selectedOption = null;
        }
    }



    @Override
    public void onDraw(DrawContextView context) {
        if (options.isEmpty()) return;
        int currentIndex = selectedOption != null ? options.indexOf(selectedOption) : 0;
        int visibleLines = getVisibleLines();

        int start = currentIndex - (visibleLines / 2 - 1);
        if (start < 0) start = 0;

        int end = start + visibleLines;
        if (end > options.size()) {
            end = options.size();
            start = Math.max(0, end - visibleLines);
        }

        int yOffset = 0;
        for (Option option : options.subList(start, end)) {
            context.drawString(0, yOffset, option.getLabelWidget().getContent(), option.getLabelWidget().getColor());
            yOffset += 2;
        }
    }

    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (selectedOption == null) return;
        if (event.isArrow(KeyInputEvent.Direction.UP) || event.isChar(KeyInputEvent.Letter.W)) offsetSelection(-1);
        if (event.isArrow(KeyInputEvent.Direction.DOWN) || event.isChar(KeyInputEvent.Letter.S)) offsetSelection(1);
        if (event.isSpace() && selectedOption.getRunnable() != null) selectedOption.getRunnable().run();
    }

    // Rebuilds the marker prefix of every option to show the selection
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

    public Option getSelectedOption(){
        return selectedOption;
    }

    // Moves the selection, skipping over non-navigable (null runnable) header entries.
    // Uses iteration instead of recursion to avoid any edge-case stack issues.
    private void offsetSelection(int offset) {
        if (options.isEmpty() || selectedOption == null) return;
        int currentIndex = options.indexOf(selectedOption);
        int newIndex = currentIndex;

        // Step one at a time in the given direction, skipping null-runnable headers
        int step = offset > 0 ? 1 : -1;
        while (true) {
            int next = newIndex + step;
            // Hit the boundary — stay where we are
            if (next < 0 || next >= options.size()) break;
            newIndex = next;
            // Found a navigable option — select it and stop
            if (options.get(newIndex).getRunnable() != null) break;
        }

        // Only move if the final candidate is actually navigable
        if (options.get(newIndex).getRunnable() != null) setSelectedOption(options.get(newIndex));
    }

    @Override
    public void onMouseInput(MouseInputEvent event) {
        if (event.type() == MouseInputEvent.Type.SCROLL_UP) offsetSelection(-1);
        if (event.type() == MouseInputEvent.Type.SCROLL_DOWN) offsetSelection(1);
    }
}
