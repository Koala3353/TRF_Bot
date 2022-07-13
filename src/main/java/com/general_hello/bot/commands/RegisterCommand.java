package com.general_hello.bot.commands;

import com.general_hello.bot.database.DataUtils;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class RegisterCommand extends SlashCommand {
    public RegisterCommand() {
        this.name = "register";
        this.help = "Register your Solana Address to the bot";
        this.cooldown = 100;
        this.guildOnly = false;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.hasAccount(event.getUser().getIdLong())) {
            event.reply("You already made an account.").setEphemeral(true).queue();
            return;
        }
        TextInput name = TextInput.create("name", "Solana Token Address", TextInputStyle.SHORT)
                .setPlaceholder("Place your Solana Address here.")
                .setMinLength(31)
                .setMaxLength(45)
                .build();

        Modal modal = Modal.create("register", "Register")
                .addActionRows(ActionRow.of(name))
                .build();

        event.replyModal(modal).queue();
    }
}
