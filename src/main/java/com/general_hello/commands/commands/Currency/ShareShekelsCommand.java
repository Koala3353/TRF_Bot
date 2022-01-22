package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.Emoji.Emojis;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class ShareShekelsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        try {
            if (ctx.getMessage().getMentionedUsers().isEmpty()) {
                ctx.getChannel().sendMessage("Kindly mention someone to give it to!").queue();
                return;
            }

            if (ctx.getArgs().size() > 2) {
                ctx.getChannel().sendMessage("Kindly state the money to be given!").queue();
                return;
            }
            User receiverOfShekels = ctx.getMessage().getMentionedUsers().get(0);

            if (ctx.getMessage().getMentionedMembers().get(0).equals(ctx.getMember())) {
                ctx.getChannel().sendMessage("You can't give to your self!!!").queue();
                return;
            }

            if (RPGDataUtils.checkUser(ctx)) {
                return;
            }

            User author = ctx.getAuthor();
            int shekelsOfGiver = RPGUser.getShekels(author.getIdLong());
            int money = Integer.parseInt(ctx.getArgs().get(0));

            int tax = (money) / 10;
            int toBeGiven = money - tax;

            if (shekelsOfGiver < money) {
                ctx.getChannel().sendMessage("You do not have enough money to give!").queue();
                return;
            }

            if (money < 1) {
                ctx.getChannel().sendMessage("You can't give less than 0!!!").queue();
                return;
            }

            if (money > 1_000_000) {
                ctx.getChannel().sendMessage("You can not share more than " + Emojis.shekels + " 1,000,000!!!").queue();
                return;
            }
            RPGUser.addShekels(receiverOfShekels.getIdLong(), toBeGiven);
            RPGUser.addShekels(author.getIdLong(), -toBeGiven);
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Successful");
            embedBuilder.setDescription("Successfully given  " + RPGEmojis.shekels + " **" + toBeGiven + "** to " + receiverOfShekels.getAsMention() + "!\n**" +
                    tax + "** shekels has been deducted due to tax!");

            embedBuilder.setFooter("3% tax was applied");
            embedBuilder.setTimestamp(OffsetDateTime.now());
            embedBuilder.setColor(InfoUserCommand.randomColor());
            ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(Button.of(ButtonStyle.DANGER, "TAXAMMOUNT", tax + " credits spent on tax").asDisabled()).queue();
        } catch (Exception e) {
            ctx.getChannel().sendMessage("An unknown error occurred! Kindly recheck your command!").queue();
        }
    }

    @Override
    public String getName() {
        return "share";
    }

    @Override
    public String getHelp(String prefix) {
        return "Gives credits to other people!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.WALLET;
    }
}
