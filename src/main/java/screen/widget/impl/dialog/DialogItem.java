package main.java.screen.widget.impl.dialog;

import java.util.function.Supplier;

public record DialogItem(Supplier<String> questionSupplier, AnswerType answerField, Runnable onEnter) {
        public DialogItem(String staticQuestion, AnswerType answerField, Runnable onEnter) {
            this(() -> staticQuestion, answerField, onEnter);
        }
        public DialogItem(String staticQuestion){
            this(()->staticQuestion, AnswerType.NONE, ()->{});
        }
        public DialogItem(String staticQuestion, AnswerType answerField){
            this(()->staticQuestion,answerField, ()->{});
        }
        public DialogItem(String staticQuestion, Runnable onEnter){
            this(()->staticQuestion,AnswerType.NONE, onEnter);
        }
    }