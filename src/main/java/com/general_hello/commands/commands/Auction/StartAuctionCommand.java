package com.general_hello.commands.commands.Auction;

import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;

import java.io.IOException;
import java.sql.SQLException;

public class StartAuctionCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        if (!ctx.getAuthor().getId().equals(Config.get("owner_id"))) {
            ctx.getChannel().sendMessage("Only the owner can do this!").queue();
            return;
        }

        ctx.getChannel().sendMessage("How long will the auction be? Format: `2d 3h 5m`").queue();
        QuestionProgress.progress.put(ctx.getMember(), Progress.TIME);
    }

    @Override
    public String getName() {
        return "startauction";
    }

    @Override
    public String getHelp(String prefix) {
        return "Creates an auction for your wonderful bidders to bid :D";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
