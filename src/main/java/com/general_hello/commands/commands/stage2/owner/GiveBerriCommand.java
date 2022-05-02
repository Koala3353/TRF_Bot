package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class GiveBerriCommand extends Command {
    /**
     * <p>This class is a command the gives berris to users (Owner command only)
     *
     * @author General Rain
     */
    public GiveBerriCommand() {
        // Name
        this.name = "giveberri";
        // Owner command setting
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Splitting the message and placing it in variables
            String[] args = event.getArgs().split("\\s+");
            int berri = Integer.parseInt(args[0]);
            long userid = Long.parseLong(args[1]);
            // Gets the user from the user id of the command
            User user = event.getJDA().getUserById(userid);
            // Gets the player object from the user
            Player player = DataUtils.getPlayer(user);
            // Sets the berri of the user
            player.setBerri(player.getBerri() + berri);
            // Replying
            event.reply("Successfully gave " + berri + " berri to " + user.getAsTag());
        } catch (Exception e) {
            // Invalid format reply
            event.reply("Invalid format placed. Correct format: `!giveberri [amount] [userid]`\n" +
                    "`!giveberri 100 506056364859064330`");
        }
    }
}
