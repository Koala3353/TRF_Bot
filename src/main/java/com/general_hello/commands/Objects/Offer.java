package com.general_hello.commands.Objects;

import java.util.ArrayList;
import java.util.List;

// Class the is used in the trade command and Trade class
public class Offer {
    // Variable
    private int berri;
    private int rainbowShards;
    private List<Object> items;

    // Initializer
    public Offer() {
        this.berri = 0;
        this.rainbowShards = 0;
        this.items = new ArrayList<>();
    }

    // Saves the trade in a hashmap
    public Trade save(long userId) {
        return Trade.loadTrade(userId).setOfferFocused(this).save();
    }

    // Getters and setters
    public int getBerri() {
        return berri;
    }

    public Offer setBerri(int berri) {
        this.berri = berri;
        return this;
    }

    public int getRainbowShards() {
        return rainbowShards;
    }

    public Offer setRainbowShards(int rainbowShards) {
        this.rainbowShards = rainbowShards;
        return this;
    }

    public List<Object> getItems() {
        return items;
    }

    public Offer setItems(Object... items) {
        this.items = List.of(items);
        return this;
    }

    public Offer setItems(List<Object> items) {
        this.items = items;
        return this;
    }

    public Offer addItems(Object... items) {
        this.items.addAll(List.of(items));
        return this;
    }

    public Offer addItems(List<Object> items) {
        this.items.addAll(items);
        return this;
    }

    // Makes the offer to fancy text
    public String getOfferString() {
        StringBuilder content = new StringBuilder();
        if (this.items.isEmpty() && this.rainbowShards == 0 && this.berri == 0) {
            content = new StringBuilder("*No offer so far*");
        }

        if (!this.items.isEmpty()) {
            content.append("**Items:** \n");
            int x = 0;
            for (Object item : getItems()) {
                content.append(item.getName());
                x++;
                if (x != getItems().size()) {
                    content.append(", ");
                }
            }
            content.append("\n**Rainbow shards:** " + RPGEmojis.rainbowShards + " ").append(getRainbowShards()).append("\n");
            content.append("**Berri:** " + RPGEmojis.berri + " ").append(getBerri());
        }

        if (this.rainbowShards != 0 || this.berri != 0) {
            content.append("**Rainbow shards:** " + RPGEmojis.rainbowShards + " ").append(getRainbowShards()).append("\n");
            content.append("**Berri:** " + RPGEmojis.berri + " ").append(getBerri());
        }
        return content.toString();
    }
}
