package com.general_hello.commands.commands.stage1;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class LeaveCommand extends Command {
    public LeaveCommand() {
        this.name = "leave";
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Checks if the user made an account
            if (DataUtils.makeCheck(event)) {
                return;
            }

            // Gets the player
            Player player = DataUtils.getPlayer(event.getAuthor());
            // Checks if they have a sensei
            if (player.getSenseiId() == -1) {
                event.reply("You don't have a Sensei or Student to leave.");
                return;
            }

            // Gets the user with the sensei id
            User user = event.getJDA().getUserById(player.getSenseiId());

            if (DataUtils.hasAccount(user) == -1) {
                event.reply(user.getAsMention() + " didn't made an account still.");
                return;
            }

            if (DataUtils.getPlayer(user).getSenseiId() == -1) {
                event.reply("The user doesn't have a Sensei or Student.");
                return;
            }
            // Resets the sensei
            player.setSenseiId(-1);
            DataUtils.getPlayer(user).setSenseiId(-1);
           event.getChannel().sendMessage("Successfully left your Sensei or Student").queue();
        } catch (Exception ignored) {}
    }
}
