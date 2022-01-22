package com.general_hello.commands.commands.GroupOfGames.Blackjack;

import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Utils.MoneyData;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.concurrent.TimeUnit;

public class DoubleDownCommand implements ICommand {
    @Override
    public void handle(CommandContext e) throws InterruptedException {
        if (e.getArgs().isEmpty()) {
            BlackjackGame bjg = GameHandler.getBlackJackGame(e.getAuthor().getIdLong());
            if (bjg != null) {
                e.getMessage().delete().queueAfter(4, TimeUnit.SECONDS);
                if (bjg.canDouble()){
                    if (MoneyData.money.get(e.getAuthor()) - 2*bjg.getBet() >= 0){
                        bjg.doubleDown();
                        e.getChannel().retrieveMessageById(bjg.getMessageId()).queue(m -> {
                            EmbedBuilder eb = bjg.buildEmbed(e.getAuthor().getName(), e.getGuild());
                            if (bjg.hasEnded()) {
                                int d = bjg.getWonCreds();
                                GameHandler.removeBlackJackGame(e.getAuthor().getIdLong());
                                RPGUser.addShekels(e.getAuthor().getIdLong(), d);
                                eb.addField("Shekels", "You now have " + d + " more shekels", false);
                            }
                            m.editMessageEmbeds(eb.build()).queue();
                        });
                    } else {
                        e.getChannel().sendMessage("You have not enough shekels").queue();
                    }
                } else {
                    e.getChannel().sendMessage("You can't do that").queue();
                }
            } else e.getChannel().sendMessage("No game has been started! Type `ignt bj` to start one!").queue();
        }
    }

    @Override
    public String getName() {
        return "double";
    }

    @Override
    public String getHelp(String prefix) {
        return "Double the bet and take last card\n" +
                "Usage: `" + prefix + getName() + "`";
    }
    @Override
    public CommandType getCategory() {
        return CommandType.GAMES;
    }
}
