package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.*;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class ShopCommand extends Command {
    public static final DecimalFormat formatter = new DecimalFormat("#,###.00");

    public ShopCommand() {
        this.name = "shop";
        this.cooldown = 5;
        this.cooldownScope = CooldownScope.USER;
        String[] strings = new String[1];
        strings[0] = "store";
        this.aliases = strings;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!RPGDataUtils.isRPGUser(event.getAuthor())) {
            event.getChannel().sendMessage("Kindly start your journey first by doing `ignt journey`").queue();
            return;
        }

        if (event.getArgs().isEmpty()) {
            buildEmbed(1, event.getTextChannel(), event.getAuthor().getIdLong());
            return;
        }
        String name = event.getArgs().replaceAll("'", "").replaceAll("^[\"']\\w+[\"']", "").replaceAll("\\s+", "").toLowerCase();

        boolean isErrorOutputed = false;
        try {
            if (Initializer.landAnimalToId.containsKey(name)) {
                LandAnimal landAnimal = Initializer.landAnimalToId.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(landAnimal.getName() + " (" + RPGUser.getItemCount(event.getAuthor().getIdLong(), RPGDataUtils.filter(name)) + " owned)");
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setDescription("> " + landAnimal.getDescription() + "\n\n" +
                        "**BUY** - " + (landAnimal.getCostToBuy() == null ? "Out of Stock" : RPGEmojis.shekels + " [" + landAnimal.getFormattedPrice() + "](https://discord.com)") + "\n" +
                        "**SELL** - " + RPGEmojis.shekels + " [" + landAnimal.getFormattedSellPrice() + "](https://discord.com)\n" +
                        "**TRADE** - Unknown");
                embedBuilder.setThumbnail(landAnimal.getEmojiUrl());
                embedBuilder.addField("Rarity", "`" + landAnimal.getRarity().getName() + "`", true);
                embedBuilder.addField("Type", "`Sellable`", true);
                embedBuilder.addField("Cooking Reward", "`" + landAnimal.getRewardForCooking() + "`", true);
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }

            if (Initializer.seaAnimalToId.containsKey(name)) {
                SeaAnimal seaAnimal = Initializer.seaAnimalToId.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(seaAnimal.getName() + " (" + RPGUser.getItemCount(event.getAuthor().getIdLong(), RPGDataUtils.filter(name)) + " owned)");
                embedBuilder.setColor(Color.CYAN);
                embedBuilder.setDescription("> " + seaAnimal.getDescription() + "\n\n" +
                        "**BUY** - " + (seaAnimal.getCostToBuy() == null ? "Out of Stock" : RPGEmojis.shekels + " [" + seaAnimal.getFormattedPrice() + "](https://discord.com)") + "\n" +
                        "**SELL** - " + RPGEmojis.shekels + " [" + seaAnimal.getFormattedSellPrice() + "](https://discord.com)\n" +
                        "**TRADE** - Unknown");
                embedBuilder.addField("Rarity", "`" + seaAnimal.getRarity().getName() + "`", true);
                embedBuilder.addField("Type", "`Sellable`", true);
                embedBuilder.addField("Cooking Reward", "`" + seaAnimal.getRewardForCooking() + "`", true);
                embedBuilder.setThumbnail(seaAnimal.getEmojiUrl());
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }

            if (Initializer.toolToId.containsKey(name)) {
                Tool tool = Initializer.toolToId.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(tool.getName() + " (" + RPGUser.getItemCount(event.getAuthor().getIdLong(), RPGDataUtils.filter(name)) + " owned)");
                embedBuilder.setColor(Color.ORANGE);
                embedBuilder.setDescription("> " + tool.getDescription() + "\n\n" +
                        "**BUY** - " + RPGEmojis.shekels + " [30,000](https://discord.com)" + "\n" +
                        "**SELL** - " + RPGEmojis.shekels + " [20,000](https://discord.com)\n" +
                        "**TRADE** - Unknown");
                embedBuilder.addField("Rarity", "`" + tool.getRarity().getName() + "`", true);
                embedBuilder.addField("Type", "`Tool`", true);
                embedBuilder.addField("Retrievable by", "`" + tool.getToBeUsedOn().toString() + "`", true);
                embedBuilder.setThumbnail(tool.getEmojiUrl());
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }

            if (Initializer.artifactToId.containsKey(name)) {
                Artifact artifact = Initializer.artifactToId.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(artifact.getName() + " (" + RPGUser.getItemCount(event.getAuthor().getIdLong(), RPGDataUtils.filter(name)) + " owned)");
                embedBuilder.setColor(Color.YELLOW);
                embedBuilder.setDescription("> " + artifact.getDescription() + "\n\n" +
                        "**BUY** - " + RPGEmojis.shekels + " [" + artifact.getFormattedPrice() + "](https://discord.com)" + "\n" +
                        "**SELL** - " + RPGEmojis.shekels + " [" + artifact.getFormattedSellPrice() + "](https://discord.com)\n" +
                        "**TRADE** - Unknown");
                embedBuilder.addField("Rarity", "`" + artifact.getRarity().getName() + "`", true);
                embedBuilder.addField("Type", "`Collectible`", true);
                embedBuilder.addField("ID", "`" + name + "`", true);
                embedBuilder.setThumbnail(artifact.getEmojiUrl());
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }
        } catch (Exception e) {
            event.getTextChannel().sendMessage("The item you searched for is not there!").queue();
            isErrorOutputed = true;
        }

        if (!isErrorOutputed) {
            event.getTextChannel().sendMessage("The item you searched for is not there!").queue();
        }
    }

    public static void buildEmbed(int number, TextChannel textChannel, long author) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Shop");
        embedBuilder.setTimestamp(OffsetDateTime.now()).setFooter("Page " + number + " of 7");
        embedBuilder.setColor(InfoUserCommand.randomColor());

        switch (number) {
            case 1:
                buildDescription(embedBuilder, author);
                break;
            case 2:
                buildDescription2(embedBuilder, author);
                break;
            case 3:
                buildDescription3(embedBuilder, author);
                break;
            case 4:
                buildDescription4(embedBuilder, author);
                break;
            case 5:
                buildDescription5(embedBuilder, author);
                break;
            case 6:
                buildDescription6(embedBuilder, author);
                break;
            case 7:
                buildDescription7(embedBuilder, author);
                break;
        }

        switch (number) {
            case 1:
            case 2:
                textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                        Button.of(ButtonStyle.PRIMARY, author + ":previous:" + number, Emoji.fromMarkdown("<:left:915425233215827968>")).asDisabled(),
                        Button.of(ButtonStyle.SECONDARY, "1234:empty", "Land Animals").asDisabled(),
                        Button.of(ButtonStyle.PRIMARY, author + ":next:" + number, Emoji.fromMarkdown("<:right:915425310592356382>"))
                ).queue();
                break;
            case 3:
            case 4:
                textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                        Button.of(ButtonStyle.PRIMARY, author + ":previous:" + number, Emoji.fromMarkdown("<:left:915425233215827968>")),
                        Button.of(ButtonStyle.SECONDARY, "1234:empty", "Sea Animals").asDisabled(),
                        Button.of(ButtonStyle.PRIMARY, author + ":next:" + number, Emoji.fromMarkdown("<:right:915425310592356382>"))
                ).queue();
                break;
            case 5:
            case 6:
                textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                        Button.of(ButtonStyle.PRIMARY, author + ":previous:" + number, Emoji.fromMarkdown("<:left:915425233215827968>")),
                        Button.of(ButtonStyle.SECONDARY, "1234:empty", "Artifacts").asDisabled(),
                        Button.of(ButtonStyle.PRIMARY, author + ":next:" + number, Emoji.fromMarkdown("<:right:915425310592356382>"))
                ).queue();
                break;
            case 7:
                textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                        Button.of(ButtonStyle.PRIMARY, author + ":previous:" + number, Emoji.fromMarkdown("<:left:915425233215827968>")),
                        Button.of(ButtonStyle.SECONDARY, "1234:empty", "Tools").asDisabled(),
                        Button.of(ButtonStyle.PRIMARY, author + ":next:" + number, Emoji.fromMarkdown("<:right:915425310592356382>")).asDisabled()
                ).queue();
                break;
        }
    }

    public static void buildDescription(EmbedBuilder embedBuilder, long userId) {
        ArrayList<LandAnimal> landAnimals = Initializer.landAnimals;

        int x = 0;
        while (x < landAnimals.size()) {
            LandAnimal landAnimal = landAnimals.get(x);
            embedBuilder.appendDescription(landAnimal.getEmojiOfItem() + " **" + landAnimal.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(landAnimal.getName())) + ") — " + (landAnimal.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(landAnimal.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    landAnimal.getDescription() + "\n\n");
            x++;

            if (x > 7) return;
        }
    }

    public static void buildDescription2(EmbedBuilder embedBuilder, long userId) {
        ArrayList<LandAnimal> landAnimals = Initializer.landAnimals;

        int x = 7;
        while (x < landAnimals.size()) {
            LandAnimal landAnimal = landAnimals.get(x);
            embedBuilder.appendDescription(landAnimal.getEmojiOfItem() + " **" + landAnimal.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(landAnimal.getName())) + ") — " + (landAnimal.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(landAnimal.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    landAnimal.getDescription() + "\n\n");
            x++;
        }
    }

    public static void buildDescription3(EmbedBuilder embedBuilder, long userId) {
        ArrayList<SeaAnimal> seaAnimals = Initializer.seaAnimals;

        int x = 0;
        while (x < seaAnimals.size()) {
            SeaAnimal seaAnimal = seaAnimals.get(x);
            embedBuilder.appendDescription(seaAnimal.getEmojiOfItem() + " **" + seaAnimal.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(seaAnimal.getName())) + ") — " + (seaAnimal.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(seaAnimal.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    seaAnimal.getDescription() + "\n\n");
            x++;

            if (x > 7) return;
        }
    }

    public static void buildDescription4(EmbedBuilder embedBuilder, long userId) {
        ArrayList<SeaAnimal> seaAnimals = Initializer.seaAnimals;

        int x = 7;
        while (x < seaAnimals.size()) {
            SeaAnimal seaAnimal = seaAnimals.get(x);
            embedBuilder.appendDescription(seaAnimal.getEmojiOfItem() + " **" + seaAnimal.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(seaAnimal.getName())) + ") — " + (seaAnimal.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(seaAnimal.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    seaAnimal.getDescription() + "\n\n");
            x++;
        }
    }

    public static void buildDescription5(EmbedBuilder embedBuilder, long userId) {
        ArrayList<Artifact> artifacts = Initializer.artifacts;

        int x = 0;
        while (x < artifacts.size()) {
            Artifact artifact = artifacts.get(x);
            embedBuilder.appendDescription(artifact.getEmojiOfItem() + " **" + artifact.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(artifact.getName())) + ") — " + "<:shekels:906039266650505256> [" + formatter.format(artifact.getCostToBuy()) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    artifact.getDescription() + "\n\n");
            x++;

            if (x > 7) return;
        }
    }

    public static void buildDescription6(EmbedBuilder embedBuilder, long userId) {
        ArrayList<Artifact> artifacts = Initializer.artifacts;

        int x = 7;
        while (x < artifacts.size()) {
            Artifact artifact = artifacts.get(x);
            embedBuilder.appendDescription(artifact.getEmojiOfItem() + " **" + artifact.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(artifact.getName())) + ") — " + "<:shekels:906039266650505256> [" + formatter.format(artifact.getCostToBuy()) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    artifact.getDescription() + "\n\n");
            x++;
        }
    }

    public static void buildDescription7(EmbedBuilder embedBuilder, long userId) {
        ArrayList<Tool> tools = Initializer.tools;

        int x = 0;
        while (x < tools.size()) {
            Tool tool = tools.get(x);
            embedBuilder.appendDescription(tool.getEmojiOfItem() + " **" + tool.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(tool.getName())) + ") — " + "<:shekels:906039266650505256> [" + formatter.format((30_000)) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    tool.getDescription() + "\n\n");
            x++;
        }
    }
}