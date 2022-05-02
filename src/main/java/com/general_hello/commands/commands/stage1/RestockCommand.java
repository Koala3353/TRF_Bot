package com.general_hello.commands.commands.stage1;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Item;
import com.general_hello.commands.Objects.LimitedItems;
import com.general_hello.commands.Objects.User.Player;
import com.general_hello.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RestockCommand extends SlashCommand {
    public RestockCommand() {
        this.name = "restock";
        this.ownerCommand = true;
        this.options = List.of(
                new OptionData(OptionType.STRING, "item", "The item you'll give")
                        .setRequired(true).setAutoComplete(true),
                new OptionData(OptionType.INTEGER, "amount", "Amount to give").setRequired(true));
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
        // Auto complete for the code
        if (event.getFocusedOption().getName().equals("item")) {
            List<ExtractedResult> item = FuzzySearch.extractTop(event.getOption("item").getAsString(), Initializer.nameToItem.keySet(), 5);
            Collection<Command.Choice> choices = new ArrayList<>();
            int x = 0;
            while (x < item.size()) {
                choices.add(new Command.Choice(item.get(x).getString(), item.get(x).getString()));
                x++;
            }
            event.replyChoices(choices).queue();
        }
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // Checks if the user made an account or not
        if (DataUtils.makeCheck(event)) {
            return;
        }

        // Gets the amount inputted
        int amount = event.getOption("amount").getAsInt();
        // Can't be negative
        if (amount < 1) {
            event.reply("Higher than 1 only.").queue();
            return;
        }
        // Gets the item closest to the given name
        String itemString = event.getOption("item").getAsString();
        ArrayList<Item> items = Initializer.items;
        List<ExtractedResult> results = FuzzySearch.extractTop(itemString, Initializer.allObjects.keySet(), 1);
        // It has to be a "Limited item"
        if (Initializer.nameToItem.get(results.get(0).getString()) instanceof LimitedItems) {} else {
            event.reply("The item doesn't fit within the criteria").queue();
            return;
        }
        // Removes the item from their account
        Player.addItem(event.getUser().getIdLong(), -amount, results.get(0).getString());
        StringBuilder summary = new StringBuilder();
        // Randomly adds some stock
        for (Item item : items) {
            if (item instanceof LimitedItems limitedItems) {
                int amountRestocked = UtilNum.randomNum(0, amount);
                if (limitedItems.getCurrentStock() + amountRestocked > limitedItems.getMaxStock()) {
                    amountRestocked = limitedItems.getMaxStock() - limitedItems.getCurrentStock();
                }
                amount -= amountRestocked;
                limitedItems.setCurrentStock(limitedItems.getCurrentStock() + amountRestocked);
                if (amountRestocked != 0) {
                    summary.append("Restocked ").append(limitedItems.getName()).append(" by ").append(amountRestocked).append("\n");
                }
            }
        }
        // Reply
        event.reply("**Successfully restocked the following items:**\n" + summary).queue();
    }
}
