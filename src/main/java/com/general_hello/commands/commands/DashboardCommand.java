package com.general_hello.commands.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.time.OffsetDateTime;

public class DashboardCommand extends Command {
    public static LastUsed LAST_USED = new LastUsed(-1, -1, -1);
    private static long messageId = -1;
    public DashboardCommand() {
        this.name = "dashboard";
        this.help = "Sends the dashboard to manage the bot";
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getMessage().delete().queue();

        buildAndSend(event.getTextChannel());
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
                > [Set result](https://discord.com) - `Sets the result of a game`
                > [Shutdown](https://discord.com) - `Shuts the bot down`
                > [Reload data](https://discord.com) - `Reloads all the games data`
                """);
        if (LAST_USED.hasData) {
            dashboard.appendDescription("\n\n[**Last used:**](https://discord.com)\n");

            if (LAST_USED.getSetResult() != -1) {
                dashboard.appendDescription("**Set Result:** " + formatTime(LAST_USED.getSetResult()) + "\n");
            }

            if (LAST_USED.getShutdown() != -1) {
                dashboard.appendDescription("**Shutdown:** " + formatTime(LAST_USED.getShutdown()) + "\n");
            }

            if (LAST_USED.getReloadData() != -1) {
                dashboard.appendDescription("**Reload Data:** " + formatTime(LAST_USED.getReloadData()) + "\n");
            }
        }

        SelectMenu menu = SelectMenu.create("menu:dashboard")
                .setPlaceholder("Choose your command") // shows the placeholder indicating what this menu is for
                .setRequiredRange(1, 1) // only one can be selected
                .addOption("Set result", "setresult", "Sets the result of a game")
                .addOption("Shutdown", "shutdown", "Shuts the bot down")
                .addOption("Reload data", "reloaddata", "Reloads all the games data")
                .build();

        if (messageId == -1) {
            messageId = textChannel.getLatestMessageIdLong();
        }

        textChannel.editMessageEmbedsById(messageId, dashboard.build()).setActionRow(menu).queue();
    }

    private static String formatTime(long time) {
        return TimeFormat.RELATIVE.format(time) + " (" + TimeFormat.DATE_TIME_SHORT.format(time) + ")";
    }
    public static class LastUsed {
        private long setResult;
        private long shutdown;
        private long reloadData;
        private boolean hasData;

        public LastUsed(long setResult, long shutdown, long reloadData) {
            this.setResult = setResult;
            this.shutdown = shutdown;
            this.reloadData = reloadData;
            this.hasData = false;
        }

        public long getSetResult() {
            return setResult;
        }

        public LastUsed setSetResult(long setResult) {
            this.setResult = setResult;
            this.hasData = true;
            return this;
        }

        public long getShutdown() {
            return shutdown;
        }

        public LastUsed setShutdown(long shutdown) {
            this.shutdown = shutdown;
            this.hasData = true;
            return this;
        }

        public long getReloadData() {
            return reloadData;
        }

        public LastUsed setReloadData(long reloadData) {
            this.reloadData = reloadData;
            this.hasData = true;
            return this;
        }
    }
}
