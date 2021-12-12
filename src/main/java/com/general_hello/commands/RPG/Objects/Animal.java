package com.general_hello.commands.RPG.Objects;

import com.general_hello.commands.RPG.Commands.ShopCommand;
import com.general_hello.commands.RPG.Types.AttainableBy;
import com.general_hello.commands.RPG.Types.Rarity;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.entities.Emoji;

public class Animal {
    private final Rarity rarity;
    private final int rewardForCooking;
    private final String name;
    private final AttainableBy retrieveBy;
    private final String emojiOfItem;
    private final String description;
    private final Integer price;
    private final int sellPrice;

    public Animal(Rarity rarity, String name, String emojiOfItem, AttainableBy attainableBy, String description, Integer price) {
        this.rarity = rarity;
        this.rewardForCooking = rarity.getMultipliedValue();
        this.name = name;
        this.retrieveBy = attainableBy;
        this.emojiOfItem = emojiOfItem;
        this.description = description;
        this.price = price;

        int multi = UtilNum.randomNum(20, 100);
        this.sellPrice = rarity.getWorth() * multi;
    }

    public Long getEmojiId() {
        Emoji emoji = Emoji.fromMarkdown(this.emojiOfItem);
        return emoji.getIdLong();
    }

    public String getEmojiUrl() {
        if (emojiOfItem.replaceFirst("<", "").startsWith("a")) {
            return "https://cdn.discordapp.com/emojis/" + getEmojiId() + ".gif";
        }
        return "https://cdn.discordapp.com/emojis/" + getEmojiId() + ".png";
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return ShopCommand.formatter.format(this.price);
    }

    public String getFormattedSellPrice() {
        return ShopCommand.formatter.format(this.sellPrice);
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public AttainableBy getRetrieveBy() {
        return retrieveBy;
    }

    public String getEmojiOfItem() {
        return emojiOfItem;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getRewardForCooking() {
        return rewardForCooking;
    }

    public String getName() {
        return name;
    }
}
