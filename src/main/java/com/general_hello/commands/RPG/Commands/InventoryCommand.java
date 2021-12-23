package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Objects;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InventoryCommand extends Command {
    public static HashMap<Long, ArrayList<String>> embedPaginatorMessage = new HashMap<>();
    public InventoryCommand() {
        this.name = "inv";
        String[] aliases = new String[1];
        aliases[0] = "inventory";
        this.aliases = aliases;
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        User author = event.getAuthor();
        ArrayList<String> allNames = Initializer.allNames;
        long authorId = author.getIdLong();
        int x = 0;
        int y = 0;

        ArrayList<String> inventories = new ArrayList<>();
        StringBuilder inventory = new StringBuilder();
        boolean isAdded = true;

        while (x < allNames.size()) {
            int itemCount = RPGUser.getItemCount(authorId, allNames.get(x));
            if (itemCount > 0) {
                Objects objects = Initializer.allItems.get(RPGDataUtils.filter(allNames.get(x)));
                inventory.append(objects.getEmojiOfItem()).append(" **")
                        .append(objects.getName()).append("** ─ ")
                        .append(itemCount).append("\n")
                        .append("*ID* `").append(RPGDataUtils.filter(objects.getName())).append("`")
                        .append(" ─ ").append(objects.getRarity().getName()).append("\n\n");
                y++;
            }

            if (y%5==0) {
                inventories.add(inventory.toString());
                inventory = new StringBuilder();
                isAdded = false;
            }
            x++;
        }

        if (isAdded) {
            inventories.add(inventory.toString());
        }

        inventories.remove(0);

        if (inventories.get(0).equals("")) {
            event.replyError("You do not have anything in your inventory");
            return;
        }

        boolean disableNext = inventories.size() < 2;

        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.BLACK).setTitle("Owned Items");
        embedBuilder.setAuthor(author.getName() + "'s inventory", null, author.getAvatarUrl());
        embedBuilder.setDescription(inventories.get(0));
        embedBuilder.setFooter("You can use 'ignt sell [item]' to sell an item. ─ Page 1 of " + inventories.size());
        event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.of(ButtonStyle.PRIMARY, author.getId() + ":previousInv:1", Emoji.fromMarkdown("<:left:915425233215827968>")).asDisabled(),
                Button.of(ButtonStyle.PRIMARY, author.getId() + ":nextInv:1", Emoji.fromMarkdown("<:right:915425310592356382>")).withDisabled(disableNext)
        ).queue(
                (message -> {
                    embedPaginatorMessage.put(message.getIdLong(), inventories);
                })
        );
    }
}
