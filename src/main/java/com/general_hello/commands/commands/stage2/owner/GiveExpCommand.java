package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class GiveExpCommand extends Command {
    /**
     *
     * <p>This class is a command that gives Exp to users (Owner command)
     *
     * @author General Rain
     */
    public GiveExpCommand() {
        this.name = "giveexp";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Splitting the input
            String[] args = event.getArgs().split("\\s+");
            // Storing it to vars
            int exp = Integer.parseInt(args[0]);
            long userid = Long.parseLong(args[1]);
            // Gets the player with the user from the user id
            User user = event.getJDA().getUserById(userid);
            Player player = DataUtils.getPlayer(user);
            // Sets the exp
           player.setExp(player.getExp() + exp);
           // Success reply
            event.reply("Successfully gave " + exp + " EXP to " + user.getAsTag());
        } catch (Exception e) {
            // Invalid format
            event.reply("Invalid format placed. Correct format: `!giveexp [amount] [userid]`\n" +
                    "`!giveexp 100 506056364859064330`");
        }
    }
}
