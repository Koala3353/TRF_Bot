package com.general_hello.commands.commands.Currency;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RemindBeg extends Command {
    public RemindBeg() {
        this.name = "remindbeg";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getTextChannel().sendMessage("You will now be reminded when you need to beg!").queue();
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("beg reminder", true).get(0)).queue();
    }
}
