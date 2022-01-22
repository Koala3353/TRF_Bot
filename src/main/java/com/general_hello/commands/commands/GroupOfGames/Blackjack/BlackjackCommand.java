package com.general_hello.commands.commands.GroupOfGames.Blackjack;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.Emoji.Emojis;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.sql.SQLException;

public class BlackjackCommand implements ICommand {
    private GameHandler gameHandler;

    public BlackjackCommand(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }


    @Override
    public void handle(CommandContext e) throws InterruptedException, SQLException {
        User author = e.getAuthor();
        long playerId = author.getIdLong();

        if (e.getArgs().isEmpty()) {
            e.getChannel().sendMessage("Kindly place your bet after the command.\n" +
                    "For example: `ignt bj 1000`").queue();
            return;
        }

        if (Integer.parseInt(e.getArgs().get(0)) > DatabaseManager.INSTANCE.getCredits(author.getIdLong()) || Integer.parseInt(e.getArgs().get(0)) < 0) {
            e.getChannel().sendMessage("You do not have enough to make that bet!").queue();
            return;
        }

        if (Integer.parseInt(e.getArgs().get(0)) > 400_000) {
            e.getChannel().sendMessage("You can not blackjack more than " + Emojis.shekels + " 400,000!!!").queue();
            return;
        }

        if (Integer.parseInt(e.getArgs().get(0)) < 1) {
            e.getChannel().sendMessage("You can not blackjack less than " + Emojis.shekels + " 1!!!").queue();
            return;
        }

         BlackjackGame objg = GameHandler.getBlackJackGame(playerId);
                if (objg == null) {
                    BlackjackGame bjg = new BlackjackGame(Integer.parseInt(e.getArgs().get(0)));
                    EmbedBuilder eb = bjg.buildEmbed(author.getName(), e.getGuild());
                    if (!bjg.hasEnded()) {
                        GameHandler.putBlackJackGame(playerId, bjg);
                    } else {
                        double d = bjg.getWonCreds();
                        RPGUser.addShekels(e.getAuthor().getIdLong(), (int) d);
                        eb.addField("Shekels", "You now have " + d + " more shekels", false);
                    }

                    if (!bjg.hasEnded()) {
                        e.getChannel().sendMessageEmbeds(eb.build()).setActionRows(
                                ActionRow.of(
                                        Button.of(ButtonStyle.PRIMARY, playerId + ":hit", "Hit"),
                                        Button.of(ButtonStyle.PRIMARY, playerId + ":stand", "Stand"),
                                        Button.of(ButtonStyle.PRIMARY, playerId + ":double", "Double")
                                ), ActionRow.of(
                                        Button.of(ButtonStyle.PRIMARY, playerId + ":split", "Split"),
                                        Button.of(ButtonStyle.DANGER, playerId + ":endbj", "Surrender")
                                )
                        ).queue(m -> bjg.setMessageId(m.getIdLong()));
                    } else {
                        e.getChannel().sendMessageEmbeds(eb.build()).setActionRow(
                                Button.of(ButtonStyle.SUCCESS, "GG", "Game Ended").asDisabled()
                        ).queue();
                    }
                } else {
                    e.getChannel().sendMessage("You're already playing a game").queue();
                }
            }

    @Override
    public String getName() {
        return "bj";
    }

    @Override
    public String getHelp(String p) {
        return "Start a blackjack game!\n" +
                "Usage: `" + p + "bj [amount of money]`";
    }

    public static int getInt(String s) {
        s = s.replaceAll("m", "000000");
        s = s.replaceAll("k", "000");

        int i;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            i = -1;
        }
        return i;
    }


    @Override
    public CommandType getCategory() {
        return CommandType.GAMES;
    }
}
