 package com.general_hello.commands.OtherEvents;

 import com.general_hello.Config;
 import net.dv8tion.jda.api.EmbedBuilder;
 import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
 import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
 import net.dv8tion.jda.api.hooks.ListenerAdapter;
 import net.dv8tion.jda.api.interactions.ModalInteraction;
 import org.jetbrains.annotations.NotNull;

 import java.awt.*;
 import java.time.OffsetDateTime;
 import java.time.format.DateTimeFormatter;

 public class OnSelectionMenu extends ListenerAdapter {
    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        // Checks if the selection menu id starts with trade
        if (event.getSelectMenu().getId().startsWith("trade")) {
            String userId = event.getSelectMenu().getId().split(":")[1];
            if (event.getUser().getId().equals(userId)) {
                switch (event.getSelectedOptions().get(0).getValue()) {
                }
            }
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if ("feedback".equals(event.getModalId())) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setAuthor("New Feedback Received", null, event.getJDA().getSelfUser().getEffectiveAvatarUrl());
            embed.setFooter("Submitted at " + formatOffsetDateTime(OffsetDateTime.now()));
            embed.setColor(Color.GREEN);
            ModalInteraction interaction = event.getInteraction();
            embed.setDescription("[**User:**](" + Config.get("link") + ") " + event.getUser().getAsTag() + " (" + event.getUser().getId() + ")\n" +
                    "[**Command:**](" + Config.get("link") + ") `" + interaction.getValue("command").getAsString() + "`\n" +
                    "[**Feedback:**](" + Config.get("link") + ") ```java\n" + interaction.getValue("feedback").getAsString() + "```");
            event.getJDA().getTextChannelById(Config.get("feedback")).sendMessageEmbeds(embed.build()).queue();
            event.reply("Thank you for your feedback and have a nice day.").setEphemeral(true).queue();
        }
    }

     /**
      * Change OffsetDateTIme to human readable form.
      *
      * @param time the OffsetDateTIme to be formatted to
      * @return a string of the date in "Month/Day/Year Hour:Minute:Second AM/PM" format
      */
     public static String formatOffsetDateTime(OffsetDateTime time) {
         return DateTimeFormatter.ofPattern("M/d/u h:m:s a").format(time);
     }
}