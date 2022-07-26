package com.general_hello.bot.commands;

import com.general_hello.bot.objects.GlobalVariables;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.time.OffsetDateTime;

public class DashboardCommand extends SlashCommand {
    public static final LastUsed LAST_USED = new LastUsed(-1, -1, -1, -1);
    private static long messageId = -1;
    public DashboardCommand() {
        this.name = "dashboard";
        this.help = "Sends the dashboard to manage the bot";
        this.ownerCommand = true;
        this.userPermissions = new Permission[]{
                Permission.MANAGE_SERVER,
                Permission.MESSAGE_MANAGE
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        event.reply("Sending the dashboard...").setEphemeral(true).queue();
        buildAndSend(event.getChannel().asTextChannel());
    }

    public static void buildAndSend(TextChannel textChannel) {
        EmbedBuilder dashboard = new EmbedBuilder();
        dashboard.setTitle("Dashboard");
        Member bot = textChannel.getGuild().getSelfMember();
        dashboard.setColor(bot.getColor());
        dashboard.setFooter(bot.getEffectiveName(), bot.getAvatarUrl()).setTimestamp(OffsetDateTime.now());
        dashboard.setDescription("""
                > This is the **dashboard** of the bot. To use the command, use the selection menu below.
                
                **Commands:**
                > [Shutdown](https://discord.com) - `Shuts the bot down`
                > [Reload data](https://discord.com) - `Reloads all the games data`
                > [Extract champ data](https://discord.com) - `Extracts the champ data from the database to a json file`
                > [Send Hall of Fame](https://discord.com) - `Sends the Hall of Fame`
                > [Restart Daily Leaderboard](https://discord.com) - `Restarts the daily leaderboard task`
                """);
        if (LAST_USED.hasData) {
            dashboard.appendDescription("\n\n[**Last used:**](" + GlobalVariables.LINK + ")\n");

            if (LAST_USED.getShutdown() != -1) {
                dashboard.appendDescription("**Shutdown:** " + formatTime(LAST_USED.getShutdown()) + "\n");
            }

            if (LAST_USED.getReloadData() != -1) {
                dashboard.appendDescription("**Reload Data:** " + formatTime(LAST_USED.getReloadData()) + "\n");
            }

            if (LAST_USED.getExtractData() != -1) {
                dashboard.appendDescription("**Extract Champ Data:** " + formatTime(LAST_USED.getExtractData()) + "\n");
            }

            if (LAST_USED.getHof() != -1) {
                dashboard.appendDescription("**Send Hall of Fame:** " + formatTime(LAST_USED.getHof()) + "\n");
            }
        }

        SelectMenu menu = SelectMenu.create("menu:dashboard")
                .setPlaceholder("Choose your command") // shows the placeholder indicating what this menu is for
                .setRequiredRange(1, 1) // only one can be selected
                .addOption("Shutdown", "shutdown", "Shuts the bot down")
                .addOption("Reload data", "reloaddata", "Reloads all the games data")
                .addOption("Extract Champ data", "extractdata", "Extract all the champs to a json file")
                .addOption("Send Hall of Fame", "hof", "Gets the top post and sends it to the channel")
                .addOption("Restart Daily Leaderboard", "restartlb", "Restarts the daily leaderboard task")
                .build();

        if (messageId == -1) {
            textChannel.sendMessageEmbeds(dashboard.build()).setActionRow(menu).queue((message ->
                    messageId = message.getIdLong()));
        } else {
            textChannel.editMessageEmbedsById(messageId, dashboard.build()).setActionRow(menu).queue();
        }
    }

    public static boolean deleteDashboard(TextChannel textChannel) {
        try {
            if (messageId == -1) {
                return true;
            }

            textChannel.deleteMessageById(messageId).queue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String formatTime(long time) {
        return TimeFormat.RELATIVE.format(time) + " (" + TimeFormat.DATE_TIME_SHORT.format(time) + ")";
    }

    public static class LastUsed {
        private long shutdown;
        private long reloadData;
        private long extractData;
        private long hof;
        private boolean hasData;

        public LastUsed(long shutdown, long reloadData, long extractData, long hof) {
            this.shutdown = shutdown;
            this.reloadData = reloadData;
            this.extractData = extractData;
            this.hof = hof;
            this.hasData = false;
        }

        public long getExtractData() {
            return extractData;
        }

        public void setExtractData(long extractData) {
            this.extractData = extractData;
            this.hasData = true;
        }

        public long getHof() {
            return hof;
        }

        public void setHof(long hof) {
            this.hof = hof;
            this.hasData = true;
        }

        public long getShutdown() {
            return shutdown;
        }

        public void setShutdown(long shutdown) {
            this.shutdown = shutdown;
            this.hasData = true;
        }

        public long getReloadData() {
            return reloadData;
        }

        public void setReloadData(long reloadData) {
            this.reloadData = reloadData;
            this.hasData = true;
        }
    }
}
