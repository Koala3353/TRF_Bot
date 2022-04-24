package com.general_hello.commands.commands;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.general_hello.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class SenseiCommand extends Command {
    public SenseiCommand() {
        this.name = "sensei";
        this.help = "Makes someone your sensei";
        this.cooldown = 60;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }

        Player player = DataUtils.getPlayer(event.getAuthor());
        if (player.getSenseiId() != -1) {
            event.reply("You already have a sensei.");
            return;
        }

        List<User> mentionedUsers = event.getMessage().getMentionedUsers();
        if (mentionedUsers.isEmpty()) {
            event.reply("Mention someone to be your sensei");
            return;
        }

        User user = mentionedUsers.get(0);

        if (user.equals(event.getAuthor())) {
            event.reply("You can't be your own sensei!");
        }

        if (DataUtils.hasAccount(user) == -1) {
            event.reply(user.getAsMention() + " didn't made an account still.");
            return;
        }
        event.getChannel().sendMessageEmbeds(EmbedUtil.defaultEmbed(user.getAsMention() + ", " + event.getAuthor().getAsMention() + " wants you to become their sensei!\n" +
                "> **Do you accept?**")).setActionRow(Button.primary(user.getId() + ":sensei:yes", "Yes"), Button.danger(user.getId() + ":sensei:no", "No")).queue();
    }
}
