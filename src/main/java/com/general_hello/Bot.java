package com.general_hello;

import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.OtherEvents.OnButtonClick;
import com.general_hello.commands.OtherEvents.OnReadyEvent;
import com.general_hello.commands.OtherEvents.OnSelectionMenu;
import com.general_hello.commands.commands.*;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA jda;
    public static final String PAYPAL_LINK = Config.get("paypal");
    public static final String PATREON_LINK = Config.get("patreon");
    private static Bot bot;
    private final EventWaiter eventWaiter;
    // If you want to log stuff
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static Bot getBot() {
        return bot;
    }

    public Bot() throws LoginException, InterruptedException {
        bot = this;
        // Initialize the waiter and client
        CommandClientBuilder client = new CommandClientBuilder();


        // Set the client settings
        client.setOwnerId(Config.get("owner_id"));
        client.setCoOwnerIds(Config.get("owner_id_partner"));
        client.setPrefix(Config.get("prefix"));
        client.useHelpBuilder(false);
        // Remove when done
        client.forceGuildOnly(Config.get("guild"));

        addCommands(client);
        eventWaiter = new EventWaiter();
        // Finalize the command client
        CommandClient commandClient = client.build();

        jda = JDABuilder.createDefault(Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(eventWaiter, commandClient,
                        new OnReadyEvent(), new OnSelectionMenu(), new OnButtonClick())
                .setStatus(OnlineStatus.IDLE)
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .build().awaitReady();
    }

    public static void main(String[] args) throws LoginException {
        initialize();
        try {
            new Bot();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addCommands(CommandClientBuilder clientBuilder) {
        // commands here
        clientBuilder.addSlashCommands(new StartCommand(), new SupporterCommand(), new ShopCommand(),
                new InventoryCommand(), new PaypalCommand(), new PatreonCommand(),
                new SkillInfoCommand(), new ItemInfoCommand(), new AchievementCommand(), new MySkillsList(),
                new ProfileCommand());
        clientBuilder.addCommands(new SenseiCommand());
        LOGGER.info("Added the slash commands");
    }

    public static void initialize() {
        // some code
        LOGGER.info("Running the initializer");
        Initializer.initializer();
    }
}