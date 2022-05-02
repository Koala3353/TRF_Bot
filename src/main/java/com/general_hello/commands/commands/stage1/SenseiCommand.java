package com.general_hello.commands.commands.stage1;

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
        try {
            // Checks if the user has an account, doesn't have a sensei, mentioned a user to be their sensei
            if (DataUtils.makeCheck(event)) {
                return;
            }

            Player player = DataUtils.getPlayer(event.getAuthor());
            if (player.getSenseiId() != -1) {
                event.reply("You already have a Sensei.");
                return;
            }

            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            if (mentionedUsers.isEmpty()) {
                event.reply("Mention someone to be your Sensei");
                return;
            }

            User user = mentionedUsers.get(0);

            if (user.getId().equals(event.getAuthor().getId())) {
                event.reply("You can't be your own Sensei!");
                return;
            }

            if (DataUtils.hasAccount(user) == -1) {
                event.reply(user.getAsMention() + " has to register and make an account first.");
                return;
            }

            if (DataUtils.getPlayer(user).getSenseiId() != -1) {
                event.reply("The user already has a Sensei.");
                return;
            }
            // Accepting message
            event.getChannel().sendMessageEmbeds(EmbedUtil.defaultEmbed(user.getAsMention() + ", " + event.getAuthor().getAsMention() + " wants you to become their Sensei!\n" +
                    "> **Do you accept?**")).setActionRow(Button.primary(user.getId() + ":sensei:yes:" + event.getAuthor().getId(), "Yes"), Button.danger(user.getId() + ":sensei:no", "No")).queue();
        } catch (Exception ignored) {}
    }
}
