package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class RemoveBerriCommand extends Command {
    public RemoveBerriCommand() {
        this.name = "removeberri";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Splits the args and places it in vars
            String[] args = event.getArgs().split("\\s+");
            int berri = Integer.parseInt(args[0]);
            long userid = Long.parseLong(args[1]);
            // Gets the player from the user that was retrieved by using the user id
            User user = event.getJDA().getUserById(userid);
            Player player = DataUtils.getPlayer(user);
            // Sets the berri
            player.setBerri(player.getBerri() - berri);
            event.reply("Successfully removed " + berri + " berri to " + user.getAsTag());
        } catch (Exception e) {
            event.reply("Invalid format placed. Correct format: `!removeberri [amount] [userid]`\n" +
                    "`!removeberri 100 506056364859064330`");
        }
    }
}
