package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.Bot;
import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.time.OffsetDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommonDrop {
    public static void runDrop() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(CommonDrop::drop, 0, 13, TimeUnit.MINUTES);
    }

    private static void drop() {
        if (!shouldDrop()) return;

        Bot.jda.getTextChannelById(876363970108334162L).sendMessage("<@&905691492205621278>").queue();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Common Chest found!").setTimestamp(OffsetDateTime.now()).setColor(InfoUserCommand.randomColor());
        embedBuilder.setDescription("A new common chest " + RPGEmojis.common_chest + " has been found! **First igniter** to press the button below will get a chest that contains random common items!");
        embedBuilder.setThumbnail(RPGEmojis.customEmojiUrl(Emoji.fromMarkdown(RPGEmojis.common_chest)));
        Bot.jda.getTextChannelById(876363970108334162L).sendMessageEmbeds(embedBuilder.build()).setActionRows(
                ActionRow.of(Button.of(ButtonStyle.PRIMARY, "0000:claimcommondrop", "Open"), Button.of(ButtonStyle.DANGER, "0000:NADAME", "Dropped by Someone").asDisabled())
        ).queue((message -> {
            DropCommand.isClaimed.put(message.getIdLong(), false);
        }));
    }

    private static boolean shouldDrop() {
        int i = UtilNum.randomNum(1, 100);

        return i > 50;
    }
}
