package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;
import net.dv8tion.jda.api.entities.Emoji;

public class Tool {
    private final Rarity rarity;
    private final AttainableBy toBeUsedOn;
    private final String name;
    private final AttainableBy retrieveBy;
    private final String emoji;
    private final String description;

    public Tool(Rarity rarity, String name, AttainableBy retrieveBy, String emoji, String description) {
        this.rarity = rarity;
        this.name = name;
        this.toBeUsedOn = AttainableBy.CRAFTING;
        this.retrieveBy = retrieveBy;
        this.emoji = emoji;
        this.description = description;
        Initializer.tools.add(this);
        Initializer.allNames.add(name);
        Initializer.toolToId.put(name.toLowerCase().replace("'", "").replaceAll("\\s+", ""), this);
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }

    public AttainableBy getRetrieveBy() {
        return retrieveBy;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public AttainableBy getToBeUsedOn() {
        return toBeUsedOn;
    }

    public Long getEmojiId() {
        Emoji emoji = Emoji.fromMarkdown(this.emoji);
        return emoji.getIdLong();
    }

    public String getEmojiUrl() {
        if (emoji.replaceFirst("<", "").startsWith("a")) {
            return "https://cdn.discordapp.com/emojis/" + getEmojiId() + ".gif";
        }
        return "https://cdn.discordapp.com/emojis/" + getEmojiId() + ".png";
    }

    public String getName() {
        return name;
    }
}
