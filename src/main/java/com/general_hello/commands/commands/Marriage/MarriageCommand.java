package com.general_hello.commands.commands.Marriage;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MarriageCommand implements ICommand {
    public static HashMap<User, User> marriage = new HashMap<>();
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        User author = ctx.getAuthor();
        if (!marriage.containsKey(author)) {

            if (ctx.getMessage().getMentionedMembers().isEmpty()) {
                ctx.getChannel().sendMessage("Kindly mention your dear wife (or husband ofc)!").queue();
                return;
            }

            Member member = ctx.getMessage().getMentionedMembers().get(0);
            if (member.equals(ctx.getMember()) || member.getUser().isBot()) {
                ctx.getChannel().sendMessage("Ye shall not marry himself (or herself) or any good bots.").queue();
                return;
            }

            ctx.getChannel().sendMessage(member.getAsMention()).queue((message -> {
                message.delete().queueAfter(3, TimeUnit.SECONDS);
            }));

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Pending Confirmation").setDescription(author.getAsMention() + " is proposing to you!\n" +
                    "**What is your answer?**");
            ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                    Button.danger(member.getId() + ":noMarry:" + member.getId(), "No"),
                    Button.success(member.getId() + ":yesMarry:" + member.getId(), "Yes")
            ).queue();
            return;
        }
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
