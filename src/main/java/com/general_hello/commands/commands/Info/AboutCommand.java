package com.general_hello.commands.commands.Info;


import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.PrefixStoring;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;

import java.awt.*;
import java.lang.management.ManagementFactory;

public class AboutCommand implements ICommand {
    String[] featureses = new String[2];
    public boolean IS_AUTHOR = true;
    public String REPLACEMENT_ICON = "+";
    public Color color;
    public String description;
    public String oauthLink;

    public AboutCommand(Color color, String description) {
        this.color = color;
        this.description = description;
    }

    @Override
    public void handle(CommandContext event) throws InterruptedException {

        final long guildID = event.getGuild().getIdLong();
        String prefix = PrefixStoring.PREFIXES.computeIfAbsent(guildID, (id) -> Config.get("prefix"));

        if (oauthLink == null) {
               oauthLink = "https://discord.com/api/oauth2/authorize?client_id=888586282098364437&permissions=8&scope=bot%20applications.commands";
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.cyan);
        builder.setAuthor("All about " + event.getSelfUser().getName() + "!", null, event.getSelfUser().getAvatarUrl());
        event.getChannel().createInvite().complete().getUrl();
        boolean join = !event.getChannel().retrieveInvites().complete().isEmpty();
        boolean inv = !oauthLink.isEmpty();
        String invline = "\n" + (join ? "Join my server [here](https://discord.gg/tXX654E9rM)" : (inv ? "Please " : ""))
                + (inv ? (join ? ", or " : "") + "[invite](" + oauthLink + ") me to your server" : "") + "!";
        String author = event.getJDA().getUserById(Config.get("owner_id"))==null ? "<@" + event.getJDA().getUserById(Config.get("owner_id"))+">"
                : event.getJDA().getUserById(Config.get("owner_id")).getName();
        StringBuilder descr = new StringBuilder().append("Hello! I am **").append(event.getSelfUser().getName()).append("**, ")
                .append(description).append("\nI ").append(IS_AUTHOR ? "was written in Java" : "am owned").append(" by **")
                .append(author).append("** using " + "Chewtils")
                .append("and the [JDA library](" + JDAInfo.GITHUB + ")")
                .append("\nType `").append(prefix).append("help")
                .append("` to see my commands!").append(join || inv ? invline : "").append("\n");


        descr.append("\n");
        descr.append("Max memory: ").append((int) (Runtime.getRuntime().maxMemory() * 0.000001)).append(" mb\n");
        descr.append("Memory Usage: ").append((int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 0.00000001)).append(" mb");
        descr.append("\n");
        String messageCPU = String.valueOf(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()).startsWith("-") ? ("Unavailable") : (ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() + "%");
        descr.append("\nCPU Usage: ");
        descr.append("\n").append(messageCPU).append("\nProcessors: ").append(ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors()).append(" processors");
        //Here
        System.out.println("Total Memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Free Memory: " + Runtime.getRuntime().freeMemory());
        System.out.println("Max Memory: " + Runtime.getRuntime().maxMemory());

        builder.setDescription(descr);
        event.getJDA().getShardInfo();
        builder.addField("Stats", (event.getJDA().getGuilds().size() + " Servers\nJDA version " + (JDAInfo.VERSION)), true);
        builder.addField("This shard", event.getJDA().getUsers().size() + " Users\n" + event.getJDA().getGuilds().size() + " Servers", true);
        builder.addField("", event.getJDA().getTextChannels().size() + " Text Channels\n" + event.getJDA().getVoiceChannels().size() + " Voice Channels", true);
        builder.setFooter("Among sus", null);
        event.getMessage().replyEmbeds(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "about";
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows information about the bot!\n" +
                "Usage: `" + prefix + "about`";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
