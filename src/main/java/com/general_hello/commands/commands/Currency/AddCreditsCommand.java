package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.Config;
import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;

import java.io.IOException;
import java.sql.SQLException;

public class AddCreditsCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        if (!ctx.getAuthor().getId().equals(Config.get("owner_id")) && !ctx.getAuthor().getId().equals(Config.get("owner_id_partner"))) {
            ctx.getChannel().sendMessage("Only the owner can do this!").queue();
            return;
        }

        if (ctx.getMessage().getMentionedMembers().isEmpty()) {
            ctx.getChannel().sendMessage("Kindly mention someone!").queue();
            return;
        }

        int moneey;

        String money = ctx.getArgs().get(1);
        try {
            moneey = Integer.parseInt(money);
        } catch (NumberFormatException e) {
            ctx.getChannel().sendMessage("Kindly send a valid number!").queue();
            return;
        }

        DatabaseManager.INSTANCE.setCredits(ctx.getMessage().getMentionedMembers().get(0).getIdLong(), moneey);
        ctx.getChannel().sendMessage("Successfully added " + money + " to " + ctx.getMessage().getMentionedMembers().get(0).getAsMention()).queue();
    }

    @Override
    public String getName() {
        return "addcredit";
    }

    @Override
    public String getHelp(String prefix) {
        return "GIMME MONEY";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
