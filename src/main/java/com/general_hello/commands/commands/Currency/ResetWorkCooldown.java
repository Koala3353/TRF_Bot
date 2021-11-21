package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class ResetWorkCooldown implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        if (ctx.getAuthor().getId().equals(Config.get("owner_id"))) {
            WorkCommand.cooldown = new HashMap<>();
            ctx.getChannel().sendMessage("Successfully Reset the cool down!!!").queue();
        }
    }

    @Override
    public String getName() {
        return "resetwork";
    }

    @Override
    public String getHelp(String prefix) {
        return "Resets the work cooldown";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
