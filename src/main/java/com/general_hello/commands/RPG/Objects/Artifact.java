package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Commands.ShopCommand;
import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.entities.Emoji;

public class Artifact {
    private final Rarity rarity;
    private final String name;
    private final AttainableBy retrieveBy;
    private final String emoji;
    private final String description;
    private final int price;
    private final int sellPrice;

    public Artifact(Rarity rarity, String name, String emoji, String description, int price) {
        this.rarity = rarity;
        this.name = name;
        this.retrieveBy = AttainableBy.CRAFTING;
        this.emoji = emoji;
        this.description = description;
        this.price = price;
        Initializer.artifactToId.put(name.toLowerCase().replace("'", "").replaceAll("\\s+", ""), this);
        Initializer.artifacts.add(this);
        Initializer.allNames.add(name);

        int multi = UtilNum.randomNum(20, 100);
        this.sellPrice = rarity.getWorth() * multi;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
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

    public AttainableBy getRetrieveBy() {
        return retrieveBy;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return ShopCommand.formatter.format(this.price);
    }

    public String getFormattedSellPrice() {
        return ShopCommand.formatter.format(this.sellPrice);
    }
}
