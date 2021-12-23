package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Objects;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BuyCommand extends Command {
    public BuyCommand() {
        this.name = "buy";
        this.cooldown = 10;
        this.cooldownScope = CooldownScope.USER;
    }
    @Override
    protected void execute(CommandEvent event) {
        long authorId = event.getAuthor().getIdLong();
        if (event.getArgs().isEmpty()) {
            event.replyError("Kindly place what item you'll buy, and the quantity (Optional)\n" +
                    "`ignt buy ruby 5` or `ignt buy ruby`");
            return;
        }

        String arg = event.getArgs();
        String[] args = arg.split("\\s+");

        int amount = 1;
        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (Exception e) {
                event.replyError("Invalid number placed! Try again!");
            }
        }

        if (!Initializer.allItems.containsKey(args[0])) {
            event.replyError("There is no such item that you want to buy.");
            return;
        }

        Objects item = Initializer.allItems.get(args[0]);
        Integer cost = item.getCostToBuy();

        if (cost == null) {
            event.replyError("The item you chose is out of stock. Kindly do `ignt shop` for more details.");
            return;
        }

        cost = cost * amount;
        int shekels = RPGUser.getShekels(authorId);

        if (shekels < cost) {
            event.replyWarning("You don't have enough money to buy this item.");
            return;
        }

        RPGUser.addShekels(authorId, -cost);
        RPGUser.addItem(authorId, amount, args[0]);
        event.reply("Successfully bought " + amount + " " + item.getEmojiOfItem() + " " + item.getName() + " for " + RPGEmojis.shekels + " " + RPGDataUtils.formatter.format(cost) + ".");
    }
}
