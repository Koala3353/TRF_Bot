package com.general_hello.commands.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class FeedbackCommand extends SlashCommand {
    public FeedbackCommand() {
        this.name = "feedback";
        this.help = "Feedback a command of the bot";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        TextInput subject = TextInput.create("command", "Command Name", TextInputStyle.SHORT)
                .setPlaceholder("Command name to feedback")
                .setMinLength(7)
                .setMaxLength(30)
                .build();

        TextInput body = TextInput.create("feedback", "Feedback", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Your feedback goes here")
                .setMinLength(30)
                .setMaxLength(2000)
                .build();

        Modal modal = Modal.create("feedback", "Feedback")
                .addActionRows(ActionRow.of(subject), ActionRow.of(body))
                .build();

        event.replyModal(modal).queue();
    }
}
