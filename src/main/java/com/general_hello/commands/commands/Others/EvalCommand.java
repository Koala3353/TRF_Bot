package com.general_hello.commands.commands.Others;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.sql.SQLException;

public class EvalCommand implements ICommand {
    @Override
    public void handle(CommandContext event) throws InterruptedException, IOException, SQLException {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
        se.put("event", event.getEvent());
        se.put("jda", event.getJDA());
        se.put("guild", event.getGuild());
        se.put("channel", event.getChannel());
        se.put("bot", event.getSelfUser());
        se.put("message", event.getMessage());
        try
        {
            event.getChannel().sendMessage("✅ Evaluated Successfully:\n```\n"+se.eval(event.getMessage().getContentRaw()
                    .replaceFirst("ignt", "").replace(getName(), "").replaceAll("\\s++", "")
            )+" ```").queue();
        }
        catch(Exception e)
        {
            event.getChannel().sendMessage("🛑 An exception was thrown:\n```\n"+e+" ```").queue();
        }
    }

    @Override
    public String getName() {
        return "eval";
    }

    @Override
    public String getHelp(String prefix) {
        return "Owner command only!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
