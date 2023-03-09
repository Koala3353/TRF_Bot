package com.general_hello.bot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class NewQuizCommand extends SlashCommand {
    public NewQuizCommand() {
        this.name = "newquiz";
        this.help = "Create a new quiz";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        event.getUser().openPrivateChannel().queue((channel) -> {
            channel.sendMessage("What channel do you want the quiz to be sent?")
                    .setComponents(ActionRow.of(Button.primary("0000:quizsetup:channel", "Answer"))).queue();
        });
        event.reply("The setup is sent to your DMs. Kindly check it.").setEphemeral(true).queue();
    }
}
