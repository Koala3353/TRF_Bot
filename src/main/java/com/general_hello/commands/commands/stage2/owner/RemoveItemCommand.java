package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.xdrop.fuzzywuzzy.FuzzySearch;

public class RemoveItemCommand extends Command {
    public RemoveItemCommand() {
        this.name = "removeitem";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            // Splits the args and places it in vars
            String[] args = event.getArgs().split("\\s+");
            int amount = Integer.parseInt(args[0]);
            String itemName = FuzzySearch.extractTop(args[1], Initializer.nameToItem.keySet(), 1).get(0).getString();
            long userid = Long.parseLong(args[2]);
            // Adds the item
            Player.addItem(userid, -amount, itemName);
            event.reply("Successfully removed " + amount + " " + itemName + " to " + event.getJDA().getUserById(userid).getAsTag());
        } catch (Exception e) {
            event.reply("Invalid format placed. Correct format: `!removeitem [amount] [item name] [userid]`\n" +
                    "`!removeitem 10 ITEM 506056364859064330`");
        }
    }
}
