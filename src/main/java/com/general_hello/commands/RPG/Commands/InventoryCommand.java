package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.Bot;
import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Objects;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.ButtonPaginator;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
                if (!inventory.toString().equals("")) {
                    inventories.add(inventory.toString());
                }
                inventory = new StringBuilder();
            }
            x++;
        }

        if (!inventory.toString().equals("")) {
            inventories.add(inventory.toString());
        }

        if (inventories.isEmpty()) {
            event.replyError("You do not have anything in your inventory");
            return;
        }

        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(1)
                .setTimeout(1, TimeUnit.MINUTES)
                .useNumberedItems(false);

        builder.setTitle("**" + author.getName() + "**'s inventory")
                .setItems(inventories)
                .addAllowedUsers(event.getAuthor().getIdLong())
                .setColor(Color.decode("#452350"));

        builder.build().paginate(event.getTextChannel(), 1);
    }
}
