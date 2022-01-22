package com.general_hello.commands;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.OtherEvents.*;
import com.general_hello.commands.RPG.Commands.*;
import com.general_hello.commands.RPG.Commands.Fight.FightCommand;
import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.commands.Currency.DepositCommand;
import com.general_hello.commands.commands.Currency.RemindBeg;
import com.general_hello.commands.commands.Currency.RemindWork;
import com.general_hello.commands.commands.Currency.WithdrawCommand;
import com.general_hello.commands.commands.Emoji.Emojis;
import com.general_hello.commands.commands.Giveaway.StartGiveawayCommand;
import com.general_hello.commands.commands.Hangman.VirtualBotManager;
import com.general_hello.commands.commands.Marriage.AdoptCommand;
import com.general_hello.commands.commands.Marriage.DivorceCommand;
import com.general_hello.commands.commands.Marriage.FamilyTreeCommand;
import com.general_hello.commands.commands.Marriage.LeaveSonCommand;
import com.general_hello.commands.commands.Others.CommandCountCommand;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Scanner;

public class Bot {
    public static JDA jda;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    private static Bot bot;
    private final EventWaiter eventWaiter;
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static Bot getBot() {
        return bot;
    }

    public Bot() throws LoginException, InterruptedException {
        bot = this;
        DatabaseManager.INSTANCE.getPrefix(-1);
        WebUtils.setUserAgent("IgnBot");
        EmbedUtils.setEmbedBuilder(
                () -> new EmbedBuilder()
                        .setColor(Color.cyan)
                        .setFooter("ignt help")
        );
        // Initialize the waiter and client
        CommandClientBuilder client = new CommandClientBuilder();


        // Set the client settings
        client.useDefaultGame();
        client.setOwnerId(Config.get("owner_id"));
        client.setCoOwnerIds(Config.get("owner_id_partner"));
        client.setPrefix(Config.get("prefix") + " ").setAlternativePrefix(Config.get("prefix"));
        client.setEmojis(Emojis.BABY_YODA, Emojis.MOD, Emojis.ERROR);

        client.useHelpBuilder(false);

        addCommands(client);
        eventWaiter = new EventWaiter();
        // Finalize the command client
        client.addSlashCommand(new StartGiveawayCommand());
        CommandClient commandClient = client.build();

        jda = JDABuilder.createDefault(Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_INVITES
        )
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new VirtualBotManager(true),
                        new OnButtonClick(), new OnRPGButtonClick(), eventWaiter, commandClient, new OnApiRequest(), new OnSetupMessage(), new OnButtonClickAuction(),
                        new OnReadyEvent(), new OnSelectionMenu(),
                        new OtherEvents(), new OnPrivateMessage(),
                        new Listener(), new CommandCounter())
                .setActivity(Activity.watching("ignt help"))
                .setStatus(OnlineStatus.ONLINE)
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .build().awaitReady();
    }

    public static void main(String[] args) throws LoginException {
        final int cores = Runtime.getRuntime().availableProcessors();
        if (cores <= 1) {
            System.out.println("Available Cores \"" + cores + "\", setting Parallelism Flag");
            System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");
        }
        Initializer.initializer();
        commandPrompt();
    }

    public static void commandPrompt() throws LoginException {
        boolean question = false;
        boolean question1 = false;
        TextChannel textChannel = null;

        Scanner scanner = new Scanner(System.in);
        System.out.println(ANSI_RED + "Program Loaded!\n" +
                "Welcome to Ignite Bot Command Line! Have a great day!" + ANSI_RESET);
        while (true) {
            String s = scanner.nextLine();
            JDA jda = Listener.jda;

            if (s.equalsIgnoreCase("msgshutdown")) {
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Status").setColor(Color.RED).setFooter("This maintenance is for all Ignite bots").setDescription(jda.getSelfUser().getAsMention() + " is currently offline due to some maintenance!");
                jda.getTextChannelById(891816519498096650L).sendMessageEmbeds(embedBuilder.build()).queue();
                System.out.println("Successfully sent the message!");
            }

            if (s.equalsIgnoreCase("msgstart")) {
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Status").setColor(Color.GREEN).setFooter("This status is for all Ignite bots").setDescription(jda.getSelfUser().getAsMention() + " is now online! The problem has been resolved and the maintenance is complete!");
                jda.getTextChannelById(891816519498096650L).sendMessageEmbeds(embedBuilder.build()).queue();
                System.out.println("Successfully sent the message!");
            }

            if (s.equalsIgnoreCase("help")) {
                System.out.println(ANSI_BLUE + "Option 1: startbot = To start the bot\n" +
                        "Option 2: botinfo = To get info of the bot\n" +
                        "Option 3: sendmsg = To send a message with the bot\n" +
                        "Option 4: disconnect channel = To disconnect the channel of the server!\n" +
                        "Option 5: msgshutdown = To send the shutdown reason in #shutdown!\n" +
                        "Option 6: msgstart = To send the start up message in #startups!\n" +
                        "Option 7: startactivity = To start the activity changing crap!\n" +
                        "Option 8: stop = To stop running the program!" + ANSI_RESET);
            }

            if (s.equalsIgnoreCase("disconnect channel")) {
                question1 = false;
                System.out.println("Successfully disconnected!");
            }

            if (question1) {
                System.out.println("The message you want to send is " + s);
                System.out.println("Sending the message now.....");
                try {
                    textChannel.sendMessage(s).queue();
                    System.out.println("Message successfully sent!");
                } catch (Exception ignored) {
                    System.out.println("Message failed to send!");

                }
            }

            if (s.equalsIgnoreCase("jda")) {
                System.out.println(jda);
            }

            if (s.equalsIgnoreCase("startactivity")) {
                Status status = new Status();
                status.status().start();
            }

            if (question) {
                try {
                    System.out.println(jda.getTextChannelById(s).getName() + " is the channel that the message will be sent in!");
                } catch (Exception ignored) {
                }
                System.out.println("What will the message be?");
                question1 = true;
                question = false;
                textChannel = jda.getTextChannelById(s);
            }

            if (s.equalsIgnoreCase("startbot")) {
                System.out.println("Starting the bot up!");
                try {
                    new Bot();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        

            if (s.equalsIgnoreCase("botinfo")) {
                String asTag = jda.getSelfUser().getAsTag();
                String avatarUrl = jda.getSelfUser().getAvatarUrl();
                OffsetDateTime timeCreated = jda.getSelfUser().getTimeCreated();
                String id = jda.getSelfUser().getId();
                System.out.println("Tag of the bot: " + asTag);
                System.out.println("Avatar url: " + avatarUrl);
                System.out.println("Time created: " + timeCreated);
                System.out.println("Id: " + id);
                System.out.println("Shard info: " + jda.getShardInfo().getShardString());
            }

            if (s.equalsIgnoreCase("sendmsg")) {
                System.out.println("What is the channel id that you wish to send the message in?");
                question = true;
            }

            if (s.equalsIgnoreCase("stop")) {
                System.out.println("Thank you for using UBCL have a great day!");
                jda.shutdown();
                break;
            }
        }
    }

    private static void addCommands(CommandClientBuilder clientBuilder) {
        //add here
        clientBuilder.addCommand(new RemindBeg());
        clientBuilder.addCommand(new RemindWork());
        clientBuilder.addCommand(new ShopCommand());
        clientBuilder.addCommand(new BuyCommand());
        clientBuilder.addCommand(new SellCommand());
        clientBuilder.addCommand(new AddShekelsCommand());
        clientBuilder.addCommand(new InventoryCommand());
        clientBuilder.addCommand(new FishCommand());
        clientBuilder.addCommand(new HuntCommand());
        clientBuilder.addCommand(new FamilyTreeCommand());
        clientBuilder.addCommand(new CookCommand());
        clientBuilder.addCommand(new AdoptCommand());
        clientBuilder.addCommand(new HealthCommand());
        clientBuilder.addCommand(new DivorceCommand());
        clientBuilder.addCommand(new LeaveSonCommand());
        clientBuilder.addCommand(new FightCommand());
        clientBuilder.addCommand(new WithdrawCommand());
        clientBuilder.addCommand(new DepositCommand());
        clientBuilder.addCommand(new CommandCountCommand());
        clientBuilder.addCommand(new OpenCommand());
        clientBuilder.addCommand(new AddItemCommand());
    }
}