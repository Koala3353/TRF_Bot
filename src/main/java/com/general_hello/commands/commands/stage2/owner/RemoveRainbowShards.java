package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

public class RemoveRainbowShards extends Command {
    public RemoveRainbowShards() {
        this.name = "removeshards";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Splits the args and places it in vars
            String[] args = event.getArgs().split("\\s+");
            int rainbowShards = Integer.parseInt(args[0]);
            long userid = Long.parseLong(args[1]);
            boolean bought = Boolean.getBoolean(args[2]);
            // Gets the player from the user that was retrieved by using the user id
            User user = event.getJDA().getUserById(userid);
            Player player = DataUtils.getPlayer(user);
            // Removes the rainbow shards
            if (bought) {
                player.setRainbowShardsBought(player.getRainbowShardsBought() - rainbowShards);
            } else {
                player.setRainbowShards(player.getRainbowShards() - rainbowShards);
            }
            event.reply("Successfully removed " + rainbowShards + " rainbow shards to " + user.getAsTag());
        } catch (Exception e) {
            event.reply("Invalid format placed. Correct format: `!removeshards [amount] [userid] [bought or not]`\n" +
                    "`!removeshards 100 506056364859064330 true`");
        }
    }
}
