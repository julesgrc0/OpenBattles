package com.julesG10.game.player;

import java.util.Arrays;

public class Inventory {
    public InventoryItem[] items = new InventoryItem[8];
    private int currentIndex = 0;


    public void setItem(int index, InventoryItem item) {
        this.items[index] = item;
    }

    public InventoryItem getCurrentItem() {
        return items[this.currentIndex];
    }

    public InventoryItem getNextItem() {
        this.currentIndex++;
        if (this.currentIndex > items.length) {
            this.currentIndex = 0;
        }

        return this.getCurrentItem();
    }

    public Inventory() {
        Arrays.fill(items, InventoryItem.EMPTY);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (InventoryItem it : items)
        {
            builder.append(it.ordinal()).append("|");
        }

        return builder.toString();
    }

    public void fromString(String str)
    {
        int i =0;
        for (String item : str.split("|"))
        {
            int index = Integer.getInteger(item);
            this.items[i] = InventoryItem.values()[index];
            i++;
        }
    }
}
