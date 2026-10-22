package main.java.screen.widget.impl.options;

import main.java.item.Item;

    public class ItemOption extends Option {
        private Item item;

        public ItemOption(String name, int color, Item item, Runnable runnable) {
            super(name, color, runnable);
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }

