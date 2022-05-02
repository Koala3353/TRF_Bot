package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.xdrop.fuzzywuzzy.FuzzySearch;

public class GiveItemCommand extends Command {
    /**
     * <p>Gives an item to the user (Owner command)
     *
     * @author General Rain
     */
    public GiveItemCommand() {
        // Name
        this.name = "giveitem";
        // Owner Command setting
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Splitting to args
            String[] args = event.getArgs().split("\\s+");
            // Storing it to vars
            int amount = Integer.parseInt(args[0]);
            // Matches the closest item to the name
            String itemName = FuzzySearch.extractTop(args[1], Initializer.nameToItem.keySet(), 1).get(0).getString();
            long userid = Long.parseLong(args[2]);
            // Adds the item
            Player.addItem(userid, amount, itemName);
            // Success reply
            event.reply("Successfully gave " + amount + " " + itemName + " to " + event.getJDA().getUserById(userid).getAsTag());
        } catch (Exception e) {
            // Error Reply
            event.reply("Invalid format placed. Correct format: `!giveitem [amount] [item name] [userid]`\n" +
                    "`!giveitem 10 ITEM 506056364859064330`");
        }
    }
}
