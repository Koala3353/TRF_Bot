package com.general_hello.commands.commands.stage2.owner;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Items.Item;
import com.general_hello.commands.Objects.Items.LimitedItems;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.List;

public class RestockOwnerCommand extends Command {
    public RestockOwnerCommand() {
        this.name = "restock";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }
        try {
            // Splits the args and places it in vars
            String[] split = event.getArgs().split("\\s+");
            int amount = Integer.parseInt(split[1]);
            String itemString = split[0];
            List<ExtractedResult> results = FuzzySearch.extractTop(itemString, Initializer.nameToItem.keySet(), 1);
            Item item = Initializer.nameToItem.get(results.get(0).getString());
            // Restock the item
            if (item instanceof LimitedItems limitedItems) {
                limitedItems.setCurrentStock(amount);
                event.reply("Successfully changed the stock to " + amount);
            } else {
                event.reply("Restock failed: No item found that fits the criteria");
            }
        } catch (Exception e) {
            event.reply("Incorrect format placed. Example: `!restock [item] [amount]`");
        }
    }
}
