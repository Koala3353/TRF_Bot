package com.general_hello.bot.utils;

import com.general_hello.Bot;
import com.general_hello.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.utils.TimeFormat;
import net.dv8tion.jda.internal.utils.Checks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("ConstantConditions")
public class Util
{

    public static JDA firstShard()
    {
        return Bot.getJda();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    public static String timeFormat(long seconds)
    {
        return (new SimpleDateFormat("HH:mm:ss")).format(new Date(seconds));
    }

    public static void sendDM(String userId, CharSequence sequence)
    {
        firstShard().openPrivateChannelById(userId)
                .flatMap(channel -> channel.sendMessage(sequence))
                .queue( s -> Util.logInfo("Successfully sent a message to " + userId, Util.class), e -> {});
    }

    public static void sendDM(long userId, MessageEmbed embed, MessageEmbed... embeds)
    {
        Checks.notNull(embed, "Embed");
        Checks.noneNull(embeds, "Embeds");
        firstShard().openPrivateChannelById(userId)
                .flatMap(channel -> channel.sendMessageEmbeds(embed, embeds))
                .queue( s -> {}, e -> {} );

    }

    public static void sendOwnerDM(CharSequence sequence)
    {
        firstShard().openPrivateChannelById(Config.get("owner_id"))
                .flatMap(channel -> channel.sendMessage(sequence))
                .queue( s -> {}, e -> {} );
    }

    public static void sendOwnerDM(MessageEmbed embed, MessageEmbed... embeds)
    {
        Checks.notNull(embed, "Embed");
        Checks.noneNull(embeds, "Embeds");
        firstShard().openPrivateChannelById(Config.get("owner_id"))
                .flatMap(channel -> channel.sendMessageEmbeds(embed, embeds))
                .queue( s -> {}, e -> {} );
    }

    public static int zeroIfNegative(int x)
    {
        return Math.max(x, 0);
    }

    /**
     * Makes any number ordinal
     * e.g. 2 -> 2nd
     *
     * @param i Number to format
     * @return Ordinal number
     */
    public static String ordinal(int i)
    {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        return switch (i % 100)
                {
                    case 11, 12, 13 -> i + "th";
                    default -> i + suffixes[i % 10];
                };
    }

    public static String getJarPath()
    {
        try
        {
            CodeSource codeSource = Bot.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            return (jarFile.getParentFile().getPath());
        } catch (Exception e)
        {
            LOGGER.error("Could not get path of jar!", e);
            return null;
        }
    }

    public static ThreadFactory newThreadFactory(String threadName)
    {
        return newThreadFactory(threadName, LoggerFactory.getLogger(Bot.class));
    }

    public static ThreadFactory newThreadFactory(String threadName, boolean isDaemon)
    {
        return newThreadFactory(threadName, LoggerFactory.getLogger(Bot.class), isDaemon);
    }

    public static ThreadFactory newThreadFactory(String threadName, Logger logger)
    {
        return newThreadFactory(threadName, logger, true);
    }

    public static ThreadFactory newThreadFactory(String threadName, Logger logger, boolean isdaemon)
    {
        return (r) ->
        {
            Thread t = new Thread(r, threadName);
            t.setDaemon(isdaemon);
            t.setUncaughtExceptionHandler((final Thread thread, final Throwable throwable) ->
                    logger.error("There was a uncaught exception in the {} threadpool", thread.getName(), throwable));
            return t;
        };
    }

    public static String replaceLast(final String text, final String regex, final String replacement)
    {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    public static String format(String text, Object... arguments)
    {
        return MessageFormatter.arrayFormat(text, arguments).getMessage();
    }


    @SuppressWarnings("unchecked")
    public static <T> T[] addToArray(T[] source, T element)
    {
        T[] destination = (T[]) Array.newInstance(element.getClass(), source.length+1);
        System.arraycopy(source, 0, destination, 0, source.length);
        destination[source.length] = element;
        return destination;
    }

    public static int getListeningUsers(VoiceChannel channel)
    {
        int nonBots = 0;
        for (Member member : channel.getMembers())
        {
            if (!member.getUser().isBot() && !member.getVoiceState().isDeafened())
                nonBots++;
        }
        return nonBots;
    }

    public static void logInfo(String message, Class<?> clazz)
    {
        Logger CUSTOM_LOGGER = LoggerFactory.getLogger(clazz);
        CUSTOM_LOGGER.info(message);

        try {
            String filename= "log.txt";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            // format time
            fw.write(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd HH:mm:ss")) + " - " + message + "\n");//appends the string to the file
            fw.close();
        } catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public static void logWarning(String message, Class<?> clazz)
    {
        Logger CUSTOM_LOGGER = LoggerFactory.getLogger(clazz);
        CUSTOM_LOGGER.warn(message);
    }

    public static void logError(String message, Class<?> clazz)
    {
        Logger CUSTOM_LOGGER = LoggerFactory.getLogger(clazz);
        CUSTOM_LOGGER.error(message);
    }

    public static void logError(String message, Class<?> clazz, Exception e)
    {
        Logger CUSTOM_LOGGER = LoggerFactory.getLogger(clazz);
        CUSTOM_LOGGER.error(message, e);
    }

    public static String getTimestampDiscord(OffsetDateTime time)
    {
        return TimeFormat.RELATIVE.format(time);
    }

    public static TextChannel getChannelFromRole(Member member) {
        AtomicReference<TextChannel> returnTextChannel = new AtomicReference<>();
        member.getRoles().stream()
                .filter(role -> role.getName().toLowerCase().contains("ag"))
                .findFirst().flatMap(role -> member.getGuild().getTextChannels().stream()
                        .filter(channel -> channel.getName().toLowerCase().contains(role.getName().toLowerCase()))
                        .findFirst()).ifPresent(returnTextChannel::set);

        if (returnTextChannel.get() == null) {
            Util.logError("Could not find channel for " + member.getEffectiveName(), Util.class);
        }
        return returnTextChannel.get();
    }

    public static void createChannelIfDoesntExist(Guild guild, String name, String category, Member member) {
        if (guild.getTextChannels().stream().noneMatch(channel -> channel.getName().equalsIgnoreCase(name))) {
            List<Category> categories = guild.getCategoriesByName(category, true);
            long allowEveryone = Permission.MESSAGE_SEND.getRawValue();
            long denyEveryone = Permission.VIEW_CHANNEL.getRawValue();
            guild.createTextChannel(name).setParent(categories.get(0))
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(), allowEveryone, denyEveryone)
                    .addMemberPermissionOverride(member.getIdLong(), denyEveryone, allowEveryone).queue();
        }
    }

    public static void createCategoryIfDoesntExist(Guild guild, String name) {
        if (guild.getCategories().stream().noneMatch(category -> category.getName().equalsIgnoreCase(name))) {
            guild.createCategory(name).queue();
        }
    }

    public static void sendEodReportToChannel(Member member, int part) {
        TextChannel channel = member.getGuild().getTextChannelsByName("eod-report-" + member.getId(), true).get(0);
        if (channel == null) {
            LOGGER.warn("Could not find channel for EOD report");
            return;
        }
        EmbedBuilder eodReportEmbed = EODUtil.getEODReportEmbed(member, part, OffsetDateTime.now(ZoneId.of("UTC-6")).toString());
        channel.sendMessage("Hey " + member.getAsMention() + ", TRF bot wasn't able to send you an EOD as your DMs need to be enabled so please enable your DMs in your server privacy settings. In the meantime, feel free to answer the EOD here in this private channel until then.").setEmbeds(eodReportEmbed.build()).setComponents(EODUtil.getActionRow(part, OffsetDateTime.now(ZoneId.of("UTC-6")).toString())).queue();
    }
}