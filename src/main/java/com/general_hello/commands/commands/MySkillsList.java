package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Skill;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MySkillsList extends SlashCommand {
    public MySkillsList() {
        this.name = "skillslist";
        this.help = "Displays your skills that you have";
        this.cooldown = 10;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }

        User author = event.getUser();
        ArrayList<String> allNames = Initializer.allNames;
        long authorId = author.getIdLong();
        int x = 0;
        int y = 0;

        ArrayList<String> inventories = new ArrayList<>();
        StringBuilder inventory = new StringBuilder();

        while (x < allNames.size()) {
            int itemCount = Player.getItemCount(authorId, allNames.get(x));
            if (itemCount > 0 && Initializer.nameToSkill.containsKey(allNames.get(x))) {
                Skill objects = Initializer.nameToSkill.get((allNames.get(x)));
                inventory.append(objects.getEmojiOfItem()).append(" **")
                        .append(objects.getName()).append("** ─ ")
                        .append(objects.effectsString()).append("\n")
                        .append("*ID* `").append(DataUtils.filter(objects.getName())).append("`")
                        .append(" ─ ").append(objects.getFormattedPrice()).append("\n\n");
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
            event.reply("You do not have any skills").queue();
            return;
        }

        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(1)
                .setTimeout(1, TimeUnit.MINUTES)
                .useNumberedItems(false);

        builder.setTitle("**" + author.getName() + "**'s Skills")
                .setItems(inventories)
                .addAllowedUsers(event.getUser().getIdLong())
                .setColor(0x0099E1);

        event.reply("Loading your skills...").queue((interactionHook -> {
            interactionHook.deleteOriginal().queueAfter(3, TimeUnit.SECONDS);
        }));
        builder.build().paginate(event.getTextChannel(), 1);
    }
}
