package com.general_hello.commands.commands.Currency;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RemindWork extends Command {
    public RemindWork() {
        this.name = "remindwork";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getTextChannel().sendMessage("You will now be reminded when you need to work!").queue();
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("work reminder", true).get(0)).queue();
    }
}
