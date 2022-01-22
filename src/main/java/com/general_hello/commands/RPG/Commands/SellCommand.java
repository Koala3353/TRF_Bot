package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Objects;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SellCommand extends Command {
    public SellCommand() {
        this.name = "sell";
        this.cooldown = 10;
        this.cooldownScope = CooldownScope.USER;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) return;
        long authorId = event.getAuthor().getIdLong();
        if (event.getArgs().isEmpty()) {
            event.replyError("Kindly place what item you'll sell, and the quantity (Optional)\n" +
                    "`ignt sell ruby 5` or `ignt sell ruby`");
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
                return;
            }
        }

        if (amount < 1) {
            event.replyError("Don't try to do the hacks 😒");
            return;
        }

        if (!Initializer.allItems.containsKey(RPGDataUtils.filter(args[0]))) {
            event.replyError("There is no such item that you want to sell.");
            return;
        }

        Objects item = Initializer.allItems.get(args[0]);
        Integer sellPrice = item.getSellPrice();

        sellPrice = sellPrice * amount;

        int myItemCount = RPGUser.getItemCount(authorId, args[0]);
        if (myItemCount < amount) {
            event.reply("You do not have " + amount + " " + item.getName() + ". You only have " + myItemCount + ".");
            return;
        }

        RPGUser.addShekels(authorId, sellPrice);
        RPGUser.addItem(authorId, -amount, args[0]);
        event.reply("Successfully sold " + amount + " " + item.getEmojiOfItem() + " " + item.getName() + " for " + RPGEmojis.shekels + " " + RPGDataUtils.formatter.format(sellPrice) + ".");
    }
}