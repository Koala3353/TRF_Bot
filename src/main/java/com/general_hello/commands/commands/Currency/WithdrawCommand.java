package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class WithdrawCommand extends Command {
    public WithdrawCommand() {
        this.name = "withdraw";
        String[] aliases = new String[1];
        aliases[0] = "with";
        this.aliases = aliases;
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        if (event.getArgs().isEmpty()) {
            event.replyError("Kindly state how much to " + this.name);
            return;
        }
        long idLong = event.getAuthor().getIdLong();
        int bank = RPGUser.getBank(idLong);
        int money;

        String s = event.getArgs();
        s = s.replaceAll("m", "000000");
        s = s.replaceAll("k", "000");

        if (!s.equalsIgnoreCase("all")) {
            try {
                money = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                event.replyError("Check your number!\n\n" +
                        "Developer logs:\n" + "```java\n" + e.getMessage() + "\n" + "```");
                return;
            }
        } else {
            money = bank;
        }

        if (money > bank) {
            event.replyError("You don't even have that money in your bank .-.");
            return;
        }

        if (money < 1) {
            event.replyError("Don't bother test me 😡");
            return;
        }

        RPGUser.transferBankToShekels(idLong, money);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(InfoUserCommand.randomColor());
        embedBuilder.addField("Withdrawn", RPGEmojis.shekels + " `" + RPGDataUtils.formatter.format(money) + "`", false);
        embedBuilder.addField("Current Wallet Balance", RPGEmojis.shekels + " `" + RPGDataUtils.formatter.format(RPGUser.getShekels(idLong)) + "`", true);
        embedBuilder.addField("Current Bank Balance", RPGEmojis.shekels + " `" + RPGDataUtils.formatter.format(RPGUser.getBank(idLong)) + "`", true);
        event.reply(embedBuilder.build());
    }
}
