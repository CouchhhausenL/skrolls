package main.java.screen.widget.impl;

import main.java.console.input.KeyInputEvent;
import main.java.screen.widget.impl.container.AbstractContainerWidget;
import main.java.screen.widget.impl.options.Option;
import main.java.util.Color;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Supplier;

// Sequenced dialog. Each DialogItem asks a question and may require no answer,
// a text input or a yes/no choice; onEnter runs on submit and the dialog can
// jump backward (goToIndex) before moving on.
public class DialogWidget extends AbstractContainerWidget {
    private final List<DialogItem> dialogItems = new ArrayList<>();
    private int index = 0;

    private final List<Object> enteredAnswers = new ArrayList<>();
    private Boolean lastBooleanAnswer = null;
    private boolean redirected = false;
    private LabelWidget questionLabel;
    private LabelWidget continueNoticeLabel;
    private TextFieldWidget inputField;
    private OptionsWidget optionsWidget;
    private Runnable onComplete = () -> {
    };

    public DialogWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void addDialogItem(DialogItem item) {
        dialogItems.add(item);
        if (dialogItems.size() == 1) {
            setupCurrentItem();
        }
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }

    // Rebuilds the child widgets for the item at the current index
    private void setupCurrentItem() {
        widgets.clear();
        inputField = null;
        optionsWidget = null;

        if (index >= dialogItems.size()) return;

        DialogItem current = dialogItems.get(index);

        // Question is resolved lazily and types out character by character
        questionLabel = new LabelWidget(0, 0, current.questionSupplier().get());
        questionLabel.setFancyMode(true, 20);
        addChild(questionLabel);

        // Add the input control matching the required answer type
        if (current.answerField() == AnswerType.NONE) {
            continueNoticeLabel = new LabelWidget(0, questionLabel.getHeight() + 1, "Press [SPACE] to continue");
            addChild(continueNoticeLabel);
        }
        if (current.answerField() == AnswerType.INPUT) {
            inputField = new TextFieldWidget(0, questionLabel.getHeight() + 1, getWidth(), 1, "...Answer here", TextFieldWidget.DEFAULT_COLORS);
            addChild(inputField);
        }
        if (current.answerField() == AnswerType.BOOLEAN) {
            optionsWidget = new OptionsWidget(0, questionLabel.getHeight() + 1, getWidth(), 3);
                optionsWidget.addOption(new Option("YES", () -> selectBooleanAnswer(true)));
                optionsWidget.addOption(new Option("NO", () -> selectBooleanAnswer(false)));

            addChild(optionsWidget);
        }
    }

    private void selectBooleanAnswer(boolean value) {
        this.lastBooleanAnswer = value;
        enteredAnswers.add(value);

        // onEnter may call goToIndex, which sets redirected and changes the flow
        this.redirected = false;
        DialogItem current = (index < dialogItems.size()) ? dialogItems.get(index) : null;
        if (current != null && current.onEnter() != null) {
            current.onEnter().run();
        }

        // Only advance normally if onEnter did not redirect elsewhere
        if (!redirected) {
            advanceToNext();
        }
    }

    // Jumps to another dialog item; marks the run as redirected so the caller
    // does not also advance to the next item
    public void goToIndex(int newIndex) {
        if (newIndex >= 0 && newIndex < dialogItems.size()) {
            this.index = newIndex;
            this.redirected = true;
            setupCurrentItem();
        }
    }

    // Moves to the next item, or finishes the dialog (running onComplete)
    public void advanceToNext() {
        if (index < dialogItems.size() - 1) {
            index++;
            setupCurrentItem();
        } else {
            widgets.clear();
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }

    public String getEnteredAnswer() {
        if (inputField != null) return inputField.getContent();
        return getEnteredAnswer(index);
    }

    public String getEnteredAnswer(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < enteredAnswers.size()) {
            Object ans = enteredAnswers.get(itemIndex);
            return ans != null ? ans.toString() : "";
        }
        return "";
    }

    public boolean getEnteredBoolean() {
        if (lastBooleanAnswer != null) return lastBooleanAnswer;
        return getEnteredBoolean(index);
    }

    public boolean getEnteredBoolean(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < enteredAnswers.size()) {
            Object ans = enteredAnswers.get(itemIndex);
            if (ans instanceof Boolean b) return b;
        }
        return false;
    }

    // Routes input to the control matching the current answer type
    @Override
    public void onKeyInput(KeyInputEvent event) {
        if (index >= dialogItems.size()) return;
        DialogItem current = dialogItems.get(index);

        if (current.answerField() == AnswerType.BOOLEAN) {
            if (optionsWidget != null) {
                optionsWidget.onKeyInput(event);
            }
            return;
        }

        if (current.answerField() == AnswerType.INPUT) {
            if (inputField != null) {
                inputField.onKeyInput(event);
            }
            if (inputField != null && event.isEnter()) {
                if (current.onEnter() != null) {
                    current.onEnter().run();
                }
                advanceToNext();
            }
            return;
        }

        if (current.answerField() == AnswerType.NONE) {
            if (event.isSpace()) {
                if (current.onEnter() != null) {
                    current.onEnter().run();
                }
                advanceToNext();
            }
        }
    }

    @Override
    protected void updateWidgetPositions() {
    }

    public enum AnswerType {
        NONE,
        INPUT,
        BOOLEAN
    }

    public record DialogItem(Supplier<String> questionSupplier, AnswerType answerField, Runnable onEnter) {
        public DialogItem(String staticQuestion, AnswerType answerField, Runnable onEnter) {
            this(() -> staticQuestion, answerField, onEnter);
        }
    }
}
