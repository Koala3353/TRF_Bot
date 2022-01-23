package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.Bot;
import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.*;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.ButtonPaginator;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ShopCommand extends Command {
    public static final DecimalFormat formatter = new DecimalFormat("#,###.00");
    public static ArrayList<Chest> chests = new ArrayList<>();
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
            ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                    .setEventWaiter(Bot.getBot().getEventWaiter())
                    .setItemsPerPage(5)
                    .setTimeout(1, TimeUnit.MINUTES)
                    .useNumberedItems(false);

            builder.setTitle("Shop")
                    .setItems(buildDescription(event.getAuthor().getIdLong()))
                    .addAllowedUsers(event.getAuthor().getIdLong())
                    .setColor(Color.decode("#452350"));
            builder.build().paginate(event.getTextChannel(), 1);
            return;
        }
        String name = event.getArgs().replaceAll("'", "").replaceAll("^[\"']\\w+[\"']", "").replaceAll("\\s+", "").toLowerCase();
        name = RPGDataUtils.filter(name);

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
                try {
                    embedBuilder.setThumbnail(landAnimal.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
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
                try {
                    embedBuilder.setThumbnail(seaAnimal.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
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
                try {
                    embedBuilder.setThumbnail(tool.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
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
                try {
                    embedBuilder.setThumbnail(artifact.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }

            if (Initializer.chestToId.containsKey(name)) {
                Chest chest = Initializer.chestToId.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(chest.getName() + " (" + RPGUser.getItemCount(event.getAuthor().getIdLong(), RPGDataUtils.filter(name)) + " owned)");
                embedBuilder.setColor(Color.YELLOW);
                embedBuilder.setDescription("> " + chest.getDescription() + "\n\n" +
                        "**BUY** - " + (chest.getCostToBuy() == null ? "Out of Stock" : RPGEmojis.shekels + " [" + chest.getFormattedPrice() + "](https://discord.com)") + "\n" +
                        "**SELL** - " + RPGEmojis.shekels + " [" + chest.getFormattedSellPrice() + "](https://discord.com)\n" +
                        "**TRADE** - Unknown");
                embedBuilder.addField("Rarity", "`" + chest.getRarity().getName() + "`", true);
                embedBuilder.addField("Type", "`Loot boxes`", true);
                embedBuilder.addField("ID", "`" + name + "`", true);
                try {
                    embedBuilder.setThumbnail(chest.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
                chests.add(chest);
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).setActionRows(ActionRow.of(
                        Button.secondary(event.getAuthor().getId() + ":chestcontents:" + (chests.size()), "Possible Items")
                )).queue();
                return;
            }

            if (Initializer.powerUpToId.containsKey(name)) {
                Powerup powerup = Initializer.powerUpToId.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(powerup.getName() + " (" + RPGUser.getItemCount(event.getAuthor().getIdLong(), RPGDataUtils.filter(name)) + " owned)");
                embedBuilder.setColor(Color.cyan);
                embedBuilder.setDescription("> " + powerup.getDescription() + "\n\n" +
                        "**BUY** - " + (powerup.getCostToBuy() == null ? "Out of Stock" : RPGEmojis.shekels + " [" + powerup.getFormattedPrice() + "](https://discord.com)") + "\n" +
                        "**SELL** - " + RPGEmojis.shekels + " [" + powerup.getFormattedSellPrice() + "](https://discord.com)\n" +
                        "**TRADE** - Unknown");
                embedBuilder.addField("Rarity", "`" + powerup.getRarity().getName() + "`", true);
                embedBuilder.addField("Type", "`Power Up`", true);
                embedBuilder.addField("ID", "`" + name + "`", true);
                try {
                    embedBuilder.setThumbnail(powerup.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                return;
            }
        } catch (Exception e) {
            event.getTextChannel().sendMessage("The item you searched for is not there!").queue();
            e.printStackTrace();
            isErrorOutputed = true;
        }

        if (!isErrorOutputed) {
            event.getTextChannel().sendMessage("The item you searched for is not there!").queue();
        }
    }

    private static ArrayList<String> buildDescription(long userId) {
        ArrayList<String> content = new ArrayList<>();
        ArrayList<LandAnimal> landAnimals = Initializer.landAnimals;

        int x = 0;
        while (x < landAnimals.size()) {
            LandAnimal landAnimal = landAnimals.get(x);
            content.add(landAnimal.getEmojiOfItem() + " **" + landAnimal.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(landAnimal.getName())) + ") — " + (landAnimal.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(landAnimal.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    landAnimal.getDescription() + "\n");
            x++;
        }

        ArrayList<SeaAnimal> seaAnimals = Initializer.seaAnimals;

        x = 0;
        while (x < seaAnimals.size()) {
            SeaAnimal seaAnimal = seaAnimals.get(x);
            content.add(seaAnimal.getEmojiOfItem() + " **" + seaAnimal.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(seaAnimal.getName())) + ") — " + (seaAnimal.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(seaAnimal.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    seaAnimal.getDescription() + "\n");
            x++;
        }

        ArrayList<Artifact> artifacts = Initializer.artifacts;

        x = 0;
        while (x < artifacts.size()) {
            Artifact artifact = artifacts.get(x);
            content.add(artifact.getEmojiOfItem() + " **" + artifact.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(artifact.getName())) + ") — " + "<:shekels:906039266650505256> [" + formatter.format(artifact.getCostToBuy()) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    artifact.getDescription() + "\n");
            x++;
        }

        ArrayList<Tool> tools = Initializer.tools;

        x = 0;
        while (x < tools.size()) {
            Tool tool = tools.get(x);
            content.add(tool.getEmojiOfItem() + " **" + tool.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(tool.getName())) + ") — " + "<:shekels:906039266650505256> [" + formatter.format((30_000)) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    tool.getDescription() + "\n");
            x++;
        }

        ArrayList<Chest> chests = Initializer.chests;

        x = 0;
        while (x < chests.size()) {
            Chest chest = chests.get(x);
            content.add(chest.getEmojiOfItem() + " **" + chest.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(chest.getName())) + ") — " + "<:shekels:906039266650505256> " + (chest.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(chest.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    chest.getDescription() + "\n");
            x++;
        }

        ArrayList<Powerup> powerUps = Initializer.powerUps;

        x = 0;
        while (x < powerUps.size()) {
            Powerup powerup = powerUps.get(x);
            content.add(powerup.getEmojiOfItem() + " **" + powerup.getName() + "** " +
                    "(" + RPGUser.getItemCount(userId, RPGDataUtils.filter(powerup.getName())) + ") — " + "<:shekels:906039266650505256> " + (powerup.getCostToBuy() == null ? "[Out of stock" : "<:shekels:906039266650505256> [" + formatter.format(powerup.getCostToBuy())) + "](https://www.youtube.com/watch?v=dQw4w9WgXcQ)\n" +
                    powerup.getDescription() + "\n");
            x++;
        }

        return content;
    }
}