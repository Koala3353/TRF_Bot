package com.general_hello;

import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.OtherEvents.OnButtonClick;
import com.general_hello.commands.OtherEvents.OnReadyEvent;
import com.general_hello.commands.OtherEvents.OnSelectionMenu;
import com.general_hello.commands.commands.stage1.*;
import com.general_hello.commands.commands.stage2.owner.*;
import com.general_hello.commands.commands.stage2.AchievementCommand;
import com.general_hello.commands.commands.stage2.DailyTaskCommand;
import com.general_hello.commands.commands.stage2.ProfileCommand;
import com.general_hello.commands.commands.stage2.TradeCommand;
import com.general_hello.commands.commands.stage3.PlayCommand;
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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot {
    public static JDA jda;
    public static final String PAYPAL_LINK = Config.get("paypal");
    public static final String PATREON_LINK = Config.get("patreon");
    private static Bot bot;
    private final EventWaiter eventWaiter;
    // Edit this for the patreon roles of your server
    public static List<Long> patreonRoles = List.of(969717027126255616L, 969716935250047068L, 969716818140856391L, 965060169090347028L, 965060140187418684L);
    // The logger of the bot
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
        client.useHelpBuilder(true);
        // Remove when done (Will remove on stage 4 completion)
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
        // Initialize the commands of the bot
        clientBuilder.addSlashCommands(new StartCommand(), new SupporterCommand(), new ShopCommand(),
                new InventoryCommand(), new PaypalCommand(), new PatreonCommand(),
                new SkillInfoCommand(), new ItemInfoCommand(), new AchievementCommand(), new MySkillsList(),
                new ProfileCommand(), new DailyTaskCommand(), new TradeCommand(), new RestockCommand(),
                new VoteCommand(), new PlayCommand());
        clientBuilder.addCommands(new SenseiCommand(), new LeaveCommand(), new RestockOwnerCommand(),
                new GiveRainbowShards(), new RemoveRainbowShards(), new GiveItemCommand(), new RemoveItemCommand(),
                new GiveExpCommand(), new RemoveExpCommand(), new GiveBerriCommand(), new RemoveBerriCommand());
        LOGGER.info("Added the slash commands and plain text commands");
    }

    public static void initialize() {
        LOGGER.info("Running the initializer");
        Initializer.initializer();
    }

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        // For the top.gg vote checker (Stage 4 for full documentation)
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public static String isVoted() {
        // For the top.gg vote checker (Stage 4 for full documentation)
        URL url = null;
        try {
            url = new URL("https://top.gg/api/bots/" + Bot.jda.getSelfUser().getIdLong() + "/check");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("GET");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("param1", "val");

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(parameters));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return con.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}