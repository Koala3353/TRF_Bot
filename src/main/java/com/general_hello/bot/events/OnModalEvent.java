package com.general_hello.bot.events;

import com.general_hello.bot.database.DataUtils;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public class OnModalEvent extends ListenerAdapter {
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("register")) {
            String solanaAddress = event.getValue("name").getAsString().replaceAll("\n", "");
            if (DataUtils.verifyAddress(solanaAddress) || solanaAddress.equalsIgnoreCase("override")) {
                DataUtils.newAccount(event.getUser().getIdLong(), solanaAddress);
                event.reply("Successfully stored your Solana Address within the bot.").setEphemeral(true).queue();
            } else {
                event.reply("Invalid Solana Address placed.").setEphemeral(true).queue();
            }
        }
    }
}
