package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class RemoveExpCommand extends Command {
    public RemoveExpCommand() {
        this.name = "removeexp";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Splits the args and places it in vars
            String[] args = event.getArgs().split("\\s+");
            int exp = Integer.parseInt(args[0]);
            // Gets the player from the user that was retrieved by using the user id
            long userid = Long.parseLong(args[1]);
            User user = event.getJDA().getUserById(userid);
            Player player = DataUtils.getPlayer(user);
            // Sets the exp
            player.setExp(player.getExp() - exp);
            event.reply("Successfully removed " + exp + " EXP to " + user.getAsTag());
        } catch (Exception e) {
            event.reply("Invalid format placed. Correct format: `!removeexp [amount] [userid]`\n" +
                    "`!removeexp 100 506056364859064330`");
        }
    }
}
