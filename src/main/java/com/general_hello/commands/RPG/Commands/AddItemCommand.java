package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Objects;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class AddItemCommand extends Command {
    public AddItemCommand() {
        this.name = "additems";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.getChannel().sendMessage("Kindly fix your command! `ignt additems 100 commonfish @Koala`").queue();
            return;
        }

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("Kindly mention someone! `ignt additems 100 commonfish @Koala`").queue();
            return;
        }

        int count;
        String item;
        User users = event.getMessage().getMentionedUsers().get(0);

        if (!RPGDataUtils.isRPGUser(users)) {
            event.replyError("The user doesn't have an RPG account yet!");
            return;
        }

        String args = event.getArgs();
        String[] split = args.split("\\s+");
        try {
            count = Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Kindly send a valid number!").queue();
            return;
        }

        item = split[1];

        if (!Initializer.allItems.containsKey(RPGDataUtils.filter(item))) {
            event.replyError("There is no such item!");
            return;
        }
        Objects object = Initializer.allItems.get(RPGDataUtils.filter(item));
        RPGUser.addItem(users.getIdLong(), count, RPGDataUtils.filter(item));
        event.reply("Successfully added " + count + " " + object.getEmojiOfItem() + " " + object.getName() + " to " + users.getName());
    }
}
