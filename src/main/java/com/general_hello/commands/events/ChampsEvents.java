package com.general_hello.commands.events;

import com.general_hello.Config;
import com.general_hello.commands.objects.GlobalVariables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChampsEvents extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getGuild().getIdLong() != Config.getLong("guild")) {
            return;
        }

        AtomicBoolean isChamp = new AtomicBoolean(false);
        Member member = event.getMember();
        member.getRoles().forEach(role -> {
            if (role.getName().startsWith(Config.get("champ_prefix"))) {
                isChamp.set(true);
            }
        });

        if (event.getTextChannel().getIdLong() != GlobalVariables.CHAMPS) {
            return;
        }

        if (isChamp.get()) {
            Message message = event.getMessage();
            String contentRaw = message.getContentRaw();
            message.delete().queue();

            try {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(member.getColor());
                embedBuilder.setTimestamp(OffsetDateTime.now());
                embedBuilder.setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl());
                embedBuilder.setDescription(contentRaw);
                event.getTextChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            } catch (Exception e) {
                event.getTextChannel().sendMessage("Check if your message exceeded 4096 characters. " + member.getAsMention())
                        .queue((message1 -> {
                            message1.delete().queueAfter(10, TimeUnit.SECONDS);
                        }));
            }
        }
    }
}
