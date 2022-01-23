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

public class UncommonDrop {
    public static void runDrop() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(UncommonDrop::drop, 0, 30, TimeUnit.MINUTES);
    }

    private static void drop() {
        if (!shouldDrop()) return;

        Bot.jda.getTextChannelById(876363970108334162L).sendMessage("<@&905691492205621278>").queue();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Uncommon Chest found!").setTimestamp(OffsetDateTime.now()).setColor(InfoUserCommand.randomColor());
        embedBuilder.setDescription("A new uncommon chest " + RPGEmojis.uncommon_chest + " has been found! **First igniter** to press the button below will get a chest that has random uncommon items!");
        embedBuilder.setThumbnail(RPGEmojis.customEmojiUrl(Emoji.fromMarkdown(RPGEmojis.uncommon_chest)));
        Bot.jda.getTextChannelById(876363970108334162L).sendMessageEmbeds(embedBuilder.build()).setActionRows(
                ActionRow.of(Button.of(ButtonStyle.PRIMARY, "0000:claimuncommondrop", "Open"), Button.of(ButtonStyle.DANGER, "0000:NADAME", "Dropped by the Kraken").asDisabled())
        ).queue((message -> {
            DropCommand.isClaimed.put(message.getIdLong(), false);
        }));
    }

    private static boolean shouldDrop() {
        int i = UtilNum.randomNum(1, 100);

        return i > 70;
    }
}
