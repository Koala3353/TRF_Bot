package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;

import java.io.IOException;
import java.sql.SQLException;

public class PreviewCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        String url = "https://app.resetxd.repl.co/webshot?website=https://" + ctx.getArgs().get(0);
        ctx.getChannel().sendMessage(url).queue();
    }

    @Override
    public String getName() {
        return "preview";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows you a preview of a website!\n" +
                "Usage: `" + prefix + " preview discord.com`";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.OTHERS;
    }
}
