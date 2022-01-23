package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Chest;
import com.general_hello.commands.RPG.Objects.Objects;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OpenCommand extends Command {
    public OpenCommand() {
        this.name = "open";
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) return;

        long authorId = event.getAuthor().getIdLong();
        if (event.getArgs().isEmpty()) {
            event.replyError("Kindly place what item you'll open (Loot boxes)\n" +
                    "`ignt open legendarychest 5` or `ignt open commonchest`");
            return;
        }

        String arg = event.getArgs();
        String[] args = arg.split("\\s+");

        if (!Initializer.chestToId.containsKey(RPGDataUtils.filter(args[0]))) {
            event.replyError("There is no such loot box that you want to open.");
            return;
        }

        int toOpenCount = 1;
        if (args.length > 1) {
            int countReq = Integer.parseInt(args[1]);
            if (countReq < 1) {
                event.replyError("You can't open stuff that's below 1 🤪");
                return;
            }

            toOpenCount = countReq;
        }

        if (RPGUser.getItemCount(authorId, RPGDataUtils.filter(args[0])) < toOpenCount) {
            event.replyError("You don't have any chests!");
            return;
        }

        Chest chest = Initializer.chestToId.get(RPGDataUtils.filter(args[0]));

        EmbedBuilder openingEmbed = new EmbedBuilder();
        openingEmbed.setTitle("Opening " + chest.getName())
                .setThumbnail(chest.getEmojiUrl())
                .setFooter(toOpenCount + "x " + chest.getName() + " being opened");
        RPGUser.addItem(authorId, -toOpenCount, RPGDataUtils.filter(args[0]));
        int finalToOpenCount = toOpenCount;
        event.getChannel().sendMessageEmbeds(openingEmbed.build()).queue(
                (message -> {
                    int shekelsReward = chest.getRandomShekels();
                    RPGUser.addShekels(authorId, shekelsReward);
                    List<Objects> tempItems = chest.getRandomItems(finalToOpenCount);
                    List<Objects> randomItems = new ArrayList<>();
                    int x = 0;
                    HashMap<Objects, Integer> randomItemsToCount = new HashMap<>();
                    while (x < tempItems.size()) {
                        Objects objectA = tempItems.get(x);
                        if (!randomItemsToCount.containsKey(objectA)) {
                            randomItemsToCount.put(objectA, 1);
                            randomItems.add(objectA);
                        } else {
                            Integer count = randomItemsToCount.get(objectA) + 1;
                            randomItemsToCount.put(objectA, count);
                        }
                        x++;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    x = 0;
                    while (x < randomItems.size()) {
                        Objects randomObject = randomItems.get(x);
                        Integer countOfObject = randomItemsToCount.get(randomObject);
                        RPGUser.addItem(authorId, countOfObject, RPGDataUtils.filter(randomObject.getName()));
                        stringBuilder.append("`x").append(countOfObject).append("` - ")
                                .append(randomObject.getEmojiOfItem()).append(" ")
                                .append(randomObject.getName()).append("\n");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        x++;
                    }
                    EmbedBuilder openedEmbed = new EmbedBuilder();
                    openedEmbed.setTitle(event.getAuthor().getName() + "'s Loot Haul!")
                            .setFooter(finalToOpenCount + "x " + chest.getName() + " opened")
                            .setDescription("**Shekels:** \n" +
                                    RPGEmojis.shekels + " " + RPGDataUtils.formatter.format(shekelsReward) + "\n\n" +
                                    "**Items:**" + "\n" +
                                    stringBuilder)
                            .setThumbnail(chest.getEmojiUrl())
                            .setColor(Color.PINK);
                    message.editMessageEmbeds(openedEmbed.build()).queueAfter(3, TimeUnit.SECONDS);
                })
        );
    }
}
