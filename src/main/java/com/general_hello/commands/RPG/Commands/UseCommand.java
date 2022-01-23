package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Powerup;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.Utils.UtilNum;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class UseCommand extends Command {
    public UseCommand() {
        this.name = "use";
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        long authorId = event.getAuthor().getIdLong();
        if (event.getArgs().isEmpty()) {
            event.replyError("Kindly place what item you'll use (Power Ups) and how many (Optional)\n" +
                    "`ignt use bank 5` or `ignt use banknote`");
            return;
        }

        String arg = event.getArgs();
        String[] args = arg.split("\\s+");

        if (!Initializer.powerUpToId.containsKey(RPGDataUtils.filter(args[0]))) {
            event.replyError("There is no such power up that you want to use.");
            return;
        }

        int toOpenCount = 1;
        if (args.length > 1) {
            int countReq = Integer.parseInt(args[1]);
            if (countReq < 1) {
                event.replyError("You can't open stuff that's below 1 🤪");
                return;
            }

            toOpenCount = countReq;
        }

        if (RPGUser.getItemCount(authorId, RPGDataUtils.filter(args[0])) < toOpenCount) {
            event.replyError("You don't have any " + Initializer.powerUpToId.get(RPGDataUtils.filter(args[0])).getName() + "!");
            return;
        }

        Powerup powerup = Initializer.powerUpToId.get(RPGDataUtils.filter(args[0]));

        switch (powerup.getName().toLowerCase()) {
            case "bank note" -> {
                int BANKNOTEMAX = 100_000;
                int randomNum = UtilNum.randomNum(10_000, BANKNOTEMAX) * toOpenCount;
                RPGUser.addBankStorage(event.getAuthor().getIdLong(), randomNum);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.addField("Used", "`" + toOpenCount + "x` " + powerup.getEmojiOfItem(), true);
                embedBuilder.addField("Added Bank Space", RPGEmojis.shekels + " " + RPGDataUtils.formatter.format(randomNum), true);
                embedBuilder.addField("Total Bank Space", RPGEmojis.shekels + " " + RPGDataUtils.formatter.format(RPGUser.getBankStorage(event.getAuthor().getIdLong())), true);
                embedBuilder.setFooter("In total, you expanded your storage by " + " shekels");
                event.reply(embedBuilder.build());
            }
        }
    }
}
