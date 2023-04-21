package com.general_hello;

import com.general_hello.bot.commands.*;
import com.general_hello.bot.events.OnButtonClick;
import com.general_hello.bot.events.OnMemberJoin;
import com.general_hello.bot.events.OnReadyEvent;
import com.general_hello.bot.objects.GlobalVariables;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    private static JDA jda;
    private static Bot bot;
    private final EventWaiter eventWaiter;
    // The (logger) of the bot
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public static Bot getBot() {
        return bot;
    }

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static JDA getJda() {
        return jda;
    }

    public static void setJda(JDA jda) {
        LOGGER.info("JDA object initialized");
        Bot.jda = jda;
    }

    public Bot() throws LoginException, InterruptedException {
        bot = this;
        // Initialize the waiter and client
        CommandClientBuilder client = new CommandClientBuilder();
        // Set the client settings
        client.setOwnerId(Config.get("owner_id"));
        client.setCoOwnerIds(Config.get("owner_id_partner"));
        client.setPrefix(Config.get("prefix"));
        client.setStatus(OnlineStatus.ONLINE);
        client.setActivity(Activity.listening("God"));
        addCommands(client);
        eventWaiter = new EventWaiter();
        // Finalize the command client
        CommandClient commandClient = client.build();

        jda = JDABuilder.createDefault(Config.get("token"),
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.MESSAGE_CONTENT,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                    GatewayIntent.SCHEDULED_EVENTS,
                    GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(eventWaiter, commandClient, new OnReadyEvent(),
                        new OnButtonClick(), new OnMemberJoin())
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.SCHEDULED_EVENTS, CacheFlag.VOICE_STATE)
                .build().awaitReady();
    }

    public static void main(String[] args) throws LoginException {
        LOGGER.info("Starting program v." + GlobalVariables.VERSION);
        try {
            new Bot();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addCommands(CommandClientBuilder clientBuilder) {
        // Initialize the commands of the bot
        clientBuilder.addSlashCommands(new LoadCommand(), new AddQuestion(), new NewQuizCommand(), new DeleteQuizCommand(),
                new NewHierarchicalRolesCommand(), new SendSelectRoleMessage(), new EODCommand(), new GlobalEODCommand(),
                new DeleteQuestionCommand(), new ModifyQuestionCommand());
        LOGGER.info("Added the slash commands");
        clientBuilder.addContextMenus(new AddChallengeMenu());
        LOGGER.info("Added the context menus");
    }
}