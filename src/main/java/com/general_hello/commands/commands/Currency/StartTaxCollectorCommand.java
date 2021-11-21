package com.general_hello.commands.commands.Currency;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class StartTaxCollectorCommand extends Command {
    public StartTaxCollectorCommand() {
        this.ownerCommand = true;
        this.name = "starttax";
        this.help = "Starts the tax collector task";
    }

    @Override
    protected void execute(CommandEvent event) {
        TaxGetter.runTaxGetter();
        event.getTextChannel().sendMessage("Starting the ***TAX COLLECTOR.exe***").queue();
    }
}
