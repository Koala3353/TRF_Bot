package com.general_hello.commands.commands.Marriage;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.RPG.Commands.ShopCommand;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class MarriageCommand implements ICommand {
    public static HashMap<User, User> marriage = new HashMap<>();
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        User author = ctx.getAuthor();
        if (marriage.containsKey(author)) {
            ctx.getChannel().sendMessage("You said `I love you` to " + marriage.get(author).getAsMention()).queue();
            return;
        }

        if (ctx.getMessage().getMentionedMembers().isEmpty()) {
            ctx.getChannel().sendMessage("Kindly mention your dear wife (or husband ofc)!").queue();
            return;
        }

        Member member = ctx.getMessage().getMentionedMembers().get(0);
        if (member.equals(ctx.getMember()) || member.getUser().isBot()) {
            ctx.getChannel().sendMessage("Ye shall not marry himself (or herself) or any good bots.").queue();
            return;
        }

        int i = UtilNum.randomNum(1, 100);
        int a = i * UtilNum.randomNum(1, 20000);
        DecimalFormat formatter = ShopCommand.formatter;
        DatabaseManager.INSTANCE.setCredits(ctx.getAuthor().getIdLong(), a);
        ctx.getChannel().sendMessage("Nice you married " + ctx.getMessage().getMentionedMembers().get(0).getAsMention() + " and **" + i + " people** gave you a total of " + RPGEmojis.credits + " " + formatter.format(a) + " as a marriage gift!").setActionRow(
                Button.of(ButtonStyle.SECONDARY, "0000:marrydono:" + ctx.getAuthor().getId(), "Give 100,000 credits")
        ).queue();
        marriage.put(ctx.getAuthor(), member.getUser());
    }

    @Override
    public String getName() {
        return "marry";
    }

    @Override
    public String getHelp(String prefix) {
        return "Love and marry!!!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
